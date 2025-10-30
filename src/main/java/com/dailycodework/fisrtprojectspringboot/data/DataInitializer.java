package com.dailycodework.fisrtprojectspringboot.data;

import java.util.Set;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dailycodework.fisrtprojectspringboot.model.Role;
import com.dailycodework.fisrtprojectspringboot.model.User;
import com.dailycodework.fisrtprojectspringboot.repository.RoleRepository;
import com.dailycodework.fisrtprojectspringboot.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional// Ensure that the operations are executed within a transaction
@Component// Mark the class as a Spring component 
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;// Inject the UserRepository
    private final RoleRepository roleRepository;// Inject the RoleRepository
    private final PasswordEncoder passwordEncoder;// Inject the PasswordEncoder

    // Override the onApplicationEvent method from ApplicationListener interface 
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultsRoles = Set.of("ROLE_ADMIN","ROLE_USER");// Define default roles for the users
        createDefaultUserIfNotExists();// Call the method to create default users
        createDefaultRoleIfNotExists(defaultsRoles);// Call the method to create default roles
        createDefaultAdminIfNotExists();// Call the method to create default admin users
    }

    // Method to create default users 
    private void createDefaultUserIfNotExists() {
    Role userRole = roleRepository.findByName("ROLE_USER").get();// Get the ROLE_USER from the database
        for (int i = 1; i <= 5; i++) {
            String defaultEmail = "user" + i + "@gmail.com";
            if (userRepository.existsByEmail(defaultEmail)) {// Check if the user already exists
                continue;// Skip creating the user
            }
            User user = new User();
            user.setFirstName("The User");
            user.setLastName("User" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));// Encode the default password using the PasswordEncoder
            user.setRoles(Set.of(userRole));// Assign the ROLE_USER to the default user
            userRepository.save(user);
            System.out.println("Default vet user " + i + " created successfully.");
        }
    }

    private void createDefaultAdminIfNotExists() {// Method to create default admin users
    Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();// Get the ROLE_ADMIN from the database
        for (int i = 1; i <= 2; i++) {// Loop to create 2 default admin users
            String defaultEmail = "admin" + i + "@gmail.com";// Define the default email for the admin user
            if (userRepository.existsByEmail(defaultEmail)) {// Check if the user already exists
                continue;// Skip creating the user
            }
            User user = new User();
            user.setFirstName("The Admin");// Set the first name of the admin user
            user.setLastName("Admin" + i);// Set the last name of the admin user
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));// Encode the default password using the PasswordEncoder
            user.setRoles(Set.of(adminRole));
            userRepository.save(user);
            System.out.println("Default vet admin user " + i + " created successfully.");// Log the creation of the admin user
        }
    }


    private void createDefaultRoleIfNotExists(Set<String> roles){// Method to create default roles
        roles.stream()// Iterate through the set of roles
                .filter(role->roleRepository.findByName(role).isEmpty())// Filter roles that do not exist in the database
                .map(Role::new).forEach(roleRepository::save);// Create and save new roles in the database
    }

}
