package com.javainiaisuzspringom.tripperis.services;

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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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

    public List<CalendarEntry> getAccountCalendar(AccountDTO account, Date periodStart, Date periodEnd) {
        return calendarProviders.stream()
                .flatMap(provider -> provider.getAccountCalendar(account, periodStart, periodEnd).stream())
                .collect(Collectors.toList());
    }

    @Override
    public AccountDTO save(AccountDTO entityDto) {
        // TODO Check if username exists and if password not blank
        entityDto.setPassword(passwordEncoder.encode(entityDto.getPassword().trim()));
        entityDto.setEmail(entityDto.getEmail().toLowerCase());
        return super.save(entityDto);
    }

    protected Account convertToEntity(AccountDTO dto) {
        Account account = new Account();

        account.setFirstName(dto.getFirstName());
        account.setLastName(dto.getLastName());
        account.setEmail(dto.getEmail());
        account.setPassword(dto.getPassword());
        account.setRoles(dto.getRoleIds().stream().map(roleId -> roleRepo.getOne(roleId)).collect(Collectors.toList()));

        return account;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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
}
