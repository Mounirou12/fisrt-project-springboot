package com.dailycodework.fisrtprojectspringboot.service.order;

import java.util.List;

import com.dailycodework.fisrtprojectspringboot.dto.OrderDto;
import com.dailycodework.fisrtprojectspringboot.model.Order;

public interface IOrderService {
    Order placeOrder(Long userId);

    OrderDto getOrder(Long orderId);// Récupérer une commande par son ID et lancer une exception si elle n'existe pas

    List<OrderDto> getUserOders(Long userId);// Récupérer les commandes d'un utilisateur et les retourner en tant que liste  

    OrderDto convertToDto(Order order);// Convertir une commande en OrderDto

}
