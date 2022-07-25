package com.gflopes.bookstoremanager.user.service;

import com.gflopes.bookstoremanager.jwt.dto.JwtRequest;
import com.gflopes.bookstoremanager.jwt.dto.JwtResponse;
import com.gflopes.bookstoremanager.jwt.service.JwtTokenManager;
import com.gflopes.bookstoremanager.user.dto.AuthenticatedUser;
import com.gflopes.bookstoremanager.user.entity.User;
import com.gflopes.bookstoremanager.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    private UserRepository userRepository;

    private JwtTokenManager jwtTokenManager;

    public AuthenticationService(JwtTokenManager jwtTokenManager, UserRepository userRepository) {
        this.jwtTokenManager = jwtTokenManager;
        this.userRepository = userRepository;
    }

    public JwtResponse createAuthenticationToken(JwtRequest jwtRequest) {
        UserDetails userDetails = this.loadUserByUsername(jwtRequest.getUsername());
        String token = jwtTokenManager.generateToken(userDetails);

        return JwtResponse.builder().jwtToken(token).build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with username %s", username)));

        return new AuthenticatedUser(user.getUsername(), user.getPassword(), user.getRole().getDescription());
    }
}
