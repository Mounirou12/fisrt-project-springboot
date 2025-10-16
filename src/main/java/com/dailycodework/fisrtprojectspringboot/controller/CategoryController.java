package com.dailycodework.fisrtprojectspringboot.controller;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.fisrtprojectspringboot.model.Category;
import com.dailycodework.fisrtprojectspringboot.response.ApiResponse;
import com.dailycodework.fisrtprojectspringboot.service.category.ICategoryService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.dailycodework.fisrtprojectspringboot.exceptions.AlreadyExistsException;

import org.springframework.web.bind.annotation.PostMapping;

import com.dailycodework.fisrtprojectspringboot.exceptions.ResourceNotFoundException;

import org.springframework.web.bind.annotation.PutMapping;

// Annotation Lombok : Génère automatiquement un constructeur avec les arguments requis (pour les champs 'final').
@RequiredArgsConstructor
// Annotation Spring : Indique que cette classe est un contrôleur REST (génère
// des réponses JSON/XML, pas des vues HTML).
@RestController
// Annotation Spring : Définit le préfixe d'URL de base pour toutes les routes
// de ce contrôleur.
// "${api.prefix}" est une variable injectée depuis le fichier
// application.properties (ex: valeur = "/api/v1").
@RequestMapping("${api.prefix}/categories")
// Déclaration de la classe du contrôleur qui gère les endpoints relatifs aux
// catégories.
public class CategoryController {

    // Déclaration d'une dépendance obligatoire (injection de dépendance) vers le
    // service de gestion des catégories.
    // 'final' indique que la valeur doit être fournie via le constructeur (généré
    // par Lombok).
    private final ICategoryService categoryservice;

    // Définit un endpoint HTTP GET qui sera accessible à l'URL
    // [api.prefix]/categories/all (ex: /api/v1/categories/all).
    @GetMapping("/all")
    // Méthode qui gère les requêtes GET sur "/all". Elle retourne une réponse HTTP
    // structurée (ApiResponse).
    public ResponseEntity<ApiResponse> getAllCategories() {
        // Début de la gestion des erreurs. Toute exception levée dans le 'try' sera
        // capturée dans le 'catch'.
        try {
            // Appel la méthode du service métier pour récupérer la liste de toutes les
            // catégories depuis la base de données.
            List<Category> categories = categoryservice.getAllCategories();
            // Si réussi, construit une réponse HTTP 200 (OK) avec un message "Found" et la
            // liste des catégories en données.
            return ResponseEntity.ok(new ApiResponse("Found", categories));
            // Capture toute exception non anticipée qui pourrait survenir pendant le traitement.
        } catch (Exception e) {
            // En cas d'erreur, construit une réponse HTTP 500 (Internal Server Error) avec un message "Error".
            // Le corps de la réponse contient aussi le code d'erreur HTTP (500) comme données, ce qui n'est pas idéal.
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", INTERNAL_SERVER_ERROR));
        }
    }

