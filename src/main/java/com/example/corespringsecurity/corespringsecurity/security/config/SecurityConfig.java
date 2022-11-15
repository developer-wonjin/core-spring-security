package com.example.corespringsecurity.corespringsecurity.security.config;

import com.example.corespringsecurity.corespringsecurity.security.provider.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.cert.Extension;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationDetailsSource authenticationDetailsSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*
        * 방법3
        * CustomAuthenticationProvider를 이용하게끔 만든다.
        * */
        auth.authenticationProvider(authenticationProvider());

        /*
        * 방법2)
        * DB에서 가져오기
        * DaoAuthenticationProvider가 CustomUserDetailsService를 이용하게끔 만든다.
        * */
//        auth.userDetailsService(userDetailsService);

        /*
        * 방법1)
        * InMemory방식의 계정정보
        * */
//        String password = passwordEncoder().encode("1111");
//        auth.inMemoryAuthentication().withUser("user").password(password).roles("USER");
//        auth.inMemoryAuthentication().withUser("manager").password(password).roles("MANAGER", "USER");
//        auth.inMemoryAuthentication().withUser("admin").password(password).roles("ADMIN", "MANAGER", "USER");
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //정적 리소스가 보안설정검사 없이
        web
                .ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
//                .antMatchers("/favicon.ico", "/resources/**", "/error")
        ;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/users").permitAll()
                .antMatchers("/mypage").hasRole("USER")
                .antMatchers("/message").hasRole("MANAGER")
                .antMatchers("/config").hasRole("ADMIN")

                .anyRequest().authenticated()
        .and()
                .formLogin()
                .loginPage("/login")               //로그인 페이지의 GET 요청 엔드포인트
                .loginProcessingUrl("/login_proc") //로그인 시도의 POST요청 엔드포인트
                .defaultSuccessUrl("/")            //로그인 성공후 루트페이지로 이동
                .authenticationDetailsSource(authenticationDetailsSource)
                .permitAll()
        ;


    }
}
