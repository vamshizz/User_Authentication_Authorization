package com.example.RegisterandLogin.Service;

import com.example.RegisterandLogin.Model.User;
import com.example.RegisterandLogin.Repository.AuthenticationRepository;
import com.example.RegisterandLogin.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {



    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user=userRepository.findByEmail(username);
        return user.map(auth ->
                org.springframework.security.core.userdetails.User.builder()
                         .username(auth.getEmail())
                        .password(auth.getPassword())
                        // Ensure password is encoded
                         .build()
        ).orElseThrow(() -> new UsernameNotFoundException("Invalid Username"));    }
}