    // Définit un endpoint HTTP POST qui écoute les requêtes vers l'URL "/add"
    // L'URL complète sera donc : [api.prefix]/categories/add (ex:
    // /api/v1/categories/add)
    @PostMapping("/add")
    // La méthode retourne une réponse HTTP structurée contenant un objet
    // ApiResponse
    public ResponseEntity<ApiResponse> addCategory(
            // @RequestBody indique à Spring de convertir le corps de la requête JSON en
            // objet Java de type Category
            // Le nom du paramètre "name" est trompeur ; il s'agit en réalité de recevoir un
            // objet Category complet
            @RequestBody Category name) {

        try {
            // Appel du service métier pour ajouter la nouvelle catégorie en base de données
            // Le service renvoie la catégorie sauvegardée (généralement avec son ID généré)
            Category thecategory = categoryservice.addCategory(name);

            // Si l'opération réussit, retourne une réponse HTTP 200 (OK) avec :
            // - Un message "Success"
            // - Les données de la catégorie créée dans le corps de la réponse
            return ResponseEntity.ok(new ApiResponse("Success", thecategory));

            // Capture une exception métier spécifique levée si une catégorie avec le même nom existe déjà
        } catch (AlreadyExistsException e) {
            // Retourne une réponse HTTP 409 (CONFLICT) indiquant un conflit de ressources
            // - Le message de l'exception est renvoyé (ex: "Cette catégorie existe déjà")
            // - Les données sont null car aucune création n'a eu lieu
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    // Définit un endpoint HTTP GET qui écoute les requêtes vers l'URL
    // "/category/{id}/category"
    // {id} est un paramètre variable dans l'URL (ex:
    // /api/v1/categories/category/5/category)
    @GetMapping("/category/{id}/category")
    // La méthode retourne une réponse HTTP structurée contenant un objet
    // ApiResponse
    public ResponseEntity<ApiResponse> getCategoryById(
            // @PathVariable extrait la valeur du paramètre {id} de l'URL et la convertit en Long
            @PathVariable Long id) {
        try {
            // Appel du service métier pour rechercher une catégorie par son ID dans la base de données
            Category category = categoryservice.getCategoryById(id);

            // Si la catégorie est trouvée, retourne une réponse HTTP 200 (OK) avec :
            // - Un message "Found"
            // - Les données de la catégorie trouvée dans le corps de la réponse
            return ResponseEntity.ok(new ApiResponse("Found", category));

            // Capture une exception spécifique levée si aucune catégorie n'est trouvée avec cet ID
        } catch (ResourceNotFoundException e) {
            // Retourne une réponse HTTP 404 (NOT_FOUND) indiquant que la ressource n'existe pas
            // - Le message de l'exception est renvoyé (ex: "Category not found with id: 5")
            // - Les données sont null car aucune catégorie n'a été trouvée
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    // Définit un endpoint HTTP GET qui écoute les requêtes vers l'URL
    // "/category/{name}/category"
    // {name} est un paramètre variable dans l'URL (ex:
    // /api/v1/categories/category/Electronics/category)
    @GetMapping("/category/{name}/category")
    // La méthode retourne une réponse HTTP structurée contenant un objet
    // ApiResponse
    public ResponseEntity<ApiResponse> getCategoryByName(
            // @PathVariable extrait la valeur du paramètre {name} de l'URL et la convertit en String
            @PathVariable String name) {

        try {
            // Appel du service métier pour rechercher une catégorie par son nom dans la base de données
            Category thecategory = categoryservice.getCategoryByName(name);

            // Si la catégorie est trouvée, retourne une réponse HTTP 200 (OK) avec :
            // - Un message "Found"
            // - Les données de la catégorie trouvée dans le corps de la réponse
            return ResponseEntity.ok(new ApiResponse("Found", thecategory));

            // Capture une exception spécifique levée si aucune catégorie n'est trouvée avec ce nom
        } catch (ResourceNotFoundException e) {
            // Retourne une réponse HTTP 404 (NOT_FOUND) indiquant que la ressource n'existe pas
            // - Le message de l'exception est renvoyé (ex: "Category not found with name: Electronics")
            // - Les données sont null car aucune catégorie n'a été trouvée
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    // Définit un endpoint HTTP PUT qui écoute les requêtes vers l'URL
    // "/category/{id}/update"
    // {id} est un paramètre variable dans l'URL (ex:
    // /api/v1/categories/category/5/update)
    @PutMapping("/category/{id}/update")
    // La méthode retourne une réponse HTTP structurée contenant un objet
    // ApiResponse
    public ResponseEntity<ApiResponse> updateCategory(
            // @RequestBody extrait et convertit le corps JSON de la requête en objet Category
            @RequestBody Category category,
            // @PathVariable extrait la valeur du paramètre {id} de l'URL et la convertit en String
            @PathVariable Long id) {

        try {
            // Appel du service métier pour mettre à jour la catégorie avec les nouvelles données
            Category updateCategory = categoryservice.updateCategory(category, id);

            // Si la mise à jour réussit, retourne une réponse HTTP 200 (OK) avec :
            // - Un message "Update Success" (avec une faute de frappe : "Succes")
            // - Les données de la catégorie mise à jour
            return ResponseEntity.ok(new ApiResponse("Update Succes", updateCategory));

            // Capture une exception spécifique levée si aucune catégorie n'est trouvée avec cet ID
        } catch (ResourceNotFoundException e) {
            // Retourne une réponse HTTP 404 (NOT_FOUND) indiquant que la ressource n'existe pas
            // - Le message de l'exception est renvoyé
            // - Les données sont null car aucune mise à jour n'a été effectuée
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    // Définit un endpoint HTTP DELETE qui écoute les requêtes vers l'URL
    // "/category/{id}/delete"
    // {id} est un paramètre variable dans l'URL (ex:
    // /api/v1/categories/category/5/delete)
    @DeleteMapping("/category/{id}/delete")
    // La méthode retourne une réponse HTTP structurée contenant un objet
    // ApiResponse
    public ResponseEntity<ApiResponse> deleteCategoryById(
            // @PathVariable extrait la valeur du paramètre {id} de l'URL et la convertit en Long
            @PathVariable Long id) {

        try {
            // Appel du service métier pour supprimer la catégorie avec l'ID spécifié
            categoryservice.deleteCategoryById(id);

            // Si la suppression réussit, retourne une réponse HTTP 200 (OK) avec :
            // - Un message "Deleted" (Supprimé)
            // - Les données sont null car la ressource n'existe plus
            return ResponseEntity.ok(new ApiResponse("Deleted", null));

            // Capture une exception spécifique levée si aucune catégorie n'est trouvée avec cet ID
        } catch (ResourceNotFoundException e) {
            // Retourne une réponse HTTP 404 (NOT_FOUND) indiquant que la ressource n'existe pas
            // - Le message de l'exception est renvoyé (ex: "Category not found with id: 5")
            // - Les données sont null car aucune suppression n'a été effectuée
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}