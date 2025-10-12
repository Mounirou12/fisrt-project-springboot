package com.dailycodework.fisrtprojectspringboot.service.cart;

import com.dailycodework.fisrtprojectspringboot.model.CartItem;

public interface ICartItemService {
    void addCartItem(Long cartId, Long productId, int quantity);
    void removeCartItem(Long cartId, Long productId);
    void updateCartItem(Long cartId, Long productId, int quantity);
    CartItem getCartItem(Long cartId, Long productId);
}
