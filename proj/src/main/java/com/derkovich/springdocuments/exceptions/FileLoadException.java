package com.derkovich.springdocuments.exceptions;

public class FileLoadException extends Exception{
    public FileLoadException(){
        super("Error loading physical file");
    }
}
