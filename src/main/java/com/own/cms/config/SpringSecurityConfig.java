package com.own.cms.config;

import com.own.cms.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

 
@Configuration
public class SpringSecurityConfig  {
 
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private CustomAuthenticationProvider authProvider;
 
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }
     
     
   

    protected void configure(HttpSecurity http) throws Exception {
 
        http.csrf(AbstractHttpConfigurer::disable);
 
        // The pages does not require login
        http.authorizeRequests(auth ->auth.requestMatchers("/","/upload/**", "/login", "/logout").permitAll());
 
        // /userInfo page requires login as ROLE_USER or ROLE_ADMIN.
        // If no login, it will redirect to /login page.
    
 
        // For ADMIN only.
        http.authorizeRequests( auth ->
                                   auth.requestMatchers("/admin/**").access("hasRole('ROLE_ADMIN')"));
 
        // When the user has logged in as XX.
        // But access a page that requires role YY,
        // AccessDeniedException will be thrown.
        http.authorizeRequests().and().exceptionHandling( handle-> handle.accessDeniedPage("/403"));
 
        // Config for Login Form
        http.authorizeRequests().and().formLogin( form ->
                // Submit URL of login page.
                form.loginProcessingUrl("/j_spring_security_check") // Submit URL
                .loginPage("/login")
                .defaultSuccessUrl("/admin/dashboard")
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                );
                // Config for Logout Page
        http.logout(logOut -> logOut.logoutUrl("/logout").logoutSuccessUrl("/login"));
 
    }
    @Bean
    public AuthenticationManager customAuthenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider);
        return authenticationManagerBuilder.build();
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception { 
 
        // Setting Service to find User in the database.
        // And Setting PassswordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());     
 
    }
    
    
   
}