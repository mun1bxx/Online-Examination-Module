package com.mytutorial.onlineexammodule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String showLoginForm() { return "login"; }

    @GetMapping("/register")
    public String showRegisterForm() { return "register"; }

    @GetMapping("/home")
    public String home() { return "home"; }

    @GetMapping("/contact")
    public String contact() { return "contact"; }

    @GetMapping("/dashboard")
    public String dashboard() { return "dashboard"; }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userRepository.save(user);
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password, Model model) {
        User existingUser = userRepository.findByUsernameAndPassword(username, password);
        if (existingUser != null) {
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid Credentials");
            return "login";
        }
    }
}