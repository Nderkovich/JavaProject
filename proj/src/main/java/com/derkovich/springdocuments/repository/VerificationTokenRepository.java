package com.derkovich.springdocuments.repository;

import com.derkovich.springdocuments.service.dto.User;
import com.derkovich.springdocuments.service.dto.VerificationToken;
import org.springframework.data.repository.CrudRepository;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
    VerificationToken findByUser(User user);
}
