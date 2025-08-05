package com.dailycodework.fisrtprojectspringboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailycodework.fisrtprojectspringboot.model.Image;

/**
 * Repository Spring Data JPA pour l'entité Image.
 * Hérite des opérations CRUD standards de JpaRepository sans nécessiter d'implémentation manuelle.
 * 
 * @param <Image>  L'entité gérée par ce repository (de type Image)
 * @param <Long>   Le type de la clé primaire de l'entité (Long)
 * 
 * Fonctionnalités automatiquement fournies :
 * - save(), saveAll()       : Persistance d'images
 * - findById(), findAll()   : Récupération d'images
 * - delete(), deleteAll()   : Suppression d'images
 * - count()                 : Comptage d'entrées
 * - flush()                 : Synchronisation avec la DB
 */

public interface ImageRepository extends JpaRepository<Image, Long>{

}
