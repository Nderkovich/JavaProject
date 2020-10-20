package com.derkovich.springdocuments.api;

import com.derkovich.springdocuments.api.request.CommentRequest;
import com.derkovich.springdocuments.config.jwt.JwtFilter;
import com.derkovich.springdocuments.exceptions.CommentCreationException;
import com.derkovich.springdocuments.exceptions.CommentDoesntExistException;
import com.derkovich.springdocuments.exceptions.DocumentDoesntExistException;
import com.derkovich.springdocuments.exceptions.NotYourCommentException;
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
@RequestMapping("/api/documents")
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

    @GetMapping("/{id:\\d+}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable(value = "id") int docId){
        List<Comment> comments = commentService.findAllByDocumentId(docId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping("/{id:\\d+}/comments")
    public ResponseEntity<Comment> createComment(@PathVariable(value = "id") int docId,
                                                HttpServletRequest request,
                                                @RequestBody CommentRequest commentRequest)
            throws DocumentDoesntExistException, CommentCreationException {
        Document document = documentService.findById(docId);
        if (document == null){
            throw new DocumentDoesntExistException();
        }
        User user = userService.getUserByName(jwtFilter.getUserLogin(request));
        Comment comment = new Comment();
        comment.setText(commentRequest.getComment());
        comment.setUser(user);
        comment.setDocument(document);
        if (commentService.saveComment(comment)){
            return new ResponseEntity<>(comment, HttpStatus.OK);
        } else{
            throw new CommentCreationException();
        }
    }

    @DeleteMapping("/{id:\\d+}/comments/{commId:\\d+}")
    public ResponseEntity<Comment> deleteComment(@PathVariable(value = "id") int docId,
                                                @PathVariable(value = "commId") int commId,
                                                HttpServletRequest request)
            throws CommentDoesntExistException, NotYourCommentException {
        Comment comment = commentService.findCommentByDocument_IdAndId(docId, commId);
        if (comment == null){
            throw new CommentDoesntExistException();
        }
        User user = userService.getUserByName(jwtFilter.getUserLogin(request));
        if (comment.getUser().getUsername().equals(user.getUsername())){
            commentService.deleteCommentById(commId);
            return new ResponseEntity<>(comment, HttpStatus.OK);
        } else {
            throw new NotYourCommentException();
        }
    }

    @PutMapping("/{id:\\d+}/comments/{commId:\\d+}")
    public ResponseEntity<Comment> updateComment(@PathVariable(value = "id") int docId,
                                                @PathVariable(value = "commId") int commId,
                                                HttpServletRequest request,
                                                @RequestBody CommentRequest commentRequest)
            throws CommentDoesntExistException, NotYourCommentException {
        Comment comment = commentService.findCommentByDocument_IdAndId(docId, commId);
        if (comment == null){
            throw new CommentDoesntExistException();
        }
        User user = userService.getUserByName(jwtFilter.getUserLogin(request));
        if (comment.getUser().getUsername().equals(user.getUsername())){
            comment.setText(commentRequest.getComment());
            commentService.saveComment(comment);
            return new ResponseEntity<>(comment, HttpStatus.OK);
        } else {
            throw new NotYourCommentException();
        }
    }
}
