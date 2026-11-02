package com.mytutorial.onlineexammodule;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping({"/", "/home"})
    public String showHome() {
        return "home";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {
        User user = userRepository.findByUsernameAndPassword(username, password);
        if (user != null) {
            session.setAttribute("loggedInUser", user);
            return "redirect:/dashboard";
        }
        model.addAttribute("error", "Invalid corporate credentials. Please try again.");
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               Model model) {
        if (userRepository.findByUsername(username) != null) {
            model.addAttribute("error", "That username is already registered.");
            return "register";
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        userRepository.save(newUser);
        model.addAttribute("success", "Account created successfully! Please sign in.");
        return "login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String username,
                                        @RequestParam String newPassword,
                                        @RequestParam String confirmPassword,
                                        Model model) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            model.addAttribute("error", "No account found matching username: " + username);
            return "forgot-password";
        }
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New password and confirmation do not match.");
            return "forgot-password";
        }
        user.setPassword(newPassword);
        userRepository.save(user);
        model.addAttribute("success", "Password reset successfully! Sign in with your new password.");
        return "login";
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "dashboard";
    }

    @GetMapping("/contact")
    public String showContact(@RequestParam(required = false) String name, Model model) {
        if (name != null && !name.isBlank()) {
            model.addAttribute("sent", "Thank you, " + name + "! Your support ticket has been logged.");
        }
        return "contact";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}