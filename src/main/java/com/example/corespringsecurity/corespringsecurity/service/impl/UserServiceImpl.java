package com.example.corespringsecurity.corespringsecurity.service.impl;

import com.example.corespringsecurity.corespringsecurity.domain.Account;
import com.example.corespringsecurity.corespringsecurity.repository.UserRepository;
import com.example.corespringsecurity.corespringsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public void createUser(Account account) {
        userRepository.save(account);
    }
}
