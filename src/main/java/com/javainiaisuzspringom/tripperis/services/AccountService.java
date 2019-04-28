package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

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
}
