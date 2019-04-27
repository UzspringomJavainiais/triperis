package com.javainiaisuzspringom.tripperis.repositories;

import com.javainiaisuzspringom.tripperis.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Account, Long> {

}