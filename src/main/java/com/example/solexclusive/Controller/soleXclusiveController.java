package com.example.solexclusive.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class soleXclusiveController {
    @GetMapping({"/"})
    public String page(){
        return "index";
    }
}
