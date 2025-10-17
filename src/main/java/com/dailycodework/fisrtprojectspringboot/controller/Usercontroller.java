package com.dailycodework.fisrtprojectspringboot.controller;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.fisrtprojectspringboot.dto.UserDto;
import com.dailycodework.fisrtprojectspringboot.exceptions.AlreadyExistsException;
import com.dailycodework.fisrtprojectspringboot.exceptions.ResourceNotFoundException;
import com.dailycodework.fisrtprojectspringboot.model.User;
import com.dailycodework.fisrtprojectspringboot.request.CreateUserRequest;
import com.dailycodework.fisrtprojectspringboot.response.ApiResponse;
import com.dailycodework.fisrtprojectspringboot.service.user.IUserService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dailycodework.fisrtprojectspringboot.request.UserUpdateRequest;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class Usercontroller {
    private final  IUserService userService;

    // Retourner l'utilisateur par son ID 
    @GetMapping("/{userId}/user")
    public  ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId){
        try {
            // Récupérer l'utilisateur par son ID et la retourner 
            User user = userService.getUserById(userId);
            UserDto  userDto = userService.convertUserToDto(user);// Convertir l'utilisateur en UserDto 
            return  ResponseEntity.ok(new ApiResponse("Success!", userDto));// Retourner l'utilisateur avec un message de success
        } catch (ResourceNotFoundException e) {// Gestion de l'erreur si l'utilisateur n'existe pas
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));// Retourner un code 404 et un message d'erreur
        }
    }

    // Créer un nouvel utilisateur 
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request){
        try {
            // Créer un nouvel utilisateur
            User user = userService.createUser(request);
            UserDto  userDto = userService.convertUserToDto(user);// Convertir l'utilisateur en UserDto
            return ResponseEntity.ok(new ApiResponse("Create User Success!", userDto));// Retourner l'utilisateur avec un message de success
        } catch (AlreadyExistsException e) {// Gestion de l'erreur si l'utilisateur existe deja
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));// Retourner un code 409 et un message d'erreur
        }
    }

    // Mettre à jour l'utilisateur existant 
    @PostMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest request,@PathVariable Long userId){
        try {
            // Mettre à jour l'utilisateur existant 
            User user = userService.updateUser(request, userId);
            UserDto  userDto = userService.convertUserToDto(user);// Convertir l'utilisateur en UserDto
            return ResponseEntity.ok(new ApiResponse("Update User Success!", userDto));// Retourner l'utilisateur avec un message de success
        } catch (ResourceNotFoundException e) {// Gestion de l'erreur si l'utilisateur n'existe pas
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));//
        }
    }

    // Supprimer l'utilisateur existant 
    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId){
        try {
            // Supprimer l'utilisateur existant 
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("Delete User Success!", null));// Retourner l'utilisateur avec un message de success
        } catch (ResourceNotFoundException e) {// Gestion de l'erreur si l'utilisateur n'existe pas
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));// Retourner un code 404 et un message d'erreur
        }
    }
}
