package com.derkovich.springdocuments.repository;

import com.derkovich.springdocuments.service.dto.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer> {
    List<Comment> findAllByDocument_IdOrderById(Integer document);
    List<Comment> findAllByUser_Id(Integer user);
    void deleteCommentById(Integer id);
    Comment findCommentByDocument_IdAndId(Integer docId, Integer commId);
}
