package com.derkovich.springdocuments.repository;

import com.derkovich.springdocuments.service.dto.Rating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RatingRepository extends CrudRepository<Rating, Integer> {
    @Query("SELECT AVG(e.rating) FROM Rating e WHERE e.document.id = :document")
    Integer ratingAverage(@Param("document") Integer document);

    Set<Rating> findAllByUser_Id(Integer user);

    Rating findFirstByDocument_IdAndAndUser_Id(Integer document, Integer user);
}
