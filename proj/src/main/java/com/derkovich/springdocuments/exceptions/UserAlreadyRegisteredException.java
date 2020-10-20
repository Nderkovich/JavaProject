package com.derkovich.springdocuments.exceptions;

public class UserAlreadyRegisteredException extends Exception {
    public UserAlreadyRegisteredException(){
        super("User with such username already exists");
    }
}
