package com.dailycodework.fisrtprojectspringboot.service.user;

import com.dailycodework.fisrtprojectspringboot.dto.UserDto;
import com.dailycodework.fisrtprojectspringboot.model.User;
import com.dailycodework.fisrtprojectspringboot.request.CreateUserRequest;
import com.dailycodework.fisrtprojectspringboot.request.UserUpdateRequest;

public interface IUserService {

    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request,Long userId);
    void deleteUser(Long userId);
    UserDto convertUserToDto(User user);// Convertir l'utilisateur en UserDto
    User getAuthenticatedUser();// Récupérer l'utilisateur connecté par son email
}
