package com.dailycodework.fisrtprojectspringboot.service.category;

import java.util.List;

import com.dailycodework.fisrtprojectspringboot.model.Category;

public interface ICategoryService {
      
    /**
     * Récupère une catégorie par son identifiant unique
     * @param id L'identifiant de la catégorie recherchée
     * @return La catégorie correspondante
     * @throws CategoryNotFoundException si l'ID n'existe pas
     */
    Category getCategoryById(Long id);

      /**
     * Trouve une catégorie par son nom exact (case sensitive)
     * @param name Le nom exact de la catégorie
     * @return La catégorie trouvée
     * @throws CategoryNotFoundException si le nom n'existe pas
     */
    Category getCategoryByName(String name);

     /**
     * Liste toutes les catégories disponibles
     * @return Une liste de catégories (peut être vide)
     */
    List<Category> getAllCategories();

     /**
     * Ajoute une nouvelle catégorie
     * @param category L'objet catégorie à créer
     * @return La catégorie créée (avec son ID généré)
     * @throws DuplicateCategoryException si le nom existe déjà
     */
    Category addCategory(Category category);

     /**
     * Met à jour une catégorie existante
     * @param category Les nouvelles données de la catégorie
     * @param id L'identifiant de la catégorie à modifier
     * @return La catégorie mise à jour
     * @throws CategoryNotFoundException si l'ID n'existe pas
     */
    Category updateCategory(Category category,Long id);

    /**
     * Supprime une catégorie par son identifiant
     * @param id L'ID de la catégorie à supprimer
     * @throws CategoryNotFoundException si l'ID est invalide
     * @throws CategoryInUseException si la catégorie est utilisée par des produits
     */
    void deleteCategoryById(Long id);
}
