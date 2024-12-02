package com.xthreads.auth_service.configuration;


import com.xthreads.auth_service.constant.RoleConstant;
import com.xthreads.auth_service.entity.Account;
import com.xthreads.auth_service.entity.Role;
import com.xthreads.auth_service.repository.AccountRepository;
import com.xthreads.auth_service.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@Slf4j
public class ApplicationInitConfig {
    private static final String ADMIN_ACCOUNT = "admin";

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner applicationRunner(AccountRepository accountRepository, RoleRepository roleRepository){
        return args -> {
            log.info("Application is initializing...");

            addRoleIfNotExists(roleRepository, RoleConstant.ADMIN);
            addRoleIfNotExists(roleRepository, RoleConstant.USER);

            if(!accountRepository.existsByUsername(ADMIN_ACCOUNT)){
                Role role = roleRepository.findById(RoleConstant.ADMIN).orElseThrow();

                Set<Role> roles = new HashSet<>();
                roles.add(role);

                Account account = Account.builder()
                        .username(ADMIN_ACCOUNT)
                        .password(passwordEncoder.encode(ADMIN_ACCOUNT))
                        .roles(roles)
                        .build();
                try{
                    accountRepository.save(account);
                    log.info("An admin account has been created with username and password 'admin'. Please change the password now.");
                }catch (Exception e){
                    log.info("Admin account initialization failed.");
                }
            }
            log.info("The application has been initialized successfully.");
        };
    }

    private void addRoleIfNotExists(RoleRepository roleRepository, String role){
        if(!roleRepository.existsById(role)){
            Role role1 = Role.builder()
                    .name(role)
                    .description(role + " role.")
                    .build();
            roleRepository.save(role1);
        }
    }
}
