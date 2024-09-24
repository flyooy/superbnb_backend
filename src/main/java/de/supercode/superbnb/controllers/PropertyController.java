package de.supercode.superbnb.controllers;


import de.supercode.superbnb.dto.PropertyCreateDTO;
import de.supercode.superbnb.dto.PropertyDTO;
import de.supercode.superbnb.dto.PropertyUpdateDTO;
import de.supercode.superbnb.entities.Property;
import de.supercode.superbnb.services.PropertyService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/properties")
@CrossOrigin(origins = "http://127.0.0.1:5500")

public class PropertyController {

    @Autowired
    PropertyService propertyService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<Property> getAllProperties(){
        return propertyService.getAllProperties();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable Long id) {
        Optional<Property> property = propertyService.getPropertyById(id);
        return property.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/public")
    public ResponseEntity<List<PropertyDTO>> getAllAvailableVacationApartments() {
        List<PropertyDTO> properties = propertyService.getAllAvailableVacationApartments();
        System.out.println("Fetched properties: " + properties);
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<Property> createNewProperty(@Valid @RequestBody PropertyCreateDTO propertyDTO){
        Property property = new Property();
        property.setAdress(propertyDTO.adress());
        property.setCity(propertyDTO.city());
        property.setPricePerNight(propertyDTO.pricePerNight());
        property.setAvailability(propertyDTO.availability());

        Property createdProperty = propertyService.createNewProperty(property);
        return new ResponseEntity<>(createdProperty, HttpStatus.CREATED);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(@PathVariable Long id, @Valid @RequestBody PropertyUpdateDTO propertyDTO) {
        try {
            Optional<Property> optionalProperty = propertyService.getPropertyById(id);


            if (optionalProperty.isPresent()) {
                Property property = optionalProperty.get();
                property.setAdress(propertyDTO.adress());
                property.setCity(propertyDTO.city());
                property.setPricePerNight(propertyDTO.pricePerNight());
                property.setAvailability(propertyDTO.availability());

                Property updatedProperty = propertyService.updateProperty(id, property);

                return new ResponseEntity<>(updatedProperty, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
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
