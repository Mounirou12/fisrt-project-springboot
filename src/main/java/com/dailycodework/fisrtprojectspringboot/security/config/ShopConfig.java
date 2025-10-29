package com.dailycodework.fisrtprojectspringboot.security.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Annotation Spring qui indique que cette classe contient des configurations
@Configuration
public class ShopConfig {

    // Annotation Spring qui déclare que cette méthode retourne un bean à gérer par le conteneur Spring
    @Bean
    public ModelMapper modelMapper(){
        // Crée et retourne une nouvelle instance de ModelMapper
        return new ModelMapper();
    }

}
