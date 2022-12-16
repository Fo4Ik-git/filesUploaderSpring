package com.fo4ik.webFilesSaver.controllers;

import com.fo4ik.webFilesSaver.model.Message;
import com.fo4ik.webFilesSaver.model.User;
import com.fo4ik.webFilesSaver.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    private MessageRepo messageRepo;

    @GetMapping("/main")
    public String mainPage(Model model) {
        Iterable<Message> message =  messageRepo.findAll();
        model.addAttribute("title", "Main page");
        model.addAttribute("msg", message);



        model.addAttribute("logo", "https://jaylog.co/wp-content/uploads/2017/12/noimg.png");
        return "main";
    }

    @PostMapping("/main")
    public String addMessage(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag,
            Model model) {

        if(text.equals("") || tag.equals("")) {
            model.addAttribute("error", "Please fill all fields");
            return "main";
        }
        Message message = new Message(text, tag, user);
        messageRepo.save(message);
        Iterable<Message> messages =  messageRepo.findAll();
        model.addAttribute("msg", messages);
        return "main";
    }

    @PostMapping("filter")
    public String filter(@RequestParam String filterText, Model model) {
        Iterable<Message> messages;
        if(filterText != null && !filterText.isEmpty()) {
            messages = messageRepo.findByTag(filterText);
        } else {
            messages = messageRepo.findAll();
        }
        model.addAttribute("msg", messages);
        return "main";
    }

}
