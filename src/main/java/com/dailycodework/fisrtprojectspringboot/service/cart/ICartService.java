package com.dailycodework.fisrtprojectspringboot.service.cart;

import java.math.BigDecimal;

import com.dailycodework.fisrtprojectspringboot.model.Cart;
import com.dailycodework.fisrtprojectspringboot.model.User;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
    Cart initializeNewCart(User user);// Crée un nouveau panier
    Cart getCartByUserId(Long userId);// Récupérer le panier par ID
    
}
