package com.dailycodework.fisrtprojectspringboot.request;

import java.math.BigDecimal;

import com.dailycodework.fisrtprojectspringboot.model.Category;

import lombok.Data;

// Annotation Lombok générant automatiquement :
// - Getters/Setters
// - toString()
// - equals()/hashCode()
// - Constructeur
@Data
// Classe DTO (Data Transfer Object) pour les requêtes de mise à jour de produit
// Différence avec AddProductRequest : tous les champs sont optionnels (mise à jour partielle possible)
public class ProductUpdateRequest {
    private long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
}
