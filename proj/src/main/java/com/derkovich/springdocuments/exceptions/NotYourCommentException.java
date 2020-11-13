package com.derkovich.springdocuments.exceptions;

public class NotYourCommentException extends Exception{
    public NotYourCommentException(){
        super("Comment is not yours");
    }
}
