package com.derkovich.springdocuments.api;

import com.derkovich.springdocuments.exceptions.*;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
                        CommentCreationException.class,
                        WrongTokenException.class,
                        TokenExpiredException.class,
                        Exception.class})
    public ResponseEntity handleBadRequestException(WebRequest request, Exception ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Set<String>> handleConstraintViolation(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

        Set<String> messages = new HashSet<>(constraintViolations.size());
        messages.addAll(constraintViolations.stream()
                .map(constraintViolation -> String.format("%s value %s", constraintViolation.getPropertyPath(),
                        constraintViolation.getMessage()))
                .collect(Collectors.toList()));

        return new ResponseEntity<>(messages, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(NotYourCommentException.class)
    public ResponseEntity handleForbiddenException(WebRequest request, Exception ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ex.getMessage());
    }
}
