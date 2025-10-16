package com.dailycodework.fisrtprojectspringboot.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dailycodework.fisrtprojectspringboot.dto.ImageDto;
import com.dailycodework.fisrtprojectspringboot.exceptions.ResourceNotFoundException;
import com.dailycodework.fisrtprojectspringboot.model.Image;
import com.dailycodework.fisrtprojectspringboot.response.ApiResponse;
import com.dailycodework.fisrtprojectspringboot.service.image.IImageService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PutMapping;

// Annotation Lombok qui génère automatiquement un constructeur avec un paramètre pour chaque champ final (comme 'imageService').
// Cela évite d'avoir à écrire manuellement le constructeur pour l'injection de dépendances.
@RequiredArgsConstructor
// Annotation Spring qui indique que cette classe est un contrôleur REST.
// Elle combine @Controller et @ResponseBody, donc toutes les méthodes renvoient
// directement des données JSON/XML.
@RestController
// Annotation qui définit le préfixe commun pour toutes les routes (URLs) de ce
// contrôleur.
// "${api.prefix}" est une propriété externalisée (dans application.properties)
// pour configurer dynamiquement le préfixe (ex: "/api/v1").
// Toutes les URLs des méthodes de ce contrôleur commenceront par exemple par
// "/api/v1/images".
@RequestMapping("${api.prefix}/images")
// Déclaration de la classe du contrôleur
public class ImageController {

    // Déclaration d'une dépendance vers le service de gestion des images.
    // 'private' : encapsulation, accessible seulement dans cette classe.
    // 'final' : le champ doit être initialisé dans le constructeur et ne peut pas
    // être changé ensuite. Lombok utilise cela pour générer le constructeur.
    // 'IImageService' : Le type est l'interface, pas l'implémentation concrète.
    // C'est une bonne pratique pour le découplage et les tests.
    private final IImageService imageService;
    // Grâce à @RequiredArgsConstructor, Lombok génère ce constructeur
    // automatiquement :
    // public ImageController(IImageService imageService) { this.imageService =
    // imageService; }

    // ... Ici viendraient les méthodes (@GetMapping, @PostMapping, etc.) que vous
    // avez commentées précédemment.

    // Définit un endpoint HTTP POST qui écoute les requêtes vers l'URL "/upload"
    @PostMapping("/upload")
    // La méthode retourne un objet ResponseEntity encapsulant une réponse HTTP
    // personnalisée (ApiResponse)
    public ResponseEntity<ApiResponse> saveImages(
            // Récupère une liste de fichiers envoyés avec le paramètre de requête "files"
            @RequestParam List<MultipartFile> files,
            // Récupère l'ID du produit envoyé avec le paramètre de requête "productId"
            @RequestParam Long productId) {

        // Début d'un bloc try pour gérer les erreurs potentielles
        try {
            // Appel du service métier pour enregistrer les fichiers. Il prend la liste des
            // fichiers et l'ID du produit,
            // effectue le traitement nécessaire (sauvegarde en base, stockage sur disque,
            // etc.) et retourne une liste de DTOs.
            List<ImageDto> imageDtos = imageService.saveImages(files, productId);

            // Si tout se passe bien, construit une réponse HTTP 200 (OK) avec un corps
            // contenant un objet ApiResponse
            // qui indique le succès et inclut les données des images sauvegardées.
            return ResponseEntity.ok(new ApiResponse("Upload success!", imageDtos));

            // Bloc qui capture toute exception qui pourrait être levée dans le bloc try
        } catch (Exception e) {
            // En cas d'erreur, construit une réponse HTTP 500 (Internal Server Error) avec
            // un corps contenant
            // un objet ApiResponse qui indique l'échec et inclut le message d'erreur pour
            // le débogage.
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload failed!", e.getMessage()));
        }
    }

    // Définit un endpoint HTTP GET qui écoute les requêtes vers l'URL
    // "/image/download/{imageId}"
    // Ex: /image/download/5 téléchargera l'image avec l'ID 5.
    @GetMapping("/image/download/{imageId}")
    // La méthode retourne un ResponseEntity<Resource> pour streamer des données
    // binaires (l'image)
    // @PathVariable extrait l'ID de l'image directement depuis l'URL.
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {

        // Appel du service pour récupérer l'entité Image correspondante depuis la base
        // de données en utilisant l'ID fourni.
        Image image = imageService.getImageById(imageId);

        // Convertit les données binaires de l'image (stockées dans un Blob SQL) en un
        // tableau d'octets (byte[]),
        // puis encapsule ce tableau dans un ByteArrayResource, qui est une
        // implémentation de l'interface Resource
        // de Spring, adaptée pour renvoyer des données binaires contenues en mémoire.
        ByteArrayResource resource = new ByteArrayResource(
                image.getImage().getBytes(1, (int) image.getImage().length())); // (1) position de départ, (longueur)

        // Construit et retourne la réponse HTTP :
        return ResponseEntity.ok() // Statut HTTP 200 (OK)
                // Définit le Content-Type de la réponse (ex: "image/jpeg", "image/png") pour
                // dire au navigateur
                // quel type de fichier il reçoit. Cette info est stockée en base
                // (image.getFileType()).
                .contentType(MediaType.parseMediaType(image.getFileType()))

                // Ajoute un header HTTP 'Content-Disposition'. La valeur "attachment" indique
                // au navigateur de
                // télécharger le fichier plutôt que de tenter de l'afficher directement dans la
                // page.
                // Le "filename" suggère un nom de fichier pour le téléchargement.
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")

                // Insère les données binaires de l'image (la Resource) dans le corps de la
                // réponse HTTP.
                .body(resource);
    }

