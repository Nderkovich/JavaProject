package com.derkovich.springdocuments.api;

import com.derkovich.springdocuments.api.request.AuthRequest;
import com.derkovich.springdocuments.api.request.RegistrationRequest;
import com.derkovich.springdocuments.api.response.AuthResponse;
import com.derkovich.springdocuments.api.response.IsAdminResponse;
import com.derkovich.springdocuments.config.jwt.JwtFilter;
import com.derkovich.springdocuments.config.jwt.JwtProvider;
import com.derkovich.springdocuments.exceptions.InvalidUsernameOrPasswordException;
import com.derkovich.springdocuments.exceptions.TokenExpiredException;
import com.derkovich.springdocuments.exceptions.UserAlreadyRegisteredException;
import com.derkovich.springdocuments.exceptions.WrongTokenException;
import com.derkovich.springdocuments.repository.RoleRepository;
import com.derkovich.springdocuments.security.UserDetailsServiceImpl;
import com.derkovich.springdocuments.service.dto.User;
import com.derkovich.springdocuments.service.dto.VerificationToken;
import com.derkovich.springdocuments.service.events.OnRegistrationCompleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Calendar;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthenticationRESTController {
    @Autowired
    private UserDetailsServiceImpl userService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ApplicationEventPublisher eventPublisher;

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody RegistrationRequest registrationRequest) throws UserAlreadyRegisteredException {
        User u = userService.getUserByName(registrationRequest.getUsername());
        if (u != null) {
            throw new UserAlreadyRegisteredException();
        }
            User user = new User();
            user.setPassword(registrationRequest.getPassword());
            user.setUsername(registrationRequest.getUsername());
            userService.saveUser(user);
            System.out.println("PUBLISH");
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));
            return ResponseEntity.ok("User successfully registered");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> auth(@RequestBody AuthRequest request) throws InvalidUsernameOrPasswordException {
        User userEntity = userService.findFirstByUsernameAndPassword(request.getUsername(), request.getPassword());
        if (userEntity != null) {
            String token = jwtProvider.generateToken(userEntity.getUsername());
            return new ResponseEntity<>(new AuthResponse(token, userEntity.getId()), HttpStatus.OK);
        } else {
            throw new InvalidUsernameOrPasswordException();
        }
    }

    @GetMapping("/isadmin")
    public ResponseEntity<IsAdminResponse> isAdmin(HttpServletRequest request){
        User user = userService.getUserByName(jwtFilter.getUserLogin(request));
        return new ResponseEntity<>(new IsAdminResponse(user.getRoles().contains(roleRepository.findFirstById(1))), HttpStatus.OK);
    }

    @GetMapping("/registrationConfirm")
    public ResponseEntity registrationConfirm(WebRequest request, @RequestParam("token") String token) throws WrongTokenException, TokenExpiredException {
        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            throw new WrongTokenException();
        }
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0){
           throw new TokenExpiredException();
        }
        user.setEnabled(true);
        userService.save(user);
        return ResponseEntity.ok("User saved");
    }
}
