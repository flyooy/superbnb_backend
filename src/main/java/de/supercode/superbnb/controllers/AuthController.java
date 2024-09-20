package de.supercode.superbnb.controllers;

import de.supercode.superbnb.dto.AuthDto;
import de.supercode.superbnb.entities.AppUser;
import de.supercode.superbnb.services.AuthentificationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthentificationService authentificationService;

    @PostMapping("/signin")
    public void signin(@RequestBody AuthDto dto, HttpServletRequest request, HttpServletResponse response){
        authentificationService.login(dto, request, response);
    }

    @PostMapping("/signup")
    public AppUser signup(@RequestBody AuthDto dto){
        return authentificationService.signUp(dto);
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "log-reg";
    }
}
