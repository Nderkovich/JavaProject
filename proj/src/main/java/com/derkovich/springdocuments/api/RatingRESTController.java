package com.derkovich.springdocuments.api;

import com.derkovich.springdocuments.api.request.RatingRequest;
import com.derkovich.springdocuments.config.jwt.JwtFilter;
import com.derkovich.springdocuments.exceptions.DocumentDoesntExistException;
import com.derkovich.springdocuments.exceptions.RatingCreationException;
import com.derkovich.springdocuments.repository.DocumentRepository;
import com.derkovich.springdocuments.security.UserDetailsServiceImpl;
import com.derkovich.springdocuments.service.RatingService;
import com.derkovich.springdocuments.service.dto.Document;
import com.derkovich.springdocuments.service.dto.Rating;
import com.derkovich.springdocuments.service.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class RatingRESTController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private UserDetailsServiceImpl userService;

    @PostMapping("/api/documents/{id:\\d+}/rating")
    public ResponseEntity<Rating> rateDocument(@PathVariable(value = "id") int docId,
                                               HttpServletRequest request,
                                               @RequestBody RatingRequest rating) throws RatingCreationException, DocumentDoesntExistException {
        User user = userService.getUserByName(jwtFilter.getUserLogin(request));
        Rating rate = new Rating();
        Document document = documentRepository.findFirstById(docId);
        if (document == null){
            throw new DocumentDoesntExistException();
        }
        rate.setDocument(document);
        rate.setRating(rating.getRating());
        rate.setUser(user);
        if (ratingService.save(rate)){
            return new ResponseEntity<>(rate, HttpStatus.OK);
        } else {
            throw new RatingCreationException();
        }
    }
}