package com.fo4ik.webFilesSaver.controllers;

import com.fo4ik.webFilesSaver.model.Role;
import com.fo4ik.webFilesSaver.model.User;
import com.fo4ik.webFilesSaver.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.Collections;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("title", "Registration");
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {

        User userFromDb = userRepo.findByUsername(user.getUsername());

        //Check if user already exist
        if (userFromDb != null) {
            model.addAttribute("message", "User exists!");
            return "registration";
        }

        //Check is fills all fields
        if (user.getUsername().equals("") || user.getPassword().equals("")) {
            model.addAttribute("message", "Please fill all fields");
            return "registration";
        }

        System.out.println(user.toString());
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);

        return "redirect:/login";
    }
}
