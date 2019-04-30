package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.dto.CalendarEntry;
import com.javainiaisuzspringom.tripperis.dto.TripDuration;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
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

    @Transactional(propagation = Propagation.REQUIRED)
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getById(Integer id) {
        return accountRepository.findById(id);
    }

    public List<CalendarEntry> getAccountCalendar(Account account, Date periodStart, Date periodEnd) {
        return calendarProviders.stream()
                .flatMap(provider -> provider.getAccountCalendar(account, periodStart, periodEnd).stream())
                .collect(Collectors.toList());
    }
}
