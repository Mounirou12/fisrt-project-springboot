package com.dailycodework.fisrtprojectspringboot.model;

import java.util.List;

import org.hibernate.annotations.NaturalId;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)

    private Long id;
    private String firstName;
    private String lastName;
    @NaturalId//This annotation indicates that the email field should be unique in the database
    private String email;
    private String password;


    @OneToOne(mappedBy="user",cascade=CascadeType.ALL,orphanRemoval=true)// Relation One-to-One avec cascade ALL : les opérations sur User affecteront cette entité
    private Cart cart;

    @OneToMany(mappedBy="user",cascade=CascadeType.ALL,orphanRemoval=true)// Relation One-to-Many : une seule instance de User peut avoir plusieurs instances de Order
    private List<Order> orders;
}
