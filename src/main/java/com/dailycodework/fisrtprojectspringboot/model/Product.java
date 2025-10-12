package com.dailycodework.fisrtprojectspringboot.model;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor//Qui est supprime apres la generation du constructeur Product
@Entity

public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)

    private Long id;//correction du type Long
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="category_id")
    // Annotation Jackson qui marque le côté "enfant" dans une relation bidirectionnelle
    // Empêche la sérialisation JSON de ce champ pour éviter les boucles infinies
    @JsonBackReference
    private Category category;  

    
    @OneToMany(mappedBy = "product" , cascade = CascadeType.ALL, orphanRemoval = true )
    private List<Image> images;

    //Creation du constructeurs Product a cause des fonctions addProductet updateProduct 
    public Product(String name, String brand, BigDecimal price, int inventory, String description, Category category) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.inventory = inventory;
        this.description = description;
        this.category = category;
    }

}
