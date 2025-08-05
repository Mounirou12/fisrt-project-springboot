package com.dailycodework.fisrtprojectspringboot.exceptions;

//Cette class est pour generer les erreurs si un nouveau objet veut etre creer 
//Or il existe deja cet Objet dans la base de donnee
public class AlreadyExistsException extends RuntimeException {
      public AlreadyExistsException(String message) {
        super(message);
    }

}
