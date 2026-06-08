package com.am37.tripCalc.controller;

import com.am37.tripCalc.model.Trip;
import com.am37.tripCalc.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TripController.class)
class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private TripService tripService;
    @MockitoBean private PersonService personService;
    @MockitoBean private ExpenseService expenseService;
    @MockitoBean private SettlementService settlementService;

    @Test
    void createTrip_shouldReturn200WithTripData() throws Exception {
        Trip trip = new Trip();
        trip.setId(1L);
        trip.setName("Paris Trip");

        when(tripService.createTrip(anyString())).thenReturn(trip);
        when(personService.getPeopleOnTrip(1L)).thenReturn(List.of());
        when(expenseService.getExpensesForTrip(1L)).thenReturn(List.of());

        mockMvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Paris Trip\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Paris Trip"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getAllTrips_shouldReturnEmptyList_whenNoTrips() throws Exception {
        when(tripService.getAllTrips()).thenReturn(List.of());

        mockMvc.perform(get("/api/trips"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getTripById_shouldReturnTrip() throws Exception {
        Trip trip = new Trip();
        trip.setId(1L);
        trip.setName("Paris Trip");

        when(tripService.getTripById(1L)).thenReturn(trip);
        when(personService.getPeopleOnTrip(1L)).thenReturn(List.of());
        when(expenseService.getExpensesForTrip(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/trips/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Paris Trip"));
    }
}