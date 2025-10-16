package com.dailycodework.fisrtprojectspringboot.service.cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.dailycodework.fisrtprojectspringboot.model.Cart;
import com.dailycodework.fisrtprojectspringboot.repository.CartItemRepository;
import com.dailycodework.fisrtprojectspringboot.repository.CartRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    //private final AtomicLong cartIdGenerator = new AtomicLong();// Compteur atomique pour les IDs de panier

    @Override
    public Cart getCart(Long id) {
        // Récupère le panier par ID ou lance une exception si non trouvé
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new RuntimeException("Cart not found"));
        // Récupère le montant total actuel du panier
        BigDecimal totalAmount = cart.getTotalAmount();
        // Réassigne le même montant total (opération redondante)
        cart.setTotalAmount(totalAmount);
        // Sauvegarde le panier (potentiellement inutile) et le retourne
        return cartRepository.save(cart);
    }

    @Transactional// Opération transactionnelle pour garantir l'intégrité des données
    @Override
    public void clearCart(Long id) {
        // Récupère le panier
        Cart cart = getCart(id);
        // Supprime tous les CartItem associés au panier via le repository
        cartItemRepository.deleteAllByCartId(id);
        // Vide la collection en mémoire (redondant après deleteAllByCartId)
        cart.getItems().clear();
        // Supprime le panier lui-même
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        // Récupère le panier
        Cart cart = getCart(id);
        // Retourne le totalAmount stocké (peut être obsolète)
        return cart.getTotalAmount();
        // Alternative commentée qui recalcule le total à la volée
        // return cart.getItems().stream().map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO,
        // BigDecimal::add);
    }

    @Override
    public Long initializeNewCart(){
        // Crée une nouvelle instance de l'entité Cart
        Cart newCart = new Cart();
        // Génère un nouvel ID unique pour le panier en incrémentant un compteur atomique
        //Long newCartId = cartIdGenerator.incrementAndGet();
        // Associe l'ID généré au nouveau panier
        //newCart.setId(newCartId);
        // Sauvegarde le panier en base de données et retourne son ID
        newCart.setTotalAmount(BigDecimal.ZERO);
        return cartRepository.save(newCart).getId();
    }

    @Override// Récupérer le panier par ID
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

}
