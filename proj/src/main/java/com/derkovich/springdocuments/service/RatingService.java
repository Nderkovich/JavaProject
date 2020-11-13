package com.derkovich.springdocuments.service;

import com.derkovich.springdocuments.repository.RatingRepository;
import com.derkovich.springdocuments.service.dto.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    RatingRepository ratingRepository;

    public Integer findDocumentRating(Integer id){
        return ratingRepository.ratingAverage(id);
    }

    public List<Rating> findUserRatings(Integer id){
        return (List<Rating>) ratingRepository.findAllByUser_Id(id);
    }

    public boolean save(Rating rating){
        Rating ratingFromDB = ratingRepository.findFirstByDocument_IdAndAndUser_Id(rating.getDocument().getId(), rating.getUser().getId());
        if (ratingFromDB != null){
            ratingFromDB.setRating(rating.getRating());
            ratingRepository.save(ratingFromDB);
            return true;
        }
        else {
            ratingRepository.save(rating);
            return true;
        }
    }

}
