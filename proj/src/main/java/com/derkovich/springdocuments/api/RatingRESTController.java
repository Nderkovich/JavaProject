package com.derkovich.springdocuments.api;

import com.derkovich.springdocuments.api.request.RatingRequest;
import com.derkovich.springdocuments.config.jwt.JwtFilter;
import com.derkovich.springdocuments.repository.DocumentRepository;
import com.derkovich.springdocuments.security.UserDetailsServiceImpl;
import com.derkovich.springdocuments.service.RatingService;
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
    public ResponseEntity rateDocument(@PathVariable(value = "id") int docId, HttpServletRequest request, @RequestBody RatingRequest rating){
        User user = userService.getUserByName(jwtFilter.getUserLogin(request));
        Rating rate = new Rating();
        rate.setDocument(documentRepository.findFirstById(docId));
        rate.setRating(rating.getRating());
        rate.setUser(user);
        if (ratingService.save(rate)){
            return ResponseEntity.ok("Rate accepted");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body("Unable to rate document");
        }
    }
}