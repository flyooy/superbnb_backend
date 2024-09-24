package de.supercode.superbnb.controllers;

import de.supercode.superbnb.dto.SignUpDto;
import de.supercode.superbnb.entities.AppUser;
import de.supercode.superbnb.services.AuthentificationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class AuthController {

    @Autowired
    AuthentificationService authentificationService;

    @PostMapping("/signin")
    public void signin(){

    }

    @PostMapping("/signup")
    public AppUser signup(@Valid @RequestBody SignUpDto dto){
        return authentificationService.signUp(dto);
    }

    @GetMapping("/logout")
    public void logout(HttpSession session){
        session.invalidate();
    }






}
