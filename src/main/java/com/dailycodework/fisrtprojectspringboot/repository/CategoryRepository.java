package com.dailycodework.fisrtprojectspringboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailycodework.fisrtprojectspringboot.model.Category;


/**
 * Repository Spring Data JPA pour l'entité Category.
 * Fournit des méthodes automatiques pour interagir avec la base de données.
 * Étend JpaRepository qui offre déjà les opérations CRUD de base.
 */
public interface CategoryRepository extends JpaRepository<Category,Long>{

    // Déclaration d'une méthode de recherche personnalisée
    // Trouve une catégorie par son nom exact (respectant la casse)
    // Retourne null si aucune catégorie n'est trouvée
    // Implémentation générée automatiquement par Spring Data JPA
    Category findByName(String name);

     /**
     * Vérifie l'existence d'une catégorie par son nom exact.
     * Méthode auto-implémentée par Spring Data JPA via la convention de nommage.
     * 
     * @param name Le nom de la catégorie à vérifier (case sensitive)
     * @return true si une catégorie avec ce nom existe, false sinon
     * 
     * Correspond à la requête SQL: 
     * SELECT COUNT(*) > 0 FROM category WHERE name = ?
     */
    boolean existsByName(String name);

}
