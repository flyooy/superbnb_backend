package de.supercode.superbnb.services;

import de.supercode.superbnb.dto.PropertyDTO;
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

    public Optional<Property> getPropertyById(long id){
        return propertyRepository.findById(id);
    }

    public List<PropertyDTO> getAllAvailableVacationApartments(){
        List<Property> properties = propertyRepository.findAllByAvailabilityIsTrue();

        return properties.stream()
                .map(property -> new PropertyDTO(property.getAdress(), property.getCity(), property.getPricePerNight()))
                .collect(Collectors.toList());
    }

    public Property createNewProperty(Property property){
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

    public void deleteProperty(Long id){
        propertyRepository.deleteById(id);
    }
}

