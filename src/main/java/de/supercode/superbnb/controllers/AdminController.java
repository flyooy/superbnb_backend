package de.supercode.superbnb.controllers;

import de.supercode.superbnb.dto.BookingDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class AdminController {
    //@PreAuthorize("hasAuthority('USER')")
    @GetMapping("/some-protected-resource")
    public ResponseEntity<String> getGeheimeInformationen(){
        return ResponseEntity.ok("Geheime Informationen, ich habe Zugang, ich bin User, hahaha. Admin ist ein Narr ");
    }
}
