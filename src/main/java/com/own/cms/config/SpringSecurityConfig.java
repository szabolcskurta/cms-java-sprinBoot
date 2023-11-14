package com.own.cms.config;

import com.own.cms.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig  {
 
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
    }


    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
 
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize ->{
                authorize.requestMatchers("/","/upload/**", "/login", "/logout").permitAll()
                         .requestMatchers("/admin/**").hasRole("ADMIN");

            }).formLogin( form -> form
                .loginPage("/login").permitAll()
                .loginProcessingUrl("/j_spring_security_check")
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/admin/dashboard",true)
                .usernameParameter("username")
                .passwordParameter("password")
            )
            .logout(logout->logout.logoutUrl("/logout").logoutSuccessUrl("/login"))
            .exceptionHandling( handle-> handle.accessDeniedPage("/403"));
        return http.build();
 
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }
}