package com.dailycodework.fisrtprojectspringboot.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.hibernate.annotations.NaturalId;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    @ManyToMany(fetch=FetchType.EAGER,cascade= // Relation Many-to-Many : une seule instance de User peut avoir plusieurs instances de Role
    {CascadeType.DETACH, CascadeType.MERGE ,CascadeType.PERSIST,CascadeType.REFRESH}) // les opérations sur User affecteront ces entités roles  
    @JoinTable(name="user_roles", joinColumns= @JoinColumn(name="user_id", referencedColumnName="id"),// Spécifie la table intermédiaire pour la relation Many-to-Many 
    inverseJoinColumns=@JoinColumn(name="role_id", referencedColumnName="id")// Spécifie la colonne de jointure inverse pour la relation Many-to-Many
    )
    private  Collection<Role> roles = new HashSet<>();
}
