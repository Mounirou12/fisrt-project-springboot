package com.dailycodework.fisrtprojectspringboot.dto;

import lombok.Data;

/**
 * DTO (Data Transfer Object) pour la représentation sécurisée des images.
 * Utilisé pour exposer les données d'image aux clients API sans révéler la structure interne.
 */
@Data // Génère automatiquement getters/setters/toString/equals
public class ImageDto {

    /**
     * Identifiant unique de l'image (ex: 123)
     */
    private  Long id;

    /**
     * Nom original du fichier image (ex: "produit-photo.jpg")
     */

    private String fileName;

     /**
     * URL publique de téléchargement (ex: "https://storage.com/images/abc123.jpg")
     * Format typique pour les stockages cloud (S3, Firebase, etc.)
     */
    private String downloadUrl;
}
