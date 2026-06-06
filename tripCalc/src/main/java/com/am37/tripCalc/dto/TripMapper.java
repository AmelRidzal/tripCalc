package com.am37.tripCalc.dto;

import com.am37.tripCalc.model.Expense;
import com.am37.tripCalc.model.Person;
import com.am37.tripCalc.model.Trip;

import java.util.List;

public class TripMapper {

    public static PersonResponseDTO toPersonResponse(Person person) {
        PersonResponseDTO dto = new PersonResponseDTO();
        dto.setId(person.getId());
        dto.setName(person.getName());
        return dto;
    }

    public static ExpenseResponseDTO toExpenseResponse(Expense expense) {
        ExpenseResponseDTO dto = new ExpenseResponseDTO();
        dto.setId(expense.getId());
        dto.setDescription(expense.getDescription());
        dto.setAmount(expense.getAmount());
        dto.setPaidByName(expense.getPaidBy().getName());
        dto.setSplitAmongNames(
                expense.getSplitAmong().stream()
                        .map(Person::getName)
                        .toList()
        );
        return dto;
    }

    public static TripResponseDTO toTripResponse(Trip trip,
                                                 List<Person> people,
                                                 List<Expense> expenses) {
        TripResponseDTO dto = new TripResponseDTO();
        dto.setId(trip.getId());
        dto.setName(trip.getName());
        dto.setPeople(people.stream()
                .map(TripMapper::toPersonResponse)
                .toList());
        dto.setExpenses(expenses.stream()
                .map(TripMapper::toExpenseResponse)
                .toList());
        return dto;
    }
}