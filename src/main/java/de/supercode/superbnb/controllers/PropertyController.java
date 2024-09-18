package de.supercode.superbnb.controllers;

import de.supercode.superbnb.dto.PropertyDTO;
import de.supercode.superbnb.entities.Property;
import de.supercode.superbnb.services.PropertyService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    PropertyService propertyService;

    @GetMapping
    public ResponseEntity<List<PropertyDTO>> getAllAvailableVacationApartments(){
        List<PropertyDTO> properties = propertyService.getAllAvailableVacationApartments();
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Property> createNewProperty(@RequestBody Property property){
        Property createdProperty = propertyService.createNewProperty(property);
        return new ResponseEntity<>(createdProperty, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(@PathVariable Long id, @RequestBody Property property){

        try{
            Property updatedProperty = propertyService.updateProperty(id, property);
            return new ResponseEntity<>(updatedProperty, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProperty (@PathVariable Long id){
        try {
            propertyService.deleteProperty(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
