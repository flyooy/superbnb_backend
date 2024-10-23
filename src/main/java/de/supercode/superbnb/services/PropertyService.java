package de.supercode.superbnb.services;

import de.supercode.superbnb.dto.PropertyDTO;
import de.supercode.superbnb.dto.PropertyRequestDTO;
import de.supercode.superbnb.dto.PropertyResponseDTO;
import de.supercode.superbnb.entities.Property;
import de.supercode.superbnb.repositories.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropertyService {
    @Autowired
    PropertyRepository propertyRepository;


    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    public Property getPropertyById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        return propertyRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Property not found with id: " + id));
    }

    public List<PropertyDTO> getAllAvailableVacationApartments() {
        List<Property> properties = propertyRepository.findAllByAvailabilityIsTrue();

        return properties.stream()
                .map(property -> new PropertyDTO(property.getId(), property.getAdress(), property.getCity(), property.getPricePerNight()))
                .collect(Collectors.toList());
    }

    public Property createNewProperty(Property property) {
        return propertyRepository.save(property);
    }

    public Property updateProperty(Long id, Property updatedProperty) {
        return propertyRepository.findById(id)
                .map(property -> {
                    property.setAdress(updatedProperty.getAdress());
                    property.setCity(updatedProperty.getCity());
                    property.setPricePerNight(updatedProperty.getPricePerNight());
                    property.setAvailability(updatedProperty.isAvailability());
                    return propertyRepository.save(property);
                })
                .orElseThrow(() -> new RuntimeException("Property not found with id " + id));
    }

    public void deleteProperty(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        if (!propertyRepository.existsById(id)) {
            throw new jakarta.persistence.EntityNotFoundException("Property not found with id: " + id);
        }
        propertyRepository.deleteById(id);

    }

}

