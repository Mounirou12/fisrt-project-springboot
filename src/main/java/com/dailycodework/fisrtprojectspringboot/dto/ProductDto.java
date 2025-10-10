package com.dailycodework.fisrtprojectspringboot.dto;

import java.math.BigDecimal;
import java.util.List;

import com.dailycodework.fisrtprojectspringboot.model.Category;

import lombok.Data;

// Annotation Lombok qui génère automatiquement les getters, setters, toString, equals, hashCode
@Data
public class ProductDto {

    // DTO (Data Transfer Object) pour transférer les données Product via l'API
    // Contient uniquement les champs nécessaires pour l'exposition externe
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category; // Objet Category inclus dans la réponse
    private List<ImageDto> images;      // Liste des DTOs d'images associées au produit
}
