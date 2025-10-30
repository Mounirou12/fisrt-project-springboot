package com.dailycodework.fisrtprojectspringboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailycodework.fisrtprojectspringboot.model.Role;

public interface RoleRepository  extends JpaRepository<Role,Long>{

    Optional<Role> findByName(String role);// Récupérer le rôle par son nom 

}
