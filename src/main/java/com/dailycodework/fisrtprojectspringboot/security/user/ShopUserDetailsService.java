package com.dailycodework.fisrtprojectspringboot.security.user;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dailycodework.fisrtprojectspringboot.model.User;
import com.dailycodework.fisrtprojectspringboot.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShopUserDetailsService implements  UserDetailsService {

    private final   UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = Optional.ofNullable(userRepository.findByEmail(email))// Récupérer l'utilisateur par son email 
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));// Lancer une exception si l'utilisateur n'existe pas
        return  ShopUserDetails.buildUserDetails(user);// Récupérer l'utilisateur par son email
    }

}
