package com.am37.tripCalc.service;

import com.am37.tripCalc.model.Trip;
import com.am37.tripCalc.repository.TripRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @InjectMocks
    private TripService tripService;

    @Test
    void createTrip_shouldSaveAndReturnTrip() {
        Trip saved = new Trip();
        saved.setId(1L);
        saved.setName("Paris Trip");

        when(tripRepository.save(any(Trip.class))).thenReturn(saved);

        Trip result = tripService.createTrip("Paris Trip");

        assertEquals("Paris Trip", result.getName());
        assertEquals(1L, result.getId());
        verify(tripRepository, times(1)).save(any(Trip.class));
    }

    @Test
    void getTripById_shouldReturnTrip_whenExists() {
        Trip trip = new Trip();
        trip.setId(1L);
        trip.setName("Paris Trip");

        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        Trip result = tripService.getTripById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Paris Trip", result.getName());
    }

    @Test
    void getTripById_shouldThrow_whenNotFound() {
        when(tripRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> tripService.getTripById(99L));

        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    void getAllTrips_shouldReturnList() {
        Trip t1 = new Trip(); t1.setName("Paris");
        Trip t2 = new Trip(); t2.setName("Rome");

        when(tripRepository.findAll()).thenReturn(List.of(t1, t2));

        List<Trip> result = tripService.getAllTrips();

        assertEquals(2, result.size());
    }
}