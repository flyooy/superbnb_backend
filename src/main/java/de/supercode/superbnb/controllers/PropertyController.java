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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Property> createNewProperty(@Valid @RequestBody PropertyCreateDTO propertyDTO){
        Property property = new Property();
        property.setAdress(propertyDTO.adress());
        property.setCity(propertyDTO.city());
        property.setPricePerNight(propertyDTO.pricePerNight());
        property.setAvailability(propertyDTO.availability());

        Property createdProperty = propertyService.createNewProperty(property);
        return new ResponseEntity<>(createdProperty, HttpStatus.CREATED);
    }

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
