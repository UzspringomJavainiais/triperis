package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.Account;
import com.javainiaisuzspringom.tripperis.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public Account save(Account account) {
        return userRepository.save(account);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Account> getAllUsers() {
        return userRepository.findAll();
    }
}
