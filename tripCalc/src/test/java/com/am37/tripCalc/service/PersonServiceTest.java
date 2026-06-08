package com.am37.tripCalc.service;

import com.am37.tripCalc.model.Person;
import com.am37.tripCalc.model.Trip;
import com.am37.tripCalc.repository.PersonRepository;
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
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private TripService tripService;

    @InjectMocks
    private PersonService personService;

    @Test
    void addPersonToTrip_shouldSavePersonWithTrip() {
        Trip trip = new Trip();
        trip.setId(1L);
        trip.setName("Paris Trip");

        Person saved = new Person();
        saved.setId(1L);
        saved.setName("Alice");
        saved.setTrip(trip);

        when(tripService.getTripById(1L)).thenReturn(trip);
        when(personRepository.save(any(Person.class))).thenReturn(saved);

        Person result = personService.addPersonToTrip(1L, "Alice");

        assertEquals("Alice", result.getName());
        assertEquals(1L, result.getTrip().getId());
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void getPersonById_shouldThrow_whenNotFound() {
        when(personRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> personService.getPersonById(99L));

        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    void getPeopleOnTrip_shouldReturnPeopleForTrip() {
        Person p1 = new Person(); p1.setName("Alice");
        Person p2 = new Person(); p2.setName("Bob");

        when(personRepository.findByTripId(1L)).thenReturn(List.of(p1, p2));

        List<Person> result = personService.getPeopleOnTrip(1L);

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
    }
}