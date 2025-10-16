package com.dailycodework.fisrtprojectspringboot.service.user;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dailycodework.fisrtprojectspringboot.exceptions.AlreadyExistsException;
import com.dailycodework.fisrtprojectspringboot.exceptions.ResourceNotFoundException;
import com.dailycodework.fisrtprojectspringboot.model.User;
import com.dailycodework.fisrtprojectspringboot.repository.UserRepository;
import com.dailycodework.fisrtprojectspringboot.request.CreateUserRequest;
import com.dailycodework.fisrtprojectspringboot.request.UserUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)// Récupérer l'utilisateur par son ID
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));// Lancer une exception si l'utilisateur n'existe pas
    }

    // Créer un nouvel utilisateur
    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)// Encapsule la requête dans un Optional 
        .filter(user -> !userRepository.existsByEmail(request.getEmail()))// Filtre l'utilisateur par son email si il n'existe pas  
        .map(req ->{// Si l'utilisateur n'existe pas, créer un nouvel utilisateur
            User user = new User();// Créer un nouvel utilisateur
            user.setFirstName(request.getFirstName());// Mettre à jour le premier nom
            user.setLastName(request.getLastName());// Mettre à jour le dernier nom
            user.setEmail(request.getEmail());// Mettre à jour l'email
            user.setPassword(request.getPassword());// Mettre à jour le mot de passe
            return user;// Retourner l'utilisateur créé
        }).orElseThrow(()-> new AlreadyExistsException("OOps! " + request.getEmail() + " already exists!"));// Lancer une exception si l'utilisateur existe deja
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return userRepository.findById(userId)// Récupérer l'utilisateur par son ID
            .map(existingUser -> {// Mettre à jour l'utilisateur existant
            existingUser.setFirstName(request.getFirstName());// Mettre à jour le premier nom
            existingUser.setLastName(request.getLastName());// Mettre à jour le dernier nom
            return userRepository.save(existingUser);// Sauvegarder les modifications de l'utilisateur existant 
        }).orElseThrow(() -> new ResourceNotFoundException("User not found"));// Lancer une exception si l'utilisateur n'existe pas 
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)// Récupérer l'utilisateur par son ID
        .ifPresentOrElse(userRepository::delete, () -> {// Si l'utilisateur est trouvé, supprimer l'utilisateur
            // Sinon, lancer une exception
            throw new ResourceNotFoundException("User not found");
        });
    }

}
