package com.derkovich.springdocuments.exceptions;

public class CommentDoesntExistException extends Exception{
    public CommentDoesntExistException(){
        super("This comment doesn't exist");
    }
}
