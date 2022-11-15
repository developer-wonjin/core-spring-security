package com.example.corespringsecurity.corespringsecurity.security.provider;

import com.example.corespringsecurity.corespringsecurity.security.common.FormWebAuthenticationDetails;
import com.example.corespringsecurity.corespringsecurity.security.service.AccountContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired //CustomUserDetailsService
    private UserDetailsService userDetailsService;//

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //파라미터 authentication에는 사용자가 폼에 작성한
        //id, pw가 있다
        String username = authentication.getName();
        String password = (String)authentication.getCredentials();

        //UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        AccountContext accountContext = (AccountContext)userDetailsService.loadUserByUsername(username);
        String passwordFromDB = accountContext.getAccount().getPassword();
        Collection<GrantedAuthority> authorities = accountContext.getAuthorities();
        
        //password검증
        boolean passwordNotMatch = passwordEncoder.matches(password, passwordFromDB) == false;
        if(passwordNotMatch){
            throw new BadCredentialsException("BadCredentialsException");
        }

        //secretKey검증
        FormWebAuthenticationDetails details = (FormWebAuthenticationDetails) authentication.getDetails();
        String secretKey = details.getSecretKey();
        if(secretKey == null || !"secret".equals(secretKey)){
            throw new InsufficientAuthenticationException("InsufficientAuthenticationException");
        }

        //정책에 따라 다른 검증로직 추가가능

        //현 Provider를 호출한 [A]Manager에게 아래 인증객체를 리턴함
        return new UsernamePasswordAuthenticationToken(accountContext.getAccount(), null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //Provider의 동작 조건설정
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
