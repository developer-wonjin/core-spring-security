package com.example.corespringsecurity.corespringsecurity.repository;

import com.example.corespringsecurity.corespringsecurity.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Account, Long> {


    Account findByUsername(String username);
}
