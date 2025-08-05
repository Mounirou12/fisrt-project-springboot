package com.dailycodework.fisrtprojectspringboot.service.product;

import java.util.List;

import com.dailycodework.fisrtprojectspringboot.exceptions.ProductNotFoundException;
import com.dailycodework.fisrtprojectspringboot.model.Product;
import com.dailycodework.fisrtprojectspringboot.request.AddProductRequest;
import com.dailycodework.fisrtprojectspringboot.request.ProductUpdateRequest;

public interface IProductService {
     /**
     * Ajoute un nouveau produit à partir des données de la requête.
     * @param request Objet contenant les données nécessaires pour créer un produit
     * @return Le produit créé et persisté
     */
    Product addProduct(AddProductRequest request);

     /**
     * Récupère un produit par son identifiant unique.
     * @param id L'identifiant du produit à rechercher
     * @return Le produit correspondant à l'ID
     * @throws ProductNotFoundException si aucun produit n'est trouvé avec cet ID
     */
    Product getProductById(long id);

    /**
     * Supprime un produit existant par son identifiant.
     * @param id L'identifiant du produit à supprimer
     * @throws ProductNotFoundException si aucun produit n'est trouvé avec cet ID
     */
    void deleteProductById(long id);

     /**
     * Met à jour les informations d'un produit existant.
     * @param product Les nouvelles données du produit
     * @param productId L'identifiant du produit à mettre à jour
     * @return Le produit mis à jour
     * @throws ProductNotFoundException si aucun produit n'est trouvé avec cet ID
     */
    Product updateProduct(ProductUpdateRequest product, long productId);

     /**
     * Récupère la liste complète de tous les produits disponibles.
     * @return Une liste de tous les produits (peut être vide)
     */
    List<Product> getAllProducts();

     /**
     * Filtre les produits par catégorie.
     * @param category La catégorie de produits à rechercher
     * @return Liste des produits de la catégorie spécifiée (peut être vide)
     */
    List<Product> getProductsByCategory(String category);

     /**
     * Filtre les produits par marque.
     * @param brand La marque de produits à rechercher
     * @return Liste des produits de la marque spécifiée (peut être vide)
     */
    List<Product> getProductsByBrand(String brand);

     /**
     * Filtre les produits par combinaison de catégorie et marque.
     * @param category La catégorie de produits à rechercher
     * @param brand La marque de produits à rechercher
     * @return Liste des produits correspondants aux critères (peut être vide)
     */
    List<Product> getProductsByCategoryAndBrand(String category, String brand);

     /**
     * Recherche des produits par nom (recherche textuelle).
     * @param name Le nom ou partie du nom à rechercher
     * @return Liste des produits dont le nom correspond (peut être vide)
     */
    List<Product> getProductsByName(String name);

    /**
     * Filtre les produits par combinaison de marque et nom.
     * @param brand La marque de produits à rechercher
     * @param name Le nom ou partie du nom à rechercher
     * @return Liste des produits correspondants aux critères (peut être vide)
     */
    List<Product> getProductsByBrandAndName(String brand, String name);

      /**
     * Compte le nombre de produits correspondant à une marque et un nom donnés.
     * @param brand La marque de produits à compter
     * @param name Le nom ou partie du nom à rechercher
     * @return Le nombre total de produits correspondants
     */
    long countProductsByBrandAndName(String brand, String name);
}
