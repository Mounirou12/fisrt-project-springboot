package com.dailycodework.fisrtprojectspringboot.model;

import java.math.BigDecimal;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
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
public class Cart {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)

    private Long id;
    private BigDecimal totalAmount = BigDecimal.ZERO;// Montant total du panier, initialisé à zéro par défaut BigDecimal pour la précision des calculs monétaires

    //@JsonIgnore
    // Relation One-to-Many : un panier peut contenir plusieurs CartItem
    // mappedBy="cart" : la relation est gérée par l'attribut 'cart' dans CartItem
    // cascade = CascadeType.ALL : toutes les opérations (persist, merge, remove, etc.) sont cascadées
    // orphanRemoval = true : les CartItem sans référence à un panier sont automatiquement supprimés
    @OneToMany(mappedBy="cart",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<CartItem> cartItems;// Collection d'articles (CartItem) contenus dans ce panier Set évite les doublons (si la logique métier le permet)
}
