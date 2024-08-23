package com.demohsbc.demohsbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {
    private final IncidentService incidentService;

    @Autowired
    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @PostMapping
    public ResponseEntity<Incident> createIncident(@RequestBody Incident incident) {
        try {
            Incident createdIncident = incidentService.createIncident(incident);
            return new ResponseEntity<>(createdIncident, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    public ResponseEntity<List<Incident>> getAllIncidents(){
        List<Incident> incidents = incidentService.getAllIncidents();
        return new ResponseEntity<>(incidents, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Incident> getIncidentById(@PathVariable UUID id) {
        Optional<Incident> incident = incidentService.getIncidentById(id);
        return incident.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Incident> updateIncident(@PathVariable UUID id, @RequestBody Incident incidentDetails){
        try{
            Incident updatedIncident = incidentService.updateIncident(id, incidentDetails);
            return new ResponseEntity<>(updatedIncident, HttpStatus.OK);
        } catch(NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable UUID id){
        try {
            incidentService.deleteIncident(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
