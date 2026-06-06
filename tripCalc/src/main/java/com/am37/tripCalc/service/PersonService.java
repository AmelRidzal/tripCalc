package com.am37.tripCalc.service;

import com.am37.tripCalc.model.Person;
import com.am37.tripCalc.model.Trip;
import com.am37.tripCalc.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final TripService tripService;

    public Person addPersonToTrip(Long tripId, String name) {
        Trip trip = tripService.getTripById(tripId);
        Person person = new Person();
        person.setName(name);
        person.setTrip(trip);
        return personRepository.save(person);
    }

    public List<Person> getPeopleOnTrip(Long tripId) {
        return personRepository.findByTripId(tripId);
    }

    public Person getPersonById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));
    }
}