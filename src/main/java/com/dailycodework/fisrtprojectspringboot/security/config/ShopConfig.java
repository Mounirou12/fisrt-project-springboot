package com.dailycodework.fisrtprojectspringboot.security.config;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dailycodework.fisrtprojectspringboot.security.jwt.AuthTokenFilter;
import com.dailycodework.fisrtprojectspringboot.security.jwt.JwtAuthEntryPoint;
import com.dailycodework.fisrtprojectspringboot.security.jwt.JwtUtils;
import com.dailycodework.fisrtprojectspringboot.security.user.ShopUserDetailsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration // Annotation Spring qui indique que cette classe est un bean à gérer par le conteneur Spring
@EnableMethodSecurity(prePostEnabled=true) // Annotation Spring qui active la sécurité au niveau des méthodes
public class ShopConfig {


    private final ShopUserDetailsService userDetailsService;// Injection de dépendance via le constructeur 
    private final JwtAuthEntryPoint authEntryPoint;// Injection de dépendance via le constructeur (oubli du keyword fnal)
    private final JwtUtils jwtUtils;// Injection de dépendance via le constructeur

    private static final List<String> SECURED_URLS = List.of("/api/v1/carts/**","/api/v1/cartItems/**");// Liste des URLs protégées par l'authentification  

    // Annotation Spring qui déclare que cette méthode retourne un bean à gérer par le conteneur Spring
    @Bean
    public ModelMapper modelMapper(){
        // Crée et retourne une nouvelle instance de ModelMapper
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){// Crée et retourne une nouvelle instance de BCryptPasswordEncoder
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authTokenFilter(){// Crée et retourne une nouvelle instance de AuthTokenFilter
        return new AuthTokenFilter(jwtUtils, userDetailsService);// Retourne l'instance de AuthTokenFilter configurée
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{// Crée et retourne une nouvelle instance de AuthenticationManager
        return authConfig.getAuthenticationManager();// Retourne l'AuthenticationManager configuré par AuthenticationConfiguration
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){// Crée et retourne une nouvelle instance de DaoAuthenticationProvider
        var authProvider = new DaoAuthenticationProvider();// Crée une nouvelle instance de DaoAuthenticationProvider
        authProvider.setUserDetailsService(userDetailsService);// Configure le service de gestion des utilisateurs
        authProvider.setPasswordEncoder(passwordEncoder());// Configure l'encodeur de mot de passe
        return authProvider;// Retourne l'instance de DaoAuthenticationProvider configurée
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{// Crée et retourne une nouvelle instance de SecurityFilterChain
        http.csrf(AbstractHttpConfigurer::disable)// Désactive la protection contre les attaques CSRF 
            .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))// Configure l'EntryPoint de l'authentification 
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))// Désactive la gestion de session pour l'authentification
            .authorizeHttpRequests(auth->auth.requestMatchers(SECURED_URLS.toArray(String[]::new)).authenticated()// Autorise l'accès aux URLs protégées par l'authentification 
            .anyRequest().permitAll());// Autorise l'accès à toutes les autres URLs
        http.authenticationProvider(daoAuthenticationProvider());// Configure le fournisseur d'authentification
        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);// Ajoute le filtre d'authentification JWT avant le filtre de l'authentification de base 
        return http.build();// Construit et retourne la chaîne de filtres de sécurité configurée
    }
}
