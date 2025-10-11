package com.dailycodework.fisrtprojectspringboot.model;

import java.math.BigDecimal;
import java.util.HashSet;
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
    private Set<CartItem> items = new HashSet<>();// Collection d'articles (CartItem) contenus dans ce panier Set évite les doublons (si la logique métier le permet)

    public void addItem(CartItem item){
        this.items.add(item); // Ajoute l'article à la collection du panier
        item.setCart(this);   // Établit la relation inverse : l'article référence ce panier
    }

    public void removeItem(CartItem item){
        this.items.remove(item); // Retire l'article de la collection du panier
        item.setCart(null); // Supprime la référence au panier dans l'article
    }

    public void updateTotalAmount(){
        this.totalAmount = items.stream().// Convertit la collection en Stream pour traitement
            map(item->{                            // Transforme chaque CartItem en BigDecimal
                BigDecimal unitPrice = item.getUnitPrice();
                if (unitPrice == null) {            // Garde-fou contre les prix nuls
                    return BigDecimal.ZERO;
             }
                return unitPrice.multiply(          // Calcule : prix unitaire × quantité
                BigDecimal.valueOf(item.getQuantity())
            );
        })
            .reduce(BigDecimal.ZERO, BigDecimal::add);  // Somme tous les totaux partiel
    }
}
