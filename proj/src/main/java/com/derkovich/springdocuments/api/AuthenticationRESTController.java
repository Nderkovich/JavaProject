package com.derkovich.springdocuments.api;

import com.derkovich.springdocuments.api.request.AuthRequest;
import com.derkovich.springdocuments.api.request.RegistrationRequest;
import com.derkovich.springdocuments.api.response.AuthResponse;
import com.derkovich.springdocuments.config.jwt.JwtProvider;
import com.derkovich.springdocuments.repository.RoleRepository;
import com.derkovich.springdocuments.security.UserDetailsServiceImpl;
import com.derkovich.springdocuments.service.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@CrossOrigin
public class AuthenticationRESTController {
    @Autowired
    private UserDetailsServiceImpl userService;
    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody RegistrationRequest registrationRequest) {
        User u = userService.getUserByName(registrationRequest.getUsername());
        if (u != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("User with such username already exists");
        }
        User user = new User();
        user.setPassword(registrationRequest.getPassword());
        user.setUsername(registrationRequest.getUsername());
        userService.saveUser(user);
        return ResponseEntity.ok("User successfully registered");
    }

    @PostMapping("/auth")
    public ResponseEntity auth(@RequestBody AuthRequest request) {
        User userEntity = userService.findFirstByUsernameAndPassword(request.getUsername(), request.getPassword());
        if (userEntity != null) {
            String token = jwtProvider.generateToken(userEntity.getUsername());
            return ResponseEntity.ok(new AuthResponse(token, userEntity.getId()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body("Invalid username or password");
        }
    }
}
