package com.derkovich.springdocuments.api;

import com.derkovich.springdocuments.api.request.AuthRequest;
import com.derkovich.springdocuments.api.request.RegistrationRequest;
import com.derkovich.springdocuments.api.response.AuthResponse;
import com.derkovich.springdocuments.config.jwt.JwtProvider;
import com.derkovich.springdocuments.security.UserDetailsServiceImpl;
import com.derkovich.springdocuments.service.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthenticationRESTController {
    @Autowired
    private UserDetailsServiceImpl userService;
    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/register")
    public String registerUser(@RequestBody RegistrationRequest registrationRequest) {
        User u = new User();
        u.setPassword(registrationRequest.getPassword());
        u.setUsername(registrationRequest.getUsername());
        userService.saveUser(u);
        return "OK";
    }

    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody AuthRequest request) {
        User userEntity = userService.findFirstByUsernameAndPassword(request.getUsername(), request.getPassword());
        if (userEntity != null) {
            String token = jwtProvider.generateToken(userEntity.getUsername());
            return new AuthResponse(token);
        } else {
            return new AuthResponse("notokay");
        }
    }
}
