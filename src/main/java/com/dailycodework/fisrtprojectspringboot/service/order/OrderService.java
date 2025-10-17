package com.dailycodework.fisrtprojectspringboot.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.dailycodework.fisrtprojectspringboot.dto.OrderDto;
import com.dailycodework.fisrtprojectspringboot.enums.OrderStatus;
import com.dailycodework.fisrtprojectspringboot.exceptions.ResourceNotFoundException;
import com.dailycodework.fisrtprojectspringboot.model.Cart;
import com.dailycodework.fisrtprojectspringboot.model.Order;
import com.dailycodework.fisrtprojectspringboot.model.OrderItem;
import com.dailycodework.fisrtprojectspringboot.model.Product;
import com.dailycodework.fisrtprojectspringboot.repository.OrderRepository;
import com.dailycodework.fisrtprojectspringboot.repository.ProductRepository;
import com.dailycodework.fisrtprojectspringboot.service.cart.ICartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ICartService cartService;
    private final ModelMapper modelMapper;// Utiliser ModelMapper pour mapper les objets

    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);// Récupérer le panier par ID utilisateur

        Order order = createOrder(cart);// Créer une nouvelle commande
        List<OrderItem> orderItemList = createOrderItems(order, cart);// Créer les items de commande

        order.setOrderItems(new HashSet<>(orderItemList));// Ajouter les items de commande
        order.setTotalAmount(calculateTotalAmount(orderItemList));// Calculer le montant total de la commande
        Order savedOrder = orderRepository.save(order);// Sauvegarder la commande
        cartService.clearCart(cart.getId());// Vider le panier

        return savedOrder;// Retourner la commande
    }

    // Créer une nouvelle commande avec les données fournies du panier
    private Order createOrder(Cart cart){
        Order order = new Order();// Créer une nouvelle commande
        order.setUser(cart.getUser());// Associer l'utilisateur au panier
        order.setOrderStatus(OrderStatus.PENDING);// Mettre le statut de commande en attente
        order.setOrderDate(LocalDate.now());// Mettre la date de commande actuelle
        return order;// Retourner la nouvelle commande
    }

    // Créer des items de commande avec les données fournies du panier
    private List<OrderItem> createOrderItems(Order order,Cart cart){
        return cart.getItems().stream().map(cartItem->{// Parcourir tous les items du panier et créer des items de commande
            Product product = cartItem.getProduct();// Récupérer le produit du panier
            product.setInventory(product.getInventory() - cartItem.getQuantity());// Mettre à jour l'inventaire du produit du panier
            productRepository.save(product);// Sauvegarder le produit
            return  new OrderItem(// Créer un nouvel item de commande
                order,// Associer l'ordre au panier
                product,// Associer le produit au panier
                cartItem.getQuantity(),// Quantité de l'article
                cartItem.getUnitPrice()// Prix unitaire de l'article
            );
        }).toList();// Retourner la liste des items de commande
    }

    // Calculer le montant total de la commande en additionnant le prix total de chaque item de commande 
    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        return orderItemList.stream()// Parcourir tous les items de commande et calculer le montant total de la commande
                .map(item -> item.getPrice()// Récupérer le prix de l'article du panier
                .multiply(new BigDecimal(item.getQuantity())))// Multiplier le prix par la quantité
                .reduce(BigDecimal.ZERO, BigDecimal::add);// Additionner tous les prix en partant de zéro
    }

    // Récupérer une commande par son ID et lancer une exception si elle n'existe pas
    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)// Récupérer la commande par son ID et la retourner
        .map(this::convertToDto)// Convertir la commande en OrderDto et la retourner
        .orElseThrow(()->new ResourceNotFoundException("Order not found"));// Lancer une exception si la commande n'existe pas
    }

    // Récupérer toutes les commandes d'un utilisateur et les retourner en tant que liste 
    @Override
    public List<OrderDto> getUserOders(Long userId) {// Récupérer toutes les commandes d'un utilisateur
        List<Order> orders = orderRepository.findByUserId(userId);// Récupérer toutes les commandes d'un utilisateur
        return orders.stream()// Parcourir toutes les commandes et les convertir en OrderDto et les ajouter à la liste
        .map(this::convertToDto)// Convertir chaque commande en OrderDto et les ajouter à la liste
        .toList();// Retourner la liste de OrderDto convertie en tant que liste
    }

    // Convertir une commande en OrderDto en utilisant ModelMapper
    private OrderDto convertToDto(Order order){// Convertir une commande en OrderDto
        return modelMapper.map(order, OrderDto.class);// Utiliser ModelMapper pour convertir la commande en OrderDto
    }
}