    // Définit un endpoint HTTP PUT qui écoute les requêtes vers l'URL
    // "/image/{imageId}/update"
    // Ex: PUT /image/5/update mettra à jour l'image avec l'ID 5.
    @PutMapping("/image/{imageId}/update")
    // La méthode retourne un ResponseEntity contenant un objet ApiResponse
    // personnalisé
    public ResponseEntity<ApiResponse> updateImage(
            // Extrait la variable {imageId} de l'URL et la convertit en Long
            @PathVariable Long imageId,
            // Récupère le nouveau fichier image à partir du corps de la requête HTTP
            // ATTENTION: @RequestBody est généralement une erreur ici. Pour les fichiers
            // multipart,
            // on utilise @RequestParam. Ceci ne fonctionnera probablement pas comme
            // attendu.
            @RequestParam MultipartFile file) {

        try {
            // Appel du service pour récupérer l'image existante depuis la base de données
            // grâce à son ID
            Image image = imageService.getImageById(imageId);

            // Vérifie si l'image a bien été trouvée en base de données
            if (image != null) {
                // Si elle existe, on appelle le service pour la mettre à jour avec le nouveau
                // fichier
                imageService.updateImage(file, imageId);

                // Retourne une réponse HTTP 200 (OK) avec un message de succès et les données
                // de l'image mise à jour
                return ResponseEntity.ok(new ApiResponse("Update Success!", image));
            }
            // Capture une exception spécifique qui pourrait être levée si l'image n'est pas
            // trouvée (getImageById)
        } catch (ResourceNotFoundException e) {
            // Retourne une réponse HTTP 404 (Not Found) avec le message d'erreur de
            // l'exception
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), file));
        }

        // Si on arrive ici, cela signifie que l'image était null mais qu'aucune
        // exception n'a été levée (cas improbable).
        // On retourne une erreur serveur HTTP 500 (Internal Server Error).
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Update failed!", INTERNAL_SERVER_ERROR));
    }

    // Définit un endpoint HTTP DELETE qui écoute les requêtes vers l'URL
    // "/image/{imageId}/delete"
    // Ex: DELETE /image/5/delete supprimera l'image avec l'ID 5.
    @DeleteMapping("/image/{imageId}/delete")
    // La méthode retourne un ResponseEntity contenant un objet ApiResponse
    // personnalisé
    public ResponseEntity<ApiResponse> deleteImage(
            // Extrait la variable {imageId} de l'URL et la convertit en Long
            @PathVariable Long imageId) {

        try {
            // 1. Tentative de récupération: Appel du service pour trouver l'image par son
            // ID.
            // Cela permet de vérifier son existence avant de tenter la suppression.
            Image image = imageService.getImageById(imageId);

            // 2. Vérification: Si l'image existe bien en base de données...
            if (image != null) {
                // 3. Suppression: On appelle le service pour effectuer la suppression effective
                // de l'image.
                imageService.deleteImageById(imageId);

                // 4. Réponse de Succès: Retourne une réponse HTTP 200 (OK) avec un message de
                // confirmation
                // et les données de l'image qui vient d'être supprimée (pour information).
                return ResponseEntity.ok(new ApiResponse("Delete Success!", image));
            }

            // 5. Gestion d'Erreur Spécifique: Si l'image n'est pas trouvée, getImageById()
            // lève une exception.
        } catch (ResourceNotFoundException e) {
            // On retourne une réponse HTTP 404 (Not Found) avec le message d'erreur.
            // Le corps de la réponse contient le message d'erreur et 'null' pour les
            // données.
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }

        // 6. Gestion d'Erreur Générique: Ce point est atteint SI image == null mais
        // qu'aucune exception n'a été levée.
        // C'est un cas de fallback pour toute autre erreur inattendue (très
        // improbable).
        // Retourne une erreur HTTP 500 (Internal Server Error).
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Delete failed!", INTERNAL_SERVER_ERROR));
    }

}
