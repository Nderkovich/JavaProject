package com.derkovich.springdocuments.exceptions;

public class DocumentDoesntExistException extends Exception{
    public DocumentDoesntExistException(){
        super("This document doesn't exist");
    }
}
