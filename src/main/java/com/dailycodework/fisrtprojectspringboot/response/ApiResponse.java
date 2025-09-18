package com.dailycodework.fisrtprojectspringboot.response;

import lombok.AllArgsConstructor;
import lombok.Data;

// Annotation Lombok qui génère automatiquement un constructeur avec un argument pour chaque champ de la classe.
// Cela crée un constructeur comme : public ApiResponse(String message, Object data) { ... }
@AllArgsConstructor
// Annotation Lombok qui génère automatiquement les méthodes getters, setters,
// toString(), equals() et hashCode().
// Évite d'avoir à écrire tout ce code répétitif manuellement.
@Data
// Déclaration de la classe
public class ApiResponse {
    // Champ pour stocker un message de statut (ex: "Succès", "Erreur", etc.)
    private String message;
    // Champ pour stocker les données de la réponse. De type Object pour être
    // flexible et pouvoir contenir
    // n'importe quel type d'objet (une String, une List, un DTO, une entité, etc.).
    private Object data;
}
