package com.dailycodework.fisrtprojectspringboot.controller;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.fisrtprojectspringboot.exceptions.ResourceNotFoundException;
import com.dailycodework.fisrtprojectspringboot.model.Cart;
import com.dailycodework.fisrtprojectspringboot.response.ApiResponse;
import com.dailycodework.fisrtprojectspringboot.service.cart.ICartService;

import io.micrometer.core.ipc.http.HttpSender.Response;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {
    private final ICartService cartService;

    @GetMapping("/{cartId}/my-cart")    
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId){// Point de terminaison REST pour récupérer un panier par son ID
        try {
            // Appel du service métier pour récupérer le panier
            Cart cart = cartService.getCart(cartId);
            // Retourne une réponse HTTP 200 OK avec le panier dans le corps
            return ResponseEntity.ok(new ApiResponse("Success", cart));
        } catch (ResourceNotFoundException e) {
            // Gestion de l'erreur si le panier n'est pas trouvé
            // Retourne une réponse HTTP 404 NOT_FOUND avec le message d'erreur
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId){// Point de terminaison REST pour supprimer un panier par son ID
            try {
                 // Appel du service métier pour supprimer le panier
                cartService.clearCart(cartId);
                // Retourne une réponse HTTP 200 OK avec le panier dans le corps
                return ResponseEntity.ok(new ApiResponse("Clear Cart success !", null));
            } catch (ResourceNotFoundException e) {
            // Gestion de l'erreur si le panier n'est pas trouvé
            // Retourne une réponse HTTP 404 NOT_FOUND avec le message d'erreur
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
            }
    }

    @GetMapping("/{cartId}/cart/total-price")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId){// Point de terminaison REST pour le montant total  un panier par son ID
        try {
            // Appel du service métier pour obtenir le montant total  du panier
            BigDecimal totalPrice = cartService.getTotalPrice(cartId);
            // Retourne une réponse HTTP 200 OK avec le panier dans le corps
            return ResponseEntity.ok(new ApiResponse("Total price", totalPrice));
        } catch (ResourceNotFoundException e) {
            // Gestion de l'erreur si le panier n'est pas trouvé
            // Retourne une réponse HTTP 404 NOT_FOUND avec le message d'erreur
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
