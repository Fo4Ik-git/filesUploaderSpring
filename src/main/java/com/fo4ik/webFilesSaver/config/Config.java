package com.fo4ik.webFilesSaver.config;

import com.fo4ik.webFilesSaver.model.Logo;
import com.fo4ik.webFilesSaver.model.Role;
import com.fo4ik.webFilesSaver.model.User;
import com.fo4ik.webFilesSaver.repo.LogoRepo;
import com.fo4ik.webFilesSaver.repo.UserRepo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

public class Config {

    private final UserRepo userRepo;
    private final LogoRepo logoRepo;

    public Config(UserRepo userRepo, LogoRepo logoRepo) {
        this.userRepo = userRepo;
        this.logoRepo = logoRepo;

    }

    public void getUserLogo(@AuthenticationPrincipal User user, Model model) {
        try {
            if (user != null) {
                User userFromDb = userRepo.findByUsername(user.getUsername());
                Logo logo = logoRepo.findById(userFromDb.getId());
                if (!logo.getPath().equals("")) {
                    model.addAttribute("logo", "/files/" +logo.getPath());
                   // System.out.println("Logo path: " +  logo.getPath());
                }
                List<Role> roles = new ArrayList<>(user.getRoles());
                for (Role role : roles) {
                    switch (role) {
                        case ADMIN:
                            model.addAttribute("isAdmin", true);
                            break;
                        case USER:
                            model.addAttribute("isUser", true);
                            break;
                        case MODERATOR:
                            model.addAttribute("isModerator", true);
                            break;
                    }
                }
            }
        } catch (Exception e) {

        }
    }
}
