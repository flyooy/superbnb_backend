package de.supercode.superbnb.controllers;

import de.supercode.superbnb.dto.PropertyDTO;
import de.supercode.superbnb.services.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "http://localhost:5173")
public class PublicController {
    @Autowired
    PropertyService propertyService;
    @GetMapping("/properties")
    public ResponseEntity<List<PropertyDTO>> getAllAvailableVacationApartments() {
        List<PropertyDTO> properties = propertyService.getAllAvailableVacationApartments();
        System.out.println("Fetched properties: " + properties);
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }
}
