package com.golmart.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "pages/home"; // Trả về trang home.html
    }

    @GetMapping("/contact")
    public String contact() {
        return "pages/contact"; // Trả về trang contact.html
    }

    @GetMapping("/catalog")
    public String catalog() {
        return "pages/catalog"; // Trả về trang catalog.html
    }

    @GetMapping("/testimonials")
    public String testimonials() {
        return "pages/testimonials"; // Trả về trang testimonials.html
    }
}