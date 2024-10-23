package de.supercode.superbnb;

import de.supercode.superbnb.controllers.PropertyController;
import de.supercode.superbnb.dto.PropertyCreateDTO;
import de.supercode.superbnb.dto.PropertyRequestDTO;
import de.supercode.superbnb.dto.PropertyResponseDTO;
import de.supercode.superbnb.entities.Booking;
import de.supercode.superbnb.entities.Property;
import de.supercode.superbnb.services.PropertyService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.verification.VerificationMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PropertyControllerTest {
    // STRUKTUR
    // Trippel A
    // (1) Arrange (Vorbereitung)
    // (2) Act (Ausführung)
    // (3) Assert (Überprüfung)

    // Mock Objekt = eine simulierte Version einer realen Klasse
    @Mock
    private static PropertyService mockPropertyService;
    @InjectMocks
    private PropertyController propertyController;

    private static PropertyResponseDTO examplePropertyResponseDTO;
    private static PropertyRequestDTO examplePropertyRequestDTO;

    private static Property exampleProperty;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @BeforeAll
    public static void setUpBeforeClass(){

        examplePropertyRequestDTO = new PropertyRequestDTO(
                    "Finkenstrasse 10",
                    "Berlin",
                    59.00,
                    true);

            examplePropertyResponseDTO = new PropertyResponseDTO(
                1L, "Finkenstrasse 10", "Berlin", 59.00, true
        );
        exampleProperty = new Property();
        exampleProperty.setId(1L);
        exampleProperty.setAdress("Finkenstrasse 10");
        exampleProperty.setCity("Berlin");
        exampleProperty.setPricePerNight(59.00);
        exampleProperty.setAvailability(true);
    }

    @Test
    public void testGetPropertyByIdWhenFound(){
        // (1) Arrange
        Long propertyId = 1L;
        when(mockPropertyService.getPropertyById(propertyId)).thenReturn(exampleProperty);

        // (2) Act -> aufrugen der zu testenden Methode
        ResponseEntity<PropertyResponseDTO> response = propertyController.getPropertyById(propertyId);

        // (3) Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(examplePropertyResponseDTO, response.getBody());
        Mockito.verify(mockPropertyService, times(1)).getPropertyById(propertyId);

    }

    @Test
    public void testGetPropertyById_NotFound() {
        // (1) Arrange
        Long propertyId = 1L;

        // Мокаем возвращение пустого Optional
        when(mockPropertyService.getPropertyById(propertyId)).thenThrow(new EntityNotFoundException("Property not found with id " + propertyId));

        // (2) Act
        ResponseEntity<PropertyResponseDTO> response = propertyController.getPropertyById(propertyId);

        // (3) Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        Mockito.verify(mockPropertyService, times(1)).getPropertyById(propertyId);
    }

    // Get All Books
    @Test
    public void testGetAllProperties_ReturnListOfProperties(){
        // Arrange
        List<Property> properties = List.of(
                exampleProperty,
                new Property(2L, "Longstrasse 15", "Duisburg", 44.00, true)
        );
        List<PropertyResponseDTO> expectedResponseDTOs = properties.stream()
                .map(property -> new PropertyResponseDTO(
                        property.getId(),
                        property.getAdress(),
                        property.getCity(),
                        property.getPricePerNight(),
                        property.isAvailability()
                ))
                .collect(Collectors.toList());
        when(mockPropertyService.getAllProperties()).thenReturn(properties);

        // Act
        ResponseEntity<List<PropertyResponseDTO>> response =  propertyController.getAllProperties();

        // (3) Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponseDTOs, response.getBody());
        Mockito.verify(mockPropertyService, times(1)).getAllProperties();

    }

    @Test
    public void testCreateProperty_Succesffully(){
        // Arrange
        when(mockPropertyService.createNewProperty(any(Property.class))).thenReturn(exampleProperty);

        // Act
        ResponseEntity<PropertyResponseDTO> response = propertyController.createNewProperty(examplePropertyRequestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(examplePropertyResponseDTO, response.getBody());
        assertEquals("Finkenstrasse 10", response.getBody().adress()); // tiefe Prüfung
        Mockito.verify(mockPropertyService, times(1)).createNewProperty(any(Property.class));

    }

    @Test
    public void testDeleteProperty_Successfully(){
        // Arrange
        Long propertyId = 1L;
        doNothing().when(mockPropertyService).deleteProperty(propertyId);

        // Act
        ResponseEntity<Void> response = propertyController.deleteProperty(propertyId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(mockPropertyService, times(1)).deleteProperty(propertyId);
    }

    @Test
    public void testUpdateProperty_Successfully(){
        Long propertyId = 1L;
        Property existingProperty = new Property();
        existingProperty.setId(propertyId);
        existingProperty.setAdress("Finkenstrasse 10");
        existingProperty.setCity("Berlin");
        existingProperty.setPricePerNight(59.00);
        existingProperty.setAvailability(true);

        PropertyRequestDTO updatedPropertyRequestDTO = new PropertyRequestDTO(
                "Finkenstrasse 11",
                "Berlinn",
                59.00,
                false
        );

        Property updatedProperty = new Property();
        updatedProperty.setId(propertyId);
        updatedProperty.setAdress(updatedPropertyRequestDTO.adress());
        updatedProperty.setCity(updatedPropertyRequestDTO.city());
        updatedProperty.setPricePerNight(updatedPropertyRequestDTO.pricePerNight());
        updatedProperty.setAvailability(updatedPropertyRequestDTO.availability());
        when(mockPropertyService.getPropertyById(propertyId)).thenReturn(existingProperty);
        when(mockPropertyService.updateProperty(eq(propertyId), any(Property.class))).thenReturn(updatedProperty);
        PropertyResponseDTO updatedPropertyResponseDTO = new PropertyResponseDTO(
                propertyId,
                updatedPropertyRequestDTO.adress(),
                updatedPropertyRequestDTO.city(),
                updatedPropertyRequestDTO.pricePerNight(),
                updatedPropertyRequestDTO.availability()
        );
        // Act
        ResponseEntity<Object> response = propertyController.updateProperty(propertyId, updatedPropertyRequestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedPropertyResponseDTO, response.getBody());
        Mockito.verify(mockPropertyService, times(1)).updateProperty(eq(propertyId), any(Property.class));

    }
}
