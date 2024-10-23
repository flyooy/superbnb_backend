package de.supercode.superbnb;

import com.jayway.jsonpath.JsonPath;
import de.supercode.superbnb.entities.Property;
import de.supercode.superbnb.repositories.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@SpringBootTest
@AutoConfigureMockMvc
public class PropertyIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PropertyRepository propertyRepository;
    private static String adminToken;

    @BeforeEach
    public void setUp() throws Exception {

        // vor jedem Test DB aufräumen
        propertyRepository.deleteAll();
       Property property = new Property(null, "Finkenstrasse 10", "Berlin", 59.00, true);
        propertyRepository.save(property);

        if(adminToken == null){
            registerAdmin();
        }else{
            loginAdmin();
        }
    }

    private void registerAdmin() throws Exception {
        String registerJson = "{ \"email\" : \"admin@admin.de\" , \"password\" : \"qwerty\" , \"role\" : \"ADMIN\" }";

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isOk());

        loginAdmin();
    }

    private void loginAdmin() throws Exception {
        String username = "admin@admin.de";
        String password = "qwerty";
        String auth = username + ":" + password; // Authentifizierung formatieren

        String encodeAuth = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        MvcResult result = mockMvc.perform(post("/api/auth/signin")
                        .header("Authorization", encodeAuth)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Token extrahieren
        String jsonResponse = result.getResponse().getContentAsString();
        adminToken = JsonPath.parse(jsonResponse).read("$.token");
        assertNotNull(adminToken, "Admintoken darf nicht null sein ");
    }

    @Test
    public void getPropertyById_Successfulyy() throws Exception {
        Property property = propertyRepository.findAll().getFirst();

        mockMvc.perform(get("/api/properties/" + property.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adress").value("Finkenstrasse 10"));
    }
    @Test
    public void testGetPropertyById_NotFound() throws Exception {

        mockMvc.perform(get("/api/properties/" + 99L)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteProperty_Successfully() throws Exception {
        Property property = propertyRepository.findAll().getFirst();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/properties/" + property.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        assertEquals(0, propertyRepository.count());
    }
    @Test
    public void testDeleteProperty_NotFound() throws Exception {
        // Verwende eine nicht existierende Buch-ID
        Long nonExistentPropertyId = 999L; // Beispiel-ID, die nicht in der Datenbank vorhanden ist

        // Simuliert einen HTTP-DELETE-Request  mit dem Bearer-Token für die Autorisierung
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/properties/" + nonExistentPropertyId)
                        .header("Authorization", "Bearer " + adminToken)) // Füge den Bearer-Token hinzu
                .andExpect(status().isNotFound()); // Überprüft, dass der HTTP-Status 404 Not Found zurückgegeben wird
    }
    @Test
    public void testProperty_ReturnsCreatedProperty() throws Exception {

        Property property = new Property(null, "Finkenstrasse 10", "Berlin", 59.00, true);
        String requestJson = "{ \"adress\" : \"Finkenstrasse 10\" ,\"city\": \"Berlin\", \"pricePerNight\": 59, \"availability\": \"true\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/properties")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.adress").value("Finkenstrasse 10"));

        assertEquals(2, propertyRepository.count());
    }
    @Test
    public void testUpdateProperty() throws Exception {

        Property property = propertyRepository.findAll().getFirst();

        String updateRequestJson = "{ \"adress\" : \"Finkenstrasse 11\" ,\"city\": \"Berlin\", \"pricePerNight\": 59, \"availability\": \"true\" }";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/properties/" + property.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adress").value("Finkenstrasse 11"));
    }
    @Test
    public void testAccessSuperAdminEndpoit_AsAdmin() throws Exception {
        mockMvc.perform(get("/api/user/some-protected-resource")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isForbidden());
    }
    }
