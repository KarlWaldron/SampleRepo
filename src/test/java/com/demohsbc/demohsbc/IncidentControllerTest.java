package com.demohsbc.demohsbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IncidentController.class)
public class IncidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IncidentService incidentService;

    private Incident incident;

    @BeforeEach
    public void setup() {
        incident = new Incident("Test Title", "Test Description", "OPEN");
    }

    @Test
    public void testCreateIncident() throws Exception {
        Mockito.when(incidentService.createIncident(any(Incident.class))).thenReturn(incident);

        mockMvc.perform(post("/api/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Test Title\", \"description\": \"Test Description\", \"status\": \"OPEN\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    public void testGetIncidentById() throws Exception {
        UUID id = incident.getId();
        Mockito.when(incidentService.getIncidentById(id)).thenReturn(Optional.of(incident));

        mockMvc.perform(get("/api/incidents/" + id.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    public void testGetAllIncidents() throws Exception {
        Mockito.when(incidentService.getAllIncidents()).thenReturn(Arrays.asList(incident));

        mockMvc.perform(get("/api/incidents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Title"))
                .andExpect(jsonPath("$[0].description").value("Test Description"))
                .andExpect(jsonPath("$[0].status").value("OPEN"));
    }

    @Test
    public void testUpdateIncident() throws Exception {
        UUID id = incident.getId();
        Incident updatedIncident = new Incident("Updated Title", "Updated Description", "CLOSED");
        Mockito.when(incidentService.updateIncident(any(UUID.class), any(Incident.class))).thenReturn(updatedIncident);

        mockMvc.perform(put("/api/incidents/" + id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Updated Title\", \"description\": \"Updated Description\", \"status\": \"CLOSED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.status").value("CLOSED"));
    }

    @Test
    public void testDeleteIncident() throws Exception {
        UUID id = incident.getId();
        Mockito.doNothing().when(incidentService).deleteIncident(id);

        mockMvc.perform(delete("/api/incidents/" + id.toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testCreateIncidentDuplicateTitle() {
        IncidentService incidentService = new IncidentServiceImplement();
        Incident incident1 = new Incident("Duplicate Title", "Description 1", "OPEN");
        Incident incident2 = new Incident("Duplicate Title", "Description 2", "OPEN");

        incidentService.createIncident(incident1);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            incidentService.createIncident(incident2);
        });

        assertEquals("Incident with this title already exists", exception.getMessage());
    }

    @Test
    public void testDeleteNonExistentIncident() {
        IncidentService incidentService = new IncidentServiceImplement();
        UUID nonExistentId = UUID.randomUUID();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            incidentService.deleteIncident(nonExistentId);
        });

        assertEquals("Incident with ID " + nonExistentId + " not found.", exception.getMessage());
    }

    @Test
    public void testIncidentServiceCreateIncident() {
        Incident incident = new Incident("Test Title", "Test Description", "OPEN");
        IncidentService incidentService = new IncidentServiceImplement();
        Incident createdIncident = incidentService.createIncident(incident);
        assertNotNull(createdIncident);
        assertEquals("Test Title", createdIncident.getTitle());
        assertNotNull(createdIncident.getId()); // Ensure the ID is set
    }

    @Test
    public void testCreateIncidentDirectly() {
        Incident incident = new Incident("Test Title", "Test Description", "OPEN");

        // Use the real service implementation
        IncidentService incidentService = new IncidentServiceImplement();

        IncidentController incidentController = new IncidentController(incidentService);
        ResponseEntity<Incident> response = incidentController.createIncident(incident);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody()); // Check that the body is not null
        assertEquals(incident.getTitle(), response.getBody().getTitle());
    }

    @Test
    public void testCreateIncidentDirectlyWithMock() {
        Incident incident = new Incident("Test Title", "Test Description", "OPEN");

        // Mock the service to return the same incident
        Mockito.when(incidentService.createIncident(any(Incident.class))).thenReturn(incident);

        IncidentController incidentController = new IncidentController(incidentService);
        ResponseEntity<Incident> response = incidentController.createIncident(incident);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody()); // Check that the body is not null
        assertEquals("Test Title", response.getBody().getTitle());
    }
}
