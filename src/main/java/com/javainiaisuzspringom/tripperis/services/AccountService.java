package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.dto.calendar.CalendarEntry;
import com.javainiaisuzspringom.tripperis.dto.entity.AccountDTO;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
import com.javainiaisuzspringom.tripperis.repositories.RoleRepository;
import com.javainiaisuzspringom.tripperis.services.calendar.AccountCalendarProvider;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService extends AbstractBasicEntityService<Account, AccountDTO, Integer> {

    @Getter
    @Autowired
    private AccountRepository repository;

    @Autowired
    private List<AccountCalendarProvider> calendarProviders;

    @Autowired
    private RoleRepository roleRepo;

    public Optional<Account> getById(Integer id) {
        return repository.findById(id);
    }

    public List<CalendarEntry> getAccountCalendar(AccountDTO account, Date periodStart, Date periodEnd) {
        return calendarProviders.stream()
                .flatMap(provider -> provider.getAccountCalendar(account, periodStart, periodEnd).stream())
                .collect(Collectors.toList());
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
}
