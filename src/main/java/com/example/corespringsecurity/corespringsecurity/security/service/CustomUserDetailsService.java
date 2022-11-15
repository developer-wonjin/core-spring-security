package com.example.corespringsecurity.corespringsecurity.security.service;

import com.example.corespringsecurity.corespringsecurity.domain.Account;
import com.example.corespringsecurity.corespringsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService")
public class CustomUserDetailsService  implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //DB에 있는 계정정보가 있는 ID로만 일단 조회
        Account account = userRepository.findByUsername(username);

        if(account == null){
            throw new UsernameNotFoundException("username not exist ~~");
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(account.getRole()));

        //인증객체 생성(엔티티와
        AccountContext accountContext = new AccountContext(account, roles);

        return accountContext;
    }
}
