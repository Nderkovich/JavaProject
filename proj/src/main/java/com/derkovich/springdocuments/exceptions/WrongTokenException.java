package com.derkovich.springdocuments.exceptions;

public class WrongTokenException extends Exception{
    public WrongTokenException(){
        super("Wrong token");
    }
}
