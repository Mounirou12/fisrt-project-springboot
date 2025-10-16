package com.dailycodework.fisrtprojectspringboot.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

    @OneToMany(mappedBy="cart",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<CartItem> items = new HashSet<>();// Collection d'articles (CartItem) contenus dans ce panier Set évite les doublons (si la logique métier le permet)

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    public void addItem(CartItem item){
        this.items.add(item); // Ajoute l'article à la collection du panier
        item.setCart(this);   // Établit la relation inverse : l'article référence ce panier
        updateTotalAmount();  // Met à jour le montant total(J'ai oublie de le mettre )
    }

    public void removeItem(CartItem item){
        this.items.remove(item); // Retire l'article de la collection du panier
        item.setCart(null); // Supprime la référence au panier dans l'article
        updateTotalAmount(); // Met à jour le montant total (J'ai oublie de le mettre )
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
