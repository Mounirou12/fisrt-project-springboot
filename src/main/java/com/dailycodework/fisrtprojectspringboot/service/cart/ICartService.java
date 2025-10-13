package com.dailycodework.fisrtprojectspringboot.service.cart;

import java.math.BigDecimal;

import com.dailycodework.fisrtprojectspringboot.model.Cart;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
    Long initializeNewCart();
    
}
