package de.supercode.superbnb;
import de.supercode.superbnb.dto.PropertyRequestDTO;
import de.supercode.superbnb.dto.PropertyResponseDTO;
import de.supercode.superbnb.entities.Property;
import de.supercode.superbnb.repositories.PropertyRepository;
import de.supercode.superbnb.services.PropertyService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class PropertyServiseTest {
    @Mock
    private static PropertyRepository mockPropertyRepository;
    @InjectMocks
    private static PropertyService propertyService;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testGetPropertyById(){
        // 1. Arrange
        Property existingProperty = new Property(1L, "Finkenstrasse 11",
                "Berlinn",
                59.00,
                false);
        when(mockPropertyRepository.findById(existingProperty.getId())).thenReturn(Optional.of(existingProperty));

        // 2. Action
        Property property = propertyService.getPropertyById(existingProperty.getId());

        // 3. Assertion
        assertNotNull(property, "Property should not be null");
        assertEquals(existingProperty.getCity(), property.getCity());
        assertEquals(existingProperty.getId(), property.getId());
    }

    @Test
    public void testGetPropertyById_NotFound(){
        // 1. Arrange
        when(mockPropertyRepository.findById(1L)).thenReturn(Optional.empty());

        // 2. Action / 3. Asstion
        assertThrows(EntityNotFoundException.class, () -> {
            propertyService.getPropertyById(1L);
        });
        verify(mockPropertyRepository, times(1)).findById(1L);
    }
    @Test
    public void testGetPropertyById_NullId(){
        assertThrows(IllegalArgumentException.class, () ->{
            propertyService.getPropertyById(null);
        });
        verify(mockPropertyRepository, never()).findById(null);
    }
    @Test
    public void createProperty_susseccfully(){
        // 1.
        Property request = new Property(
                                null,
                        "Finkenstrasse 10",
                        "Berlin",
                        59.00,
                        true);

        Property savedProperty = new Property(
                1L, "Finkenstrasse 10", "Berlin", 59.00, true);

        when(mockPropertyRepository.save(any(Property.class))).thenReturn(savedProperty);

        // 2.
        Property respone = propertyService.createNewProperty(request);

        // 3.
        assertNotNull(respone);
        assertEquals("Berlin", respone.getCity());
        assertEquals("Finkenstrasse 10", respone.getAdress());
        verify(mockPropertyRepository, times(1)).save(any(Property.class));
    }
    //Property
    @Test
    public void testDeletePropertyById_Successfully(){

        when(mockPropertyRepository.existsById(1L)).thenReturn(true);

        propertyService.deleteProperty(1L);

        verify(mockPropertyRepository, times(1)).deleteById(1L);
    }
    @Test
    public void testDeletePropertyById_BookDoesNotExist(){

        when(mockPropertyRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                propertyService.deleteProperty(1L));

        assertEquals("Property not found with id: " + 1L, exception.getMessage());
    }
    @Test
    public void testUpdatePropertyById_Successfully() {
        // 1. Arrange
        Long propertyId = 1L;
        Property request = new Property(1L, "Finkenstrasse 10", "Berlin", 59.00, true);
        Property existingProperty = new Property(propertyId,  "Finkenstrasse 12", "Berlin", 60.00, true);

        when(mockPropertyRepository.findById(propertyId)).thenReturn(Optional.of(existingProperty));
        when(mockPropertyRepository.save(any(Property.class))).thenReturn(existingProperty);

        // 2. Action
        Property response = propertyService.updateProperty(propertyId, request);

        // 3. Assert
        assertEquals("Finkenstrasse 10", response.getAdress());
        assertEquals(59.00, response.getPricePerNight());
        verify(mockPropertyRepository, times(1)).save(existingProperty);

    }
}
