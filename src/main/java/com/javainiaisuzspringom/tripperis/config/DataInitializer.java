package com.javainiaisuzspringom.tripperis.config;

import com.javainiaisuzspringom.tripperis.domain.Role;
import com.javainiaisuzspringom.tripperis.dto.entity.AccountDTO;
import com.javainiaisuzspringom.tripperis.repositories.RoleRepository;
import com.javainiaisuzspringom.tripperis.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Profile("!production")
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AccountService accounts;

    @Autowired
    private RoleRepository roleRepo;

    public void run(String... args) {
        Map<String, Integer> collect = roleRepo.findAll().stream().collect(Collectors.toMap(Role::getName, Role::getId));
        if(!accounts.exists("admin@gmail.com")) {
            accounts.save(AccountDTO.builder()
                    .firstName("Adminas")
                    .lastName("Adminiauskas")
                    .email("admin@gmail.com")
                    .password("admin")
                    .roleIds(new ArrayList<>(collect.values()))
                    .build());
        }
        if(!accounts.exists("user@gmail.com")) {
            accounts.save(AccountDTO.builder()
                    .email("user@gmail.com")
                    .firstName("Vartotojas")
                    .lastName("Vartotijauskas")
                    .password("user")
                    .roleIds(Optional.ofNullable(collect.get("ROLE_USER")).map(Arrays::asList).orElseGet(ArrayList::new))
                    .build());
        }
    }
}
