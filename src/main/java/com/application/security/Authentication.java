package com.application.security;

import com.application.data.entity.User;
import com.application.data.repository.UserRepository;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class Authentication {

    private final UserRepository userRepository;
    private final AuthenticationContext authenticationContext;

    public Authentication(AuthenticationContext authenticationContext, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.authenticationContext = authenticationContext;
    }

    @Transactional
    public Optional<User> getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class)
                .map(userDetails -> userRepository.findByUsername(userDetails.getUsername()));
    }

    public Boolean isLoggedIn() {
        return this.getAuthenticatedUser().isPresent();
    }

    public void logout() {
        authenticationContext.logout();
    }

}
