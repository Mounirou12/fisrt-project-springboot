package com.dailycodework.fisrtprojectspringboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailycodework.fisrtprojectspringboot.model.User;

public interface UserRepository extends JpaRepository<User,Long>{

    boolean existsByEmail(String email);// Vérifier l'existence d'un utilisateur par son adresse email

    User findByEmail(String email);// Récupérer un utilisateur par son adresse email

}
