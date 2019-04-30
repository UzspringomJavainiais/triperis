package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.dto.TripDuration;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
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

@Service
public class AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

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

    public List<TripDuration> getTripDurationsInPeriod(Account account, Date periodStart, Date periodEnd) {
        Timestamp startTimestamp = Timestamp.from(periodStart.toInstant());
        Timestamp endTimestamp = Timestamp.from(periodEnd.toInstant());

        // I don't know how to force the query to return objects of wanted type, so we have to do this stuff right here
        List<Object[]> objectList = accountRepository.getTripDates(account, startTimestamp, endTimestamp);
        List<TripDuration> accountTripDurations = new ArrayList<>();
        for(Object[] obj: objectList) {
            if(obj.length != 3) {
                LOGGER.error("Returned array has have size of 3, but had {}", obj.length);
                throw new IllegalStateException("Illegal query results");
            }
            accountTripDurations.add(new TripDuration((Integer) obj[0], (Timestamp) obj[1], (Timestamp) obj[2]));
        }
        return accountTripDurations;
    }
}
