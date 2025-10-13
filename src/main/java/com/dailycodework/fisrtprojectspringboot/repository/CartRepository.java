package com.dailycodework.fisrtprojectspringboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailycodework.fisrtprojectspringboot.model.Cart;

public interface CartRepository  extends JpaRepository<Cart,Long> {


}
