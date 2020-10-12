package com.derkovich.springdocuments.service;

import com.derkovich.springdocuments.repository.CommentRepository;
import com.derkovich.springdocuments.service.dto.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    public List<Comment> findAllByDocumentId(Integer id){
        return (List<Comment>) commentRepository.findAllByDocument_Id(id);
    }

    public List<Comment> findAllByUserId(Integer id){
        return (List<Comment>) commentRepository.findAllByUser_Id(id);
    }

    public boolean saveComment(Comment comment){
        commentRepository.save(comment);
        return true;
    }
}
