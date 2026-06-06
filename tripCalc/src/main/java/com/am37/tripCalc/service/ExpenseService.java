package com.am37.tripCalc.service;

import com.am37.tripCalc.model.Expense;
import com.am37.tripCalc.model.Person;
import com.am37.tripCalc.model.Trip;
import com.am37.tripCalc.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final TripService tripService;
    private final PersonService personService;

    public Expense addExpense(Long tripId, Long paidById, Double amount,
                              String description, List<Long> splitAmongIds) {

        Trip trip = tripService.getTripById(tripId);
        Person paidBy = personService.getPersonById(paidById);

        List<Person> splitAmong = splitAmongIds.stream()
                .map(personService::getPersonById)
                .toList();

        Expense expense = new Expense();
        expense.setTrip(trip);
        expense.setPaidBy(paidBy);
        expense.setAmount(amount);
        expense.setDescription(description);
        expense.setSplitAmong(splitAmong);

        return expenseRepository.save(expense);
    }

    public List<Expense> getExpensesForTrip(Long tripId) {
        return expenseRepository.findByTripId(tripId);
    }

    public List<Expense> getExpensesForPerson(Long personId) {
        return expenseRepository.findByPaidById(personId);
    }
}