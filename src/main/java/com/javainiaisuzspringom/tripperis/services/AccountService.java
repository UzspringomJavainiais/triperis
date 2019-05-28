package com.javainiaisuzspringom.tripperis.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.dto.calendar.CalendarEntry;
import com.javainiaisuzspringom.tripperis.dto.entity.AccountDTO;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
import com.javainiaisuzspringom.tripperis.repositories.RoleRepository;
import com.javainiaisuzspringom.tripperis.services.calendar.AccountCalendarProvider;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Service
@Primary
public class AccountService extends AbstractBasicEntityService<Account, AccountDTO, Integer> implements UserDetailsService {

    @Getter
    @Autowired
    private AccountRepository repository;

    @Autowired
    private List<AccountCalendarProvider> calendarProviders;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<Account> getById(Integer id) {
        return repository.findById(id);
    }

    public List<CalendarEntry> getAccountCalendar(Account account, Date periodStart, Date periodEnd) {
        return calendarProviders.stream()
                .flatMap(provider -> provider.getAccountCalendar(account, periodStart, periodEnd).stream())
                .collect(Collectors.toList());
    }

    public List<CalendarEntry> getAccountCalendar(Account account) {
        return calendarProviders.stream()
                .flatMap(provider -> provider.getAccountCalendar(account).stream())
                .collect(Collectors.toList());
    }

    @Override
    public Account save(AccountDTO entityDto) {
        entityDto.setPassword(passwordEncoder.encode(entityDto.getPassword().trim()));
        entityDto.setEmail(entityDto.getEmail().toLowerCase());
        return super.save(entityDto);
    }

    @Valid
    public Account convertToEntity(AccountDTO dto) {
        Account account = new Account();

        account.setFirstName(dto.getFirstName());
        account.setLastName(dto.getLastName());
        account.setEmail(dto.getEmail());
        account.setPassword(dto.getPassword());
        if(dto.getRoleIds() != null) {
            account.setRoles(dto.getRoleIds().stream().map(roleId -> roleRepo.getOne(roleId)).collect(Collectors.toList()));
        }

        return account;
    }

    @Override
    public Account loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
    }

    public boolean exists(String email) {
        Account probeAccount = new Account();
        probeAccount.setEmail(email);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("email", ignoreCase());

        return repository.exists(Example.of(probeAccount, matcher));
    }

    @Valid
    public Account mergeChanges(Account account, Account delta) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> mapOriginal = mapper.convertValue(account.convertToDTO(), new TypeReference<Map<String, Object>>() {});
        Map<String, Object> mapDelta = mapper.convertValue(delta.convertToDTO(), new TypeReference<Map<String, Object>>() {});
        mapDelta.remove("id");
        mapDelta.remove("email");
        mapDelta.computeIfPresent("password", (key, value) -> passwordEncoder.encode((String)value));

        mapDelta.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .filter(entry -> !(entry.getValue() instanceof List) || !((List) entry.getValue()).isEmpty())
                .forEach(notEmptyEntry -> mapOriginal.put(notEmptyEntry.getKey(), notEmptyEntry.getValue()));

        AccountDTO accountDTO = mapper.convertValue(mapOriginal, AccountDTO.class);
        Account merged = convertToEntity(accountDTO);
        merged.setId(account.getId());
        return merged;
    }
}
