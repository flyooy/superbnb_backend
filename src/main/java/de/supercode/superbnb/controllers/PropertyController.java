package de.supercode.superbnb.controllers;


import de.supercode.superbnb.dto.ErrorResponseDTO;
import de.supercode.superbnb.dto.PropertyRequestDTO;
import de.supercode.superbnb.dto.PropertyResponseDTO;
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
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequestMapping("/api/properties")
@CrossOrigin(origins = "http://localhost:5173/")
//@PreAuthorize("hasAuthority('ADMIN')")
public class PropertyController {

    @Autowired
    PropertyService propertyService;


    @GetMapping
    public ResponseEntity<List<PropertyResponseDTO>> getAllProperties(){
        List<Property> properties = propertyService.getAllProperties();
        List<PropertyResponseDTO> propertyResponseDTOs = properties.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ok(propertyResponseDTOs);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponseDTO> getPropertyById(@PathVariable Long id) {
        try {
            Property property = propertyService.getPropertyById(id);
            PropertyResponseDTO responseDTO = convertToResponseDTO(property);
            return ResponseEntity.ok(responseDTO);
        } catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    private PropertyResponseDTO convertToResponseDTO(Property property) {
        if (property == null) {
            return null;
        }
        return new PropertyResponseDTO(
                property.getId(),
                property.getAdress(),
                property.getCity(),
                property.getPricePerNight(),
                property.isAvailability()
        );
    }


    @PostMapping
    public ResponseEntity<PropertyResponseDTO> createNewProperty(@RequestBody PropertyRequestDTO propertyDTO){
        Property property = new Property();
        property.setAdress(propertyDTO.adress());
        property.setCity(propertyDTO.city());
        property.setPricePerNight(propertyDTO.pricePerNight());
        property.setAvailability(propertyDTO.availability());

        Property createdProperty = propertyService.createNewProperty(property);
        PropertyResponseDTO responseDTO = convertToResponseDTO(createdProperty);
        return new ResponseEntity<>(responseDTO , HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProperty(@PathVariable Long id, @Valid @RequestBody PropertyRequestDTO propertyDTO) {
        try {
            Property updatedProperty = new Property();
            updatedProperty.setAdress(propertyDTO.adress());
            updatedProperty.setCity(propertyDTO.city());
            updatedProperty.setPricePerNight(propertyDTO.pricePerNight());
            updatedProperty.setAvailability(propertyDTO.availability());

            Property savedProperty = propertyService.updateProperty(id, updatedProperty);
            PropertyResponseDTO responseDTO = convertToResponseDTO(savedProperty);
            return ResponseEntity.ok(responseDTO);

        } catch (EntityNotFoundException e) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO("Property not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
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
