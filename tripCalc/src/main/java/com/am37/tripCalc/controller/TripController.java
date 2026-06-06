package com.am37.tripCalc.controller;

import com.am37.tripCalc.dto.*;
import com.am37.tripCalc.model.Trip;
import com.am37.tripCalc.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;
    private final PersonService personService;
    private final ExpenseService expenseService;
    private final SettlementService settlementService;

    // POST /api/trips
    @PostMapping
    public ResponseEntity<TripResponseDTO> createTrip(@RequestBody TripRequestDTO request) {
        Trip trip = tripService.createTrip(request.getName());
        TripResponseDTO response = TripMapper.toTripResponse(trip,
                personService.getPeopleOnTrip(trip.getId()),
                expenseService.getExpensesForTrip(trip.getId()));
        return ResponseEntity.ok(response);
    }

    // GET /api/trips
    @GetMapping
    public ResponseEntity<List<TripResponseDTO>> getAllTrips() {
        List<TripResponseDTO> trips = tripService.getAllTrips().stream()
                .map(trip -> TripMapper.toTripResponse(trip,
                        personService.getPeopleOnTrip(trip.getId()),
                        expenseService.getExpensesForTrip(trip.getId())))
                .toList();
        return ResponseEntity.ok(trips);
    }

    // GET /api/trips/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TripResponseDTO> getTripById(@PathVariable Long id) {
        Trip trip = tripService.getTripById(id);
        TripResponseDTO response = TripMapper.toTripResponse(trip,
                personService.getPeopleOnTrip(id),
                expenseService.getExpensesForTrip(id));
        return ResponseEntity.ok(response);
    }

    // GET /api/trips/{id}/settlement
    @GetMapping("/{id}/settlement")
    public ResponseEntity<SettlementDTO> getSettlement(@PathVariable Long id) {
        Trip trip = tripService.getTripById(id);
        SettlementDTO dto = new SettlementDTO();
        dto.setTripId(trip.getId());
        dto.setTripName(trip.getName());
        dto.setSettlements(settlementService.calculateSettlement(id));
        return ResponseEntity.ok(dto);
    }
}