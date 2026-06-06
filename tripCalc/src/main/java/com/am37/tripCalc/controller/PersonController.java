package com.am37.tripCalc.controller;

import com.am37.tripCalc.dto.PersonRequestDTO;
import com.am37.tripCalc.dto.PersonResponseDTO;
import com.am37.tripCalc.dto.TripMapper;
import com.am37.tripCalc.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/trips/{tripId}/people")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    // POST /api/trips/{tripId}/people
    @PostMapping
    public ResponseEntity<PersonResponseDTO> addPerson(
            @PathVariable Long tripId,
            @RequestBody PersonRequestDTO request) {

        return ResponseEntity.ok(
                TripMapper.toPersonResponse(
                        personService.addPersonToTrip(tripId, request.getName())
                )
        );
    }

    // GET /api/trips/{tripId}/people
    @GetMapping
    public ResponseEntity<List<PersonResponseDTO>> getPeople(@PathVariable Long tripId) {
        List<PersonResponseDTO> people = personService.getPeopleOnTrip(tripId)
                .stream()
                .map(TripMapper::toPersonResponse)
                .toList();
        return ResponseEntity.ok(people);
    }
}