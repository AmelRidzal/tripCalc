package com.am37.tripCalc.service;

import com.am37.tripCalc.model.Expense;
import com.am37.tripCalc.model.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final ExpenseService expenseService;
    private final PersonService personService;

    public List<String> calculateSettlement(Long tripId) {

        List<Person> people = personService.getPeopleOnTrip(tripId);
        List<Expense> expenses = expenseService.getExpensesForTrip(tripId);

        // Step 1: calculate net balance for each person
        // positive = they are owed money
        // negative = they owe money
        Map<Long, Double> balance = new HashMap<>();
        for (Person p : people) {
            balance.put(p.getId(), 0.0);
        }

        for (Expense e : expenses) {
            int splitCount = e.getSplitAmong().size();
            if (splitCount == 0) continue;

            double share = e.getAmount() / splitCount;

            // person who paid gets credited the full amount
            Long payerId = e.getPaidBy().getId();
            balance.put(payerId, balance.get(payerId) + e.getAmount());

            // each person in the split gets debited their share
            for (Person p : e.getSplitAmong()) {
                balance.put(p.getId(), balance.get(p.getId()) - share);
            }
        }

        // Step 2: settle up using a greedy algorithm
        // debtors = people with negative balance (owe money)
        // creditors = people with positive balance (are owed money)
        List<String> settlements = new ArrayList<>();
        Map<Long, String> nameMap = new HashMap<>();
        for (Person p : people) {
            nameMap.put(p.getId(), p.getName());
        }

        Queue<Map.Entry<Long, Double>> debtors = new PriorityQueue<>(
                Comparator.comparingDouble(Map.Entry::getValue)
        );
        Queue<Map.Entry<Long, Double>> creditors = new PriorityQueue<>(
                Comparator.<Map.Entry<Long, Double>>comparingDouble(Map.Entry::getValue).reversed()
        );

        for (Map.Entry<Long, Double> entry : balance.entrySet()) {
            if (entry.getValue() < -0.01) {
                debtors.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
            } else if (entry.getValue() > 0.01) {
                creditors.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
            }
        }

        // Step 3: match debtors to creditors
        while (!debtors.isEmpty() && !creditors.isEmpty()) {
            Map.Entry<Long, Double> debtor = debtors.poll();
            Map.Entry<Long, Double> creditor = creditors.poll();

            double owedAmount = Math.min(-debtor.getValue(), creditor.getValue());
            owedAmount = Math.round(owedAmount * 100.0) / 100.0;

            String msg = String.format("%s owes %s €%.2f",
                    nameMap.get(debtor.getKey()),
                    nameMap.get(creditor.getKey()),
                    owedAmount);
            settlements.add(msg);

            double newDebtorBalance = debtor.getValue() + owedAmount;
            double newCreditorBalance = creditor.getValue() - owedAmount;

            if (newDebtorBalance < -0.01) {
                debtors.add(new AbstractMap.SimpleEntry<>(debtor.getKey(), newDebtorBalance));
            }
            if (newCreditorBalance > 0.01) {
                creditors.add(new AbstractMap.SimpleEntry<>(creditor.getKey(), newCreditorBalance));
            }
        }

        if (settlements.isEmpty()) {
            settlements.add("Everyone is settled up!");
        }

        return settlements;
    }
}