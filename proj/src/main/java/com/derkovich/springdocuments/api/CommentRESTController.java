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
import java.util.List;

@RestController
@CrossOrigin
public class CommentRESTController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserDetailsServiceImpl userService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private JwtFilter jwtFilter;

    @GetMapping("/api/documents/{id:\\d+}/comments")
    public ResponseEntity getGomments(@PathVariable(value = "id") int docId){
        List<Comment> comments = commentService.findAllByDocumentId(docId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/api/documents/{id:\\d+}/comments")
    public ResponseEntity createComment(@PathVariable(value = "id") int docId,
                                        HttpServletRequest request,
                                        @RequestBody CommentRequest commentRequest){
        Document document = documentService.findById(docId);
        if (document == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body("No such document");
        }
        User user = userService.getUserByName(jwtFilter.getUserLogin(request));
        Comment comment = new Comment();
        comment.setText(commentRequest.getComment());
        comment.setUser(user);
        comment.setDocument(document);
        if (commentService.saveComment(comment)){
            return ResponseEntity.ok(comment);
        } else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body("Unable to create comment");
        }
    }

    @DeleteMapping("/api/documents/{id:\\d+}/comments/{commId:\\d+}")
    public ResponseEntity deleteComment(@PathVariable(value = "id") int docId,
                                        @PathVariable(value = "commId") int commId,
                                        HttpServletRequest request){
        Comment comment = commentService.findCommentByDocument_IdAndId(docId, commId);
        if (comment == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such comment");
        }
        User user = userService.getUserByName(jwtFilter.getUserLogin(request));
        if (comment.getUser().getUsername().equals(user.getUsername())){
            commentService.deleteCommentById(commId);
            return ResponseEntity.ok(comment);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not your comment");
        }
    }

    @PutMapping("/api/documents/{id:\\d+}/comments/{commId:\\d+}")
    public ResponseEntity updateComment(@PathVariable(value = "id") int docId,
                                        @PathVariable(value = "commId") int commId,
                                        HttpServletRequest request,
                                        @RequestBody CommentRequest commentRequest){
        Comment comment = commentService.findCommentByDocument_IdAndId(docId, commId);
        if (comment == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such comment");
        }
        User user = userService.getUserByName(jwtFilter.getUserLogin(request));
        if (comment.getUser().getUsername().equals(user.getUsername())){
            comment.setText(commentRequest.getComment());
            commentService.saveComment(comment);
            return ResponseEntity.ok(comment);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not your comment");
        }
    }
}
