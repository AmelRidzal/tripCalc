package com.am37.tripCalc.controller;

import com.am37.tripCalc.dto.ExpenseRequestDTO;
import com.am37.tripCalc.dto.ExpenseResponseDTO;
import com.am37.tripCalc.dto.TripMapper;
import com.am37.tripCalc.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/trips/{tripId}/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    // POST /api/trips/{tripId}/expenses
    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> addExpense(
            @PathVariable Long tripId,
            @RequestBody ExpenseRequestDTO request) {

        return ResponseEntity.ok(
                TripMapper.toExpenseResponse(
                        expenseService.addExpense(
                                tripId,
                                request.getPaidById(),
                                request.getAmount(),
                                request.getDescription(),
                                request.getSplitAmongIds()
                        )
                )
        );
    }

    // GET /api/trips/{tripId}/expenses
    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> getExpenses(@PathVariable Long tripId) {
        List<ExpenseResponseDTO> expenses = expenseService.getExpensesForTrip(tripId)
                .stream()
                .map(TripMapper::toExpenseResponse)
                .toList();
        return ResponseEntity.ok(expenses);
    }

    // GET /api/trips/{tripId}/expenses/person/{personId}
    @GetMapping("/person/{personId}")
    public ResponseEntity<List<ExpenseResponseDTO>> getExpensesByPerson(
            @PathVariable Long tripId,
            @PathVariable Long personId) {

        List<ExpenseResponseDTO> expenses = expenseService.getExpensesForPerson(personId)
                .stream()
                .map(TripMapper::toExpenseResponse)
                .toList();
        return ResponseEntity.ok(expenses);
    }
}