package com.dailycodework.fisrtprojectspringboot.service.order;

import java.util.List;

import com.dailycodework.fisrtprojectspringboot.model.Order;

public interface IOrderService {
    Order placeOrder(Long userId);

    Order getOrder(Long orderId);

    List<Order> getUserOders(Long userId);// Récupérer les commandes d'un utilisateur

}
