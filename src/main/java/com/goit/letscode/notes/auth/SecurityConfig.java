package com.goit.letscode.notes.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        // TODO: adjust the httpSecurity properly
        httpSecurity.authorizeHttpRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                //.loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();
        return httpSecurity.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.jdbcAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT login, password, 1 as enabled "
                        + "FROM users "
                        + "WHERE login = ?")
                .authoritiesByUsernameQuery("SELECT login, 'USER' as authority "
                        + "FROM users "
                        + "WHERE login = ?");
    }
}
