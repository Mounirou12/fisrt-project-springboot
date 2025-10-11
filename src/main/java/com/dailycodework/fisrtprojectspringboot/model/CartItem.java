package com.dailycodework.fisrtprojectspringboot.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private int quantity;// Quantité de produits dans cette ligne de commande/panier
    private BigDecimal unitPrice;// Prix unitaire du produit, utilisé BigDecimal pour la précision financière
    private BigDecimal totalPrice;    // Prix total calculé (unitPrice * quantity), également avec BigDecimal

    @ManyToOne// Relation Many-to-One : plusieurs instances de cette entité peuvent être associées à un seul Product
    @JoinColumn(name = "product_id")// Spécifie la colonne de jointure dans la table pour la relation avec Product
    //@JsonBackReference// Annotation Jackson (commentée) pour éviter la sérialisation circulaire en  JSON
    private Product product;// Référence à l'entité Product associée à cette ligne

    @ManyToOne(cascade = CascadeType.ALL)// Relation Many-to-One avec cascade ALL : les opérations sur Cart affecteront cette entité

    @JoinColumn(name = "cart_id")// Spécifie la colonne de jointure pour la relation avec Cart
    // @JsonBackReference // Annotation Jackson commentée pour la gestion des références circulaires
    private Cart cart; // Référence au panier (Cart) auquel cette ligne appartient

    public void SetTotalPrice() {
        // Méthode pour calculer automatiquement le prix total
        // Multiplie le prix unitaire par la quantité pour obtenir le total
        this.totalPrice = this.unitPrice.multiply(new BigDecimal(quantity));
    }

}
