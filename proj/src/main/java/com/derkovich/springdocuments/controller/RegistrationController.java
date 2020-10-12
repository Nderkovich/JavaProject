package com.derkovich.springdocuments.controller;

import com.derkovich.springdocuments.service.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.derkovich.springdocuments.security.UserDetailsServiceImpl;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegistrationController {

    @Autowired
    private UserDetailsServiceImpl userService;

    @GetMapping("/registration")
    public ModelAndView registration(Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration");
        model.addAttribute("userForm", new User());
        return modelAndView;
    }

    @PostMapping("/registration")
    public ModelAndView addUser(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration");
        if (bindingResult.hasErrors()){
            return modelAndView;
        }
        if (!userService.saveUser(userForm)){
            model.addAttribute("errorMessage", "Пользователь с таким именеи уже существует");
            return modelAndView;
        }
        return modelAndView;
    }

}
