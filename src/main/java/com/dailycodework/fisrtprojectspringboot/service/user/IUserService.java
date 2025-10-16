package com.dailycodework.fisrtprojectspringboot.service.user;

import com.dailycodework.fisrtprojectspringboot.model.User;
import com.dailycodework.fisrtprojectspringboot.request.CreateUserRequest;
import com.dailycodework.fisrtprojectspringboot.request.UserUpdateRequest;

public interface IUserService {

    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request,Long userId);
    void deleteUser(Long userId);
}
