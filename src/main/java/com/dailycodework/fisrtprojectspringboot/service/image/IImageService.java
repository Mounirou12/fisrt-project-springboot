package com.dailycodework.fisrtprojectspringboot.service.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.dailycodework.fisrtprojectspringboot.dto.ImageDto;
import com.dailycodework.fisrtprojectspringboot.exceptions.ResourceNotFoundException;
import com.dailycodework.fisrtprojectspringboot.model.Image;

/**
 * Service de gestion des images pour un système e-commerce
 * Gère le stockage, la récupération et la suppression des images liées aux produits
 */
public interface IImageService {

    /**
     * Récupère une image par son identifiant unique
     * @param id Identifiant de l'image à rechercher
     * @return L'entité Image correspondante
     * @throws ImageNotFoundException si l'image n'est pas trouvée
     */
    Image getImageById(Long id);

    /**
     * Supprime une image existante
     * @param id Identifiant de l'image à supprimer
     * @throws ResourceNotFoundException si l'image n'existe pas
     */
    void deleteImageById(Long id);

    /**
     * Sauvegarde plusieurs images pour un produit spécifique
     * @param files Liste de fichiers images à uploader
     * @param productId Identifiant du produit associé
     * @return Liste des DTOs des images sauvegardées
     * @throws InvalidImageException si un fichier n'est pas une image valide
     * @throws ResourceNotFoundException si le produit associé n'existe pas
     */
    List<ImageDto> saveImages(List<MultipartFile> file, Long productId);

    /**
     * Met à jour le contenu d'une image existante
     * @param file Nouveau fichier image
     * @param imageId Identifiant de l'image à mettre à jour
     * @throws ImageNotFoundException si l'image n'existe pas
     * @throws InvalidImageException si le nouveau fichier n'est pas valide
     * @throws StorageException en cas d'échec de mise à jour
     */
    void updateImage(MultipartFile file, Long imageId);
}
