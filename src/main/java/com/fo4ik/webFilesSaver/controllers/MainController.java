package com.fo4ik.webFilesSaver.controllers;

import com.fo4ik.webFilesSaver.config.Config;
import com.fo4ik.webFilesSaver.model.Logo;
import com.fo4ik.webFilesSaver.model.Message;
import com.fo4ik.webFilesSaver.model.User;
import com.fo4ik.webFilesSaver.repo.LogoRepo;
import com.fo4ik.webFilesSaver.repo.MessageRepo;
import com.fo4ik.webFilesSaver.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
public class MainController {

    private final MessageRepo messageRepo;
    private final UserRepo userRepo;
    private final LogoRepo logoRepo;

    @Autowired
    public MainController(MessageRepo messageRepo, UserRepo userRepo, LogoRepo logoRepo) {
        this.messageRepo = messageRepo;
        this.userRepo = userRepo;
        this.logoRepo = logoRepo;
    }

    @GetMapping("/main")
    public String mainPage(@AuthenticationPrincipal User user, Model model) {
        Iterable<Message> message = messageRepo.findAll();
        model.addAttribute("title", "Main page");
        model.addAttribute("msg", message);


        //model.addAttribute("logo", "https://www.w3schools.com/images/w3schools_green.jpg");
        Config config = new Config(userRepo, logoRepo);
        config.getUserLogo(user, model);

        try {
           /* User userFromDb = userRepo.findByUsername(user.getUsername());
            Logo logo = logoRepo.findById(userFromDb.getId());
            String path = logo.getPath();
            model.addAttribute("logo", path);*/
        } catch (Exception e) {

        }
        return "main";
    }



    @PostMapping("/main")
    public String addMessage(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag,
            Model model) {

        if (text.equals("") || tag.equals("")) {
            model.addAttribute("error", "Please fill all fields");
            return "main";
        }
        Message message = new Message(text, tag, user);
        messageRepo.save(message);
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("msg", messages);
        return "main";
    }

    @PostMapping("filter")
    public String filter(@RequestParam String filterText, Model model) {
        Iterable<Message> messages;
        if (filterText != null && !filterText.isEmpty()) {
            messages = messageRepo.findByTag(filterText);
        } else {
            messages = messageRepo.findAll();
        }
        model.addAttribute("msg", messages);
        return "main";
    }


    @PostMapping("/main/add_logo")
    public String addLogo(@AuthenticationPrincipal User user, @RequestParam("logoFile") MultipartFile logoFile, Model model) {
        try {
            Logo logos = logoRepo.findById(user.getId());
            if (logos != null && !logoFile.getOriginalFilename().equals("")) {
                logos.setPath(saveLogo(user, logoFile, model));
                logoRepo.save(logos);
            } else {
                Logo logo = new Logo(saveLogo(user, logoFile, model), user);
                logo.setId(user.getId());
                logoRepo.save(logo);
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "main";
        }

        return "redirect:/main";
    }

    private String saveLogo(User user, MultipartFile logoFile, Model model) {
        try {
            Path folder = Path.of(createUserFolde(user.getId()) + "/");
            String[] fileNameArray = logoFile.getOriginalFilename().split("\\.");
            String format = "logo." + fileNameArray[fileNameArray.length - 1];
            Path path = Path.of(folder + "/" + format);
            byte[] bytes = logoFile.getBytes();
            Files.write(path, bytes);

            return String.valueOf(path);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return null;
    }

    private Path createUserFolde(Long userId) {
        File file = new File("files/users/" + userId + "/");
        try {
            if (!file.exists()) {
                file.mkdirs();
            }
            return Path.of(file.getPath());
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return null;
    }

}
