package com.derkovich.springdocuments.api;

import com.derkovich.springdocuments.api.request.AuthRequest;
import com.derkovich.springdocuments.api.request.RegistrationRequest;
import com.derkovich.springdocuments.api.response.AuthResponse;
import com.derkovich.springdocuments.api.response.IsAdminResponse;
import com.derkovich.springdocuments.config.jwt.JwtFilter;
import com.derkovich.springdocuments.config.jwt.JwtProvider;
import com.derkovich.springdocuments.exceptions.InvalidUsernameOrPasswordException;
import com.derkovich.springdocuments.exceptions.UserAlreadyRegisteredException;
import com.derkovich.springdocuments.repository.RoleRepository;
import com.derkovich.springdocuments.security.UserDetailsServiceImpl;
import com.derkovich.springdocuments.service.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


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
}
