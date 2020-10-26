package com.derkovich.springdocuments.service;

import com.derkovich.springdocuments.repository.CommentRepository;
import com.derkovich.springdocuments.service.dto.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    public List<Comment> findAllByDocumentId(Integer id){
        return (List<Comment>) commentRepository.findAllByDocument_IdOrderById(id);
    }

    public List<Comment> findAllByUserId(Integer id){
        return (List<Comment>) commentRepository.findAllByUser_Id(id);
    }

    public boolean saveComment(Comment comment){
        commentRepository.save(comment);
        return true;
    }

    @Transactional
    public void deleteCommentById(Integer id){
        commentRepository.deleteCommentById(id);
    }

    public Comment findCommentByDocument_IdAndId(Integer docId, Integer commId){
        return commentRepository.findCommentByDocument_IdAndId(docId, commId);
    }
}
