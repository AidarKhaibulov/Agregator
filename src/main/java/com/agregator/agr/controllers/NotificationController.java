package com.agregator.agr.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller("/notify")
@Slf4j
public class NotificationController {
    @GetMapping
    public String mainPage(Model model){
        return "notification-main-page";
    }
    @PostMapping("/subscribe")
    public String subscribe(Model model,@Valid @ModelAttribute("category") String category){
        return "";
    }
}
