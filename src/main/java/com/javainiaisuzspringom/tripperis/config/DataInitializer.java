package com.javainiaisuzspringom.tripperis.config;

import com.javainiaisuzspringom.tripperis.domain.Role;
import com.javainiaisuzspringom.tripperis.dto.entity.AccountDTO;
import com.javainiaisuzspringom.tripperis.repositories.RoleRepository;
import com.javainiaisuzspringom.tripperis.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Profile("!Production")
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AccountService accounts;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepo;

    public void run(String... args) {
        Map<String, Integer> collect = roleRepo.findAll().stream().collect(Collectors.toMap(Role::getName, Role::getId));
        if(!accounts.exists("admin@admin.admin")) {
            accounts.save(AccountDTO.builder()
                    .email("admin@admin.admin")
                    .password(encoder.encode("admin"))
                    .roleIds(new ArrayList<>(collect.values()))
                    .build());
        }
        if(!accounts.exists("user@user.user")) {
            accounts.save(AccountDTO.builder()
                    .email("user@user.user")
                    .password(encoder.encode("user"))
                    .roleIds(collect.get("USER") != null ? Arrays.asList(collect.get("ROLE_USER")) : Collections.emptyList())
                    .build());
        }
    }
}
