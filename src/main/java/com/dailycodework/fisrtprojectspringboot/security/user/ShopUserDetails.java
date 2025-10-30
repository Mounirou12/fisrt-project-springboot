package com.dailycodework.fisrtprojectspringboot.security.user;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dailycodework.fisrtprojectspringboot.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShopUserDetails implements UserDetails{

    private Long Id;
    private String email;
    private String password;

    private Collection<GrantedAuthority> authorities; 

    public static ShopUserDetails buildUserDetails(User user){
        List<GrantedAuthority> authorities = user.getRoles()// Récupérer la collection d'autorisations
            .stream()// Convertir la collection en Stream
            .map(role -> new SimpleGrantedAuthority(role.getName()))// Convertir chaque role en SimpleGrantedAuthority
            .collect(Collectors.toList());// Collecter les autorisations dans une liste
            return  new ShopUserDetails(// Créer une nouvelle instance de ShopUserDeails
                user.getId(),// Récupérer l'id
                user.getEmail(),// Récupérer l'email
                user.getPassword(),// Récupérer le mot de passe
                authorities// Récupérer la collection d'autorisations
            );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;// Récupérer la collection d'autorisations
    }

    @Override
    public String getPassword() {
        return password;// Récupérer le mot de passe
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return email;// Récupérer l'email
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return UserDetails.super.isEnabled();
    }

}
