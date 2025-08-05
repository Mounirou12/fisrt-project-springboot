package com.dailycodework.fisrtprojectspringboot.exceptions;

//Cette classe est cree pour afficher des erreurs de message 
//lorsque l'objet n'est pas trouve dans la base de donnee
public class ResourceNotFoundException  extends RuntimeException{

    public ResourceNotFoundException(String message) {
        super(message);
    }

    
}
