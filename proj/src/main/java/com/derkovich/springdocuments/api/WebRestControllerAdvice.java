package com.derkovich.springdocuments.api;

import com.derkovich.springdocuments.exceptions.*;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class WebRestControllerAdvice {

    @ExceptionHandler({DocumentDoesntExistException.class,
                        CommentDoesntExistException.class,
                        FileLoadException.class})
    public ResponseEntity handleNotFoundException(WebRequest request, Exception ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler({InvalidUsernameOrPasswordException.class,
                        UserAlreadyRegisteredException.class,
                        FileUploadException.class,
                        RatingCreationException.class,
                        CommentCreationException.class})
    public ResponseEntity handleBadRequestException(WebRequest request, Exception ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(NotYourCommentException.class)
    public ResponseEntity handleForbiddenException(WebRequest request, Exception ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ex.getMessage());
    }
}
