package com.dailycodework.fisrtprojectspringboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailycodework.fisrtprojectspringboot.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    //supprimer tous les items liés à un certain cartId, sans supprimer le cart
    void deleteAllByCartId(Long id);//

}
