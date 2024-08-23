package com.demohsbc.demohsbc;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IncidentServiceImplement implements IncidentService {
    private final Map<UUID, Incident> incidentStore = new HashMap<>();

    public Incident createIncident(Incident incident) {
        // Ensure a UUID is generated if not provided
        if (incident.getId() == null) {
            incident.setId(UUID.randomUUID());
        }

        if (incidentStore.containsKey(incident.getId())) {
            throw new IllegalArgumentException("Incident with this ID already exists");
        }

        for (Incident existingIncident : incidentStore.values()) {
            if (existingIncident.getTitle().equalsIgnoreCase(incident.getTitle())) {
                throw new IllegalArgumentException("Incident with this title already exists");
            }
        }

        incidentStore.put(incident.getId(), incident);
        return incident;
    }

    @Cacheable(value = "incidentCache", key = "#id")
    public Optional<Incident> getIncidentById(UUID id) {
        return Optional.ofNullable(incidentStore.get(id));
    }

    @Cacheable(value = "incidentCache", key = "#root.method.name")
    public List<Incident> getAllIncidents() {
        return new ArrayList<>(incidentStore.values());
    }

    public Incident updateIncident(UUID id, Incident incidentDetails) {
        Incident incident = incidentStore.get(id);
        if(incident != null) {
            incident.setTitle(incidentDetails.getTitle());
            incident.setDescription(incidentDetails.getDescription());
            incident.setStatus(incidentDetails.getStatus());
            incident.setUpdatedDate(incidentDetails.getUpdatedDate());
            incidentStore.put(id, incident);
            return incident;
        }
        else{
            throw new NoSuchElementException("Incident not found");
        }
    }


    public void deleteIncident(UUID id) {
        if (!incidentStore.containsKey(id)) {
            throw new IllegalArgumentException("Incident with ID " + id + " not found.");
        }
        incidentStore.remove(id);
    }
}
