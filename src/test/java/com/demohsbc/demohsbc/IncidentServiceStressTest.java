package com.demohsbc.demohsbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTimeout;

public class IncidentServiceStressTest {

    private IncidentService incidentService;

    @BeforeEach
    public void setup() {
        incidentService = new IncidentServiceImplement();
    }

    @Test
    public void stressTestCreateIncidents() {
        assertTimeout(Duration.ofMinutes(1), () -> {
            for (int i = 0; i < 10000; i++) {
                Incident incident = new Incident("Title " + i, "Description " + i, "OPEN");
                incidentService.createIncident(incident);
            }
        });
    }

    @Test
    public void stressTestGetAllIncidents() {
        // Pre-populate the service with incidents
        for (int i = 0; i < 10000; i++) {
            Incident incident = new Incident("Title " + i, "Description " + i, "OPEN");
            incidentService.createIncident(incident);
        }

        assertTimeout(Duration.ofMinutes(1), () -> {
            for (int i = 0; i < 1000; i++) {
                incidentService.getAllIncidents();
            }
        });
    }

    @Test
    public void stressTestUpdateIncidents() {
        // Pre-populate the service with incidents
        for (int i = 0; i < 10000; i++) {
            Incident incident = new Incident("Title " + i, "Description " + i, "OPEN");
            incidentService.createIncident(incident);
        }

        assertTimeout(Duration.ofMinutes(1), () -> {
            for (int i = 0; i < 10000; i++) {
                UUID id = UUID.randomUUID();
                Incident updatedIncident = new Incident("Updated Title " + i, "Updated Description " + i, "CLOSED");
                try {
                    incidentService.updateIncident(id, updatedIncident);
                } catch (NoSuchElementException e) {
                    // Expected for some cases where UUIDs don't match
                }
            }
        });
    }

    @Test
    public void stressTestDeleteIncidents() {
        // Pre-populate the service with incidents
        for (int i = 0; i < 10000; i++) {
            Incident incident = new Incident("Title " + i, "Description " + i, "OPEN");
            incidentService.createIncident(incident);
        }

        assertTimeout(Duration.ofMinutes(1), () -> {
            for (int i = 0; i < 10000; i++) {
                UUID id = UUID.randomUUID();
                try {
                    incidentService.deleteIncident(id);
                } catch (IllegalArgumentException e) {
                    // Expected for some cases where UUIDs don't match
                }
            }
        });
    }
}
