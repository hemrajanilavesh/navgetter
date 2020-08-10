package com.investscape.navgetter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RequestController {

    @RequestMapping("/")
    public String home() {
        return "redirect:/swagger-ui.html";
    }
}
