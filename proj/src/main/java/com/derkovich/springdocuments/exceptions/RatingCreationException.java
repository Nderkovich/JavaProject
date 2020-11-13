package com.derkovich.springdocuments.exceptions;

public class RatingCreationException extends Exception{
    public RatingCreationException(){
        super("Unable to rate document");
    }
}
