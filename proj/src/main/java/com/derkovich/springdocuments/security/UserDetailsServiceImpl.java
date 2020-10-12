package com.derkovich.springdocuments.security;

import com.derkovich.springdocuments.repository.RoleRepository;
import com.derkovich.springdocuments.repository.UserRepository;
import com.derkovich.springdocuments.service.dto.Role;
import com.derkovich.springdocuments.service.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return new MyUserDetails(user);
    }

    public User findUserById(Integer userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    public User findFirstByUsernameAndPassword(String username, String password){

        User user = userRepository.getUserByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("No such user");
        }
        if (bCryptPasswordEncoder.matches(password, user.getPassword())){
            return user;
        }
        //TODO
        return null;
    }

    public User getUserByName(String username){
        return userRepository.getUserByUsername(username);
    }

    public boolean saveUser(User user){
        User userFormDB = userRepository.getUserByUsername(user.getUsername());

        if (userFormDB != null){
            return false;
        }
        Role role = roleRepository.findFirstById(2);
        user.getRoles().add(role);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }
}
