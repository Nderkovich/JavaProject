package com.derkovich.springdocuments.exceptions;

public class InvalidUsernameOrPasswordException extends Exception{
    public InvalidUsernameOrPasswordException(){
        super("Invalid username or password");
    }
}
