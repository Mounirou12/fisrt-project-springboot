package com.dailycodework.fisrtprojectspringboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailycodework.fisrtprojectspringboot.model.Product;


/**
 * Repository Spring Data JPA pour l'entité Product.
 * Fournit des méthodes automatiques pour interagir avec la base de données.
 * Étend JpaRepository qui offre déjà les opérations CRUD de base.
 */
public interface ProductRepository extends JpaRepository<Product,Long> {

    /**
     * Recherche les produits appartenant à une catégorie spécifique.
     * @param category Le nom de la catégorie
     * @return Liste des produits de cette catégorie (peut être vide)
     */
    List<Product> findByCategoryName(String category);

    /**
     * Recherche les produits d'une marque spécifique.
     * @param brand Le nom de la marque
     * @return Liste des produits de cette marque (peut être vide)
     */
    List<Product> findByBrand(String brand);

    /**
     * Recherche les produits par combinaison catégorie ET marque.
     * @param category Le nom de la catégorie
     * @param brand Le nom de la marque
     * @return Liste des produits correspondants (peut être vide)
     */
    List<Product> findByCategoryNameAndBrand(String category, String brand);

    /**
     * Recherche des produits par leur nom exact.
     * @param name Le nom complet du produit
     * @return Liste des produits avec ce nom exact (peut être vide)
     */
    List<Product> findByName(String name);

       /**
     * Recherche des produits par combinaison marque ET nom.
     * @param brand Le nom de la marque
     * @param name Le nom du produit
     * @return Liste des produits correspondants (peut être vide)
     */
    List<Product> findByBrandAndName(String brand, String name);

     /**
     * Compte le nombre de produits pour une combinaison marque/nom.
     * @param brand Le nom de la marque
     * @param name Le nom du produit
     * @return Le nombre total de produits correspondants
     */
    long countByBrandAndName(String brand, String name);

     boolean existsByNameAndBrand(String name, String brand);

}
