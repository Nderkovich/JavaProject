package com.derkovich.springdocuments.exceptions;

public class CommentCreationException extends Exception{
    public CommentCreationException(){
        super("Error creating comment");
    }
}
