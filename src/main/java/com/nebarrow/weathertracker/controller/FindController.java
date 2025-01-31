package com.nebarrow.weathertracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FindController {

    @GetMapping("/find")
    public String showFindForm() {
        return "index";
    }

    @PostMapping("/find")
    public String find() {
        return "redirect:/";
    }
}
