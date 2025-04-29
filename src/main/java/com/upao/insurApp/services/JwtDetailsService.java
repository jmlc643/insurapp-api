package com.upao.insurApp.services;

import com.upao.insurApp.models.User;
import com.upao.insurApp.repos.UserRepository;
import com.upao.insurApp.utils.UserSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class JwtDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario no fue encontrado"));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+user.getRole().name());
        return new UserSecurity(user.getUserId(), user.getPassword(), Collections.singletonList(authority), user);
    }
}