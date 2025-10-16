package com.dailycodework.fisrtprojectspringboot.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import com.dailycodework.fisrtprojectspringboot.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor//is an annotation from the Project Lombok library. It automatically generates a public, no-argument constructor for the Order class. 
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long orderId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    @Enumerated(EnumType.STRING)//This annotation specifies how the orderStatus enum value is persisted.
    //EnumType.STRING tells the persistence layer (like Hibernate) to save the enum value as its string representation in the database (e.g., "PENDING", "SHIPPED", "CANCELLED").
    private OrderStatus orderStatus;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<OrderItem> orderItems;
    
    @ManyToOne// Relation Many-to-One : une seule instance de Order peut avoir plusieurs instances de User
    @JoinColumn(name = "user_id")
    private User user;
    
}
