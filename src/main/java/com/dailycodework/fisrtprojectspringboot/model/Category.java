package com.dailycodework.fisrtprojectspringboot.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category {

   

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    private String name;
    
    // Annotation Jackson qui ignore ce champ lors de la sérialisation/désérialisation JSON
    // Empêche ce champ d'apparaître dans la réponse JSON de l'API
    @JsonIgnore
    @OneToMany(mappedBy="category")
    private List<Product> products;

    //Creation du constructeurs Category a cause des fonctions addProductet updateProduct 
    public Category(String name) {
        this.name = name;
    }

}
