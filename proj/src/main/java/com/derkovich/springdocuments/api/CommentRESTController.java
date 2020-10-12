package com.derkovich.springdocuments.api;

import com.derkovich.springdocuments.api.request.CommentRequest;
import com.derkovich.springdocuments.config.jwt.JwtFilter;
import com.derkovich.springdocuments.config.jwt.JwtProvider;
import com.derkovich.springdocuments.security.UserDetailsServiceImpl;
import com.derkovich.springdocuments.service.CommentService;
import com.derkovich.springdocuments.service.DocumentService;
import com.derkovich.springdocuments.service.dto.Comment;
import com.derkovich.springdocuments.service.dto.Document;
import com.derkovich.springdocuments.service.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CommentRESTController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserDetailsServiceImpl userService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private JwtFilter jwtFilter;

    @PostMapping("/api/documents/{id:\\d+}/comments")
    public ResponseEntity createComment(@PathVariable(value = "id") int docId, HttpServletRequest request, @RequestBody CommentRequest commentRequest){
        User user = userService.getUserByName(jwtFilter.getUserLogin(request));
        Comment comment = new Comment();
        comment.setText(commentRequest.getComment());
        comment.setUser(user);
        comment.setDocument(documentService.findById(docId));
        if (commentService.saveComment(comment)){
            return ResponseEntity.ok(comment);
        } else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body("Unable to create comment");
        }
    }
}
