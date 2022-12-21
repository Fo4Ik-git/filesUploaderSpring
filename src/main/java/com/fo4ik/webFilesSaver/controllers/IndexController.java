package com.fo4ik.webFilesSaver.controllers;

import com.fo4ik.webFilesSaver.config.Config;
import com.fo4ik.webFilesSaver.model.Role;
import com.fo4ik.webFilesSaver.model.User;
import com.fo4ik.webFilesSaver.repo.LogoRepo;
import com.fo4ik.webFilesSaver.repo.UserRepo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class IndexController {

    private final UserRepo userRepo;
    private final LogoRepo logoRepo;

    public IndexController(UserRepo userRepo, LogoRepo logoRepo) {
        this.userRepo = userRepo;
        this.logoRepo = logoRepo;
    }

   /* @GetMapping("/")
    public String index() {
        return "index";
    }*/
    @GetMapping("/")
    public String index(@AuthenticationPrincipal User user, Model model) {
        if (user != null) {
            model.addAttribute("title", "Index page");
            Config config = new Config(userRepo, logoRepo);
            config.getUserLogo(user, model);
        }
        return "index";
    }
}
