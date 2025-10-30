package com.dailycodework.fisrtprojectspringboot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.fisrtprojectspringboot.request.LoginRequest;
import com.dailycodework.fisrtprojectspringboot.response.ApiResponse;
import com.dailycodework.fisrtprojectspringboot.response.JwtResponse;
import com.dailycodework.fisrtprojectspringboot.security.jwt.JwtUtils;
import com.dailycodework.fisrtprojectspringboot.security.user.ShopUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;// Injection de dépendance de AuthenticationManager via le constructeur
    private final JwtUtils jwtUtils;// Injection de dépendance de JwtUtils via le constructeur

    @PostMapping("/login")
    private ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        // Authentifier l'utilisateur en utilisant l'AuthenticationManager
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Générer un token JWT pour l'utilisateur authentifié
            String jwt = jwtUtils.generateTokenForUser(authentication);

            // Récupérer les détails de l'utilisateur à partir de l'objet Authentication
            ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();

            // Créer une réponse contenant le token JWT et les informations utilisateur
            JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);

            // Retourner la réponse HTTP avec le token JWT
            return ResponseEntity.ok(new ApiResponse("Login successful", jwtResponse));
        } catch (AuthenticationException e) {
            // Gestion des erreurs d'authentification
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }

    }
}
