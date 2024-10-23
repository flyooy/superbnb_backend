package de.supercode.superbnb.controllers;

import de.supercode.superbnb.dto.JwtDto;
import de.supercode.superbnb.dto.SignUpDto;
import de.supercode.superbnb.entities.AppUser;
import de.supercode.superbnb.services.AuthentificationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    AuthentificationService authentificationService;

    @PostMapping("/signin")
    public ResponseEntity<JwtDto> signin(Authentication authentication){
        return ResponseEntity.ok(new JwtDto(authentificationService.getJwt(authentication)));
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
