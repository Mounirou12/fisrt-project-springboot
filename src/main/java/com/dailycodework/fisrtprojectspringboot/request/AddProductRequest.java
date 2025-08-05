package com.dailycodework.fisrtprojectspringboot.request;
import java.math.BigDecimal;

import com.dailycodework.fisrtprojectspringboot.model.Category;

import lombok.Data;

// Annotation Lombok qui génère automatiquement :
// - Getters/Setters
// - equals()/hashCode()
// - toString()
// - Constructeur avec arguments requis
@Data
// Déclaration d'une classe DTO (Data Transfer Object) utilisée pour recevoir 
// les données d'ajout de produit depuis une API REST
public class AddProductRequest {
    private long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
}
