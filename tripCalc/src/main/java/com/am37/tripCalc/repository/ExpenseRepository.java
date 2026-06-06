package com.am37.tripCalc.repository;

import com.am37.tripCalc.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByTripId(Long tripId);
    List<Expense> findByPaidById(Long personId);
}