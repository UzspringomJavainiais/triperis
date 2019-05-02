package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.dto.CalendarEntry;
import com.javainiaisuzspringom.tripperis.dto.TripDuration;
import com.javainiaisuzspringom.tripperis.dto.entity.AccountDTO;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
import com.javainiaisuzspringom.tripperis.repositories.RoleRepository;
import com.javainiaisuzspringom.tripperis.services.calendar.AccountCalendarProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private List<AccountCalendarProvider> calendarProviders;

    @Autowired
    private RoleRepository roleRepo;

    @Transactional(propagation = Propagation.REQUIRED)
    public AccountDTO save(AccountDTO account) {
        Account entityFromDTO = getExistingOrConvert(account);
        Account savedEntity = accountRepository.save(entityFromDTO);
        return savedEntity.convertToDTO();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream().map(Account::convertToDTO).collect(Collectors.toList());
    }

    public Optional<Account> getById(Integer id) {
        return accountRepository.findById(id);
    }

    public List<CalendarEntry> getAccountCalendar(AccountDTO account, Date periodStart, Date periodEnd) {
        return calendarProviders.stream()
                .flatMap(provider -> provider.getAccountCalendar(account, periodStart, periodEnd).stream())
                .collect(Collectors.toList());
    }

    @Transactional
    public Account getExistingOrConvert(AccountDTO dto) {
        if (dto.getId() != null) {
            Optional<Account> maybeTripStep = accountRepository.findById(dto.getId());
            if (maybeTripStep.isPresent()) {
                return maybeTripStep.orElseGet(() -> {
                    // set id to null, because entity with this id does not exist
                    dto.setId(null);
                    return convertToEntity(dto);
                });
            }
        }
        return convertToEntity(dto);
    }

    private Account convertToEntity(AccountDTO dto) {
        Account account = new Account();

//        account.setId(dto.getId());
        account.setFirstName(dto.getFirstName());
        account.setLastName(dto.getLastName());
        account.setEmail(dto.getEmail());
        account.setPassword(dto.getPassword());
        account.setRoles(dto.getRoleIds().stream().map(roleId -> roleRepo.getOne(roleId)).collect(Collectors.toList()));

        return account;
    }
}
