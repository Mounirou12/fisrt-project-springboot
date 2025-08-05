package com.dailycodework.fisrtprojectspringboot.service.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dailycodework.fisrtprojectspringboot.dto.ImageDto;
import com.dailycodework.fisrtprojectspringboot.exceptions.ResourceNotFoundException;
import com.dailycodework.fisrtprojectspringboot.model.Image;
import com.dailycodework.fisrtprojectspringboot.model.Product;
import com.dailycodework.fisrtprojectspringboot.repository.ImageRepository;
import com.dailycodework.fisrtprojectspringboot.service.product.IProductService;

import lombok.RequiredArgsConstructor;

/**
 * Service d'implémentation pour la gestion des images
 * Responsabilités :
 * - Gestion du cycle de vie des images
 * - Coordination entre repository et service produit
 */
@Service // 1. Indique à Spring que c'est un composant métier
@RequiredArgsConstructor // 2. Génère un constructeur avec les dépendances finales
public class ImageService implements IImageService {

    // 3. Injection des dépendances
    public final ImageRepository imageRepository; // Accès à la persistence
    public final IProductService productService; // Service métier des produits

    /**
     * 4. Récupère une image par son ID
     * 
     * @param id L'identifiant technique de l'image
     * @return L'entité Image complète
     * @throws ResourceNotFoundException si l'image n'existe pas
     */
    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("image not found with " + id));
    }

    /**
     * 5. Supprime une image après vérification de son existence
     * 
     * @param id L'identifiant de l'image à supprimer
     * @throws ResourceNotFoundException si l'image n'existe pas
     */
    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
            throw new ResourceNotFoundException("image not found with " + id);
        });
    }

    /**
     * Sauvegarde plusieurs images pour un produit spécifique
     * 
     * @param files     Liste de fichiers images à uploader
     * @param productId ID du produit associé
     * @return Liste des DTOs des images sauvegardées
     * @throws RuntimeException si erreur de lecture fichier ou SQL
     */
    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        // 1. Récupération du produit parent
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImageDto = new ArrayList<>();
        // 2. Traitement de chaque fichier
        for (MultipartFile file : files) {
            try {
                // 3. Création de l'entité Image
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl = "/api/v1/images/image/dowload";
                String downloadUrl = buildDownloadUrl + image.getId();
                image.setDownloadUrl(downloadUrl);
                // 4. Sauvegarde initiale pour obtenir un ID
                Image savedImage = imageRepository.save(image);
                // 5. Construction de l'URL de téléchargement
                savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
                // 6. Mise à jour avec l'URL complète
                imageRepository.save(savedImage);

                // 7. Conversion en DTO
                ImageDto imageDto = new ImageDto();
                imageDto.setImageId(savedImage.getId());
                imageDto.setImageName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDto.add(imageDto);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
