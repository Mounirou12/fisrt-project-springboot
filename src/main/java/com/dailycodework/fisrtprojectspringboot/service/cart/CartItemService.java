package com.dailycodework.fisrtprojectspringboot.service.cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.dailycodework.fisrtprojectspringboot.exceptions.ResourceNotFoundException;
import com.dailycodework.fisrtprojectspringboot.model.Cart;
import com.dailycodework.fisrtprojectspringboot.model.CartItem;
import com.dailycodework.fisrtprojectspringboot.model.Product;
import com.dailycodework.fisrtprojectspringboot.repository.CartItemRepository;
import com.dailycodework.fisrtprojectspringboot.repository.CartRepository;
import com.dailycodework.fisrtprojectspringboot.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final IProductService productService;
    private final ICartService cartService;

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        // 1. Get the cart
        // 2. Get the product
        // 3. Check if the product already in the cart
        // 4. If Yes, then increase the quantity with the requested quantity
        // 5. If No, the initiate a new CartItem entry.
        Cart cart = cartService.getCart(cartId);
        // Récupère le produit correspondant à l'ID via le service Product
        Product product = productService.getProductById(productId);
        // Recherche un CartItem existant pour ce produit dans le panier
        CartItem cartItem = cart.getItems().stream()
                // Filtre les items pour trouver celui avec le productId correspondant
                .filter(item -> item.getProduct().getId().equals(productId))
                // Prend le premier trouvé, sinon crée un nouveau CartItem vide
                .findFirst().orElse(new CartItem());
        // Vérifie si c'est un nouvel item (sans ID) ou un item existant
        if (cartItem.getId() == null) {
            // NOUVEL ITEM : Configure les relations et propriétés
            cartItem.setCart(cart); // Établit la relation avec le panier
            cartItem.setProduct(product); // Associe le produit
            cartItem.setQuantity(quantity); // Définit la quantité demandée
            cartItem.setUnitPrice(product.getPrice()); // Récupère le prix actuel du produit
        } else {
            // ITEM EXISTANT : Met à jour la quantité (ajout)
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        // Calcule le prix total (unitPrice * quantity)
        cartItem.setTotalPrice();
        // Ajoute l'item au panier (gère la relation bi-directionnelle)
        cart.addItem(cartItem);
        // Sauvegarde l'item dans la base de données
        cartItemRepository.save(cartItem);
        // Sauvegarde le panier (pour mettre à jour d'éventuels calculs totaux)
        cartRepository.save(cart);

    }

    @Override
    public void removeCartItem(Long cartId, Long productId) {
        // Récupère le panier correspondant à l'ID
        Cart cart = cartService.getCart(cartId);
        // Trouve l'item spécifique à supprimer en utilisant l'ID du panier et l'ID du produit
        CartItem itemRemove = getCartItem(cartId, productId);
        // Trouve l'item spécifique à supprimer en utilisant l'ID du panier et l'ID du produit
        cart.removeItem(itemRemove);
        // Sauvegarde les modifications du panier en base de données
        cartRepository.save(cart);
    }

    @Override
    public void updateCartItem(Long cartId, Long productId, int quantity) {
        // Récupère le panier correspondant à l'ID
        Cart cart = cartService.getCart(cartId);
        // Parcours tous les items du panier pour trouver celui à mettre à jour
        cart.getItems().stream()
        // Filtre pour ne garder que l'item avec le productId correspondant
        .filter(item -> item.getProduct().getId().equals(productId))
        // Prend le premier item trouvé (normalement il ne devrait y en avoir qu'un)
                .findFirst()
            // Si un item est trouvé, exécute les opérations de mise à jour
                .ifPresent(item -> {
                    item.setQuantity(quantity);// Met à jour la quantité avec la nouvelle valeur
                    item.setUnitPrice(item.getProduct().getPrice());// Recharge le prix unitaire depuis le produit (prix actuel)
                    item.setTotalPrice();// Recalcule le prix total (unitPrice * quantity)
                });
        // Récupère le montant total actuel du panier
        BigDecimal totalAmount = cart.getTotalAmount();
        // Réassigne le même montant total (OPÉRATION REDONDANTE)
        cart.setTotalAmount(totalAmount);
        // Sauvegarde les modifications du panier en base de données
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        // Récupère le panier complet depuis la base de données via le service
        Cart cart = cartService.getCart(cartId);
        // Convertit la collection d'items en Stream pour traitement fonctionnel
        return cart.getItems().stream()
        .filter(item -> item.getProduct().getId().equals(productId))// Filtre les items pour ne garder que celui dont le produit correspond
        .findFirst() // Récupère le premier item correspondant trouvé dans le Stream
        .orElseThrow(() -> new ResourceNotFoundException("Item not found"));// Si aucun item n'est trouvé, lance une exception métier
    }

}