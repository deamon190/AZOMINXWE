package com.classroom.azominxwe.service;

import com.classroom.azominxwe.model.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("user".equals(username)) {
            return new CustomUserDetails(
                    "user",
                    new BCryptPasswordEncoder().encode("password"),
                    "Groupe_4",
                    "/images/users/avatar-last.png",
                    new ArrayList<>()
            );
        } else {
            throw new UsernameNotFoundException("Utilisateur non reconnu");
        }
    }
}
