package com.dailycodework.fisrtprojectspringboot.service.user;

import org.springframework.stereotype.Service;

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

    @Override
    public User createUser(CreateUserRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createUser'");
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
