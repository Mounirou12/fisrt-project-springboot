package com.dailycodework.fisrtprojectspringboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailycodework.fisrtprojectspringboot.model.Order;

public interface OrderRepository extends  JpaRepository<Order, Long>{

    List<Order> findByUserId(Long userId);//This method is used to retrieve a list of orders associated with a specific user

}
