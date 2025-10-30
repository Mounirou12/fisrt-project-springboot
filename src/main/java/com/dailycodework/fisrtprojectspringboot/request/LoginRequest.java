package com.dailycodework.fisrtprojectspringboot.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank//validation de l'email
    private String email;
    @NotBlank//validation du mot de passe
    private String password;
}
