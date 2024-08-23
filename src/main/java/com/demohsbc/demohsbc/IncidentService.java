package com.demohsbc.demohsbc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IncidentService {
    Incident createIncident(Incident incident);
    Optional<Incident> getIncidentById(UUID id);
    List<Incident> getAllIncidents();
    Incident updateIncident(UUID id, Incident incident);
    void deleteIncident(UUID id);
}
