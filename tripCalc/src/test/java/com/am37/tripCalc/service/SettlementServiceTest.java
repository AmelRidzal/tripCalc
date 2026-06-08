package com.am37.tripCalc.service;

import com.am37.tripCalc.model.Expense;
import com.am37.tripCalc.model.Person;
import com.am37.tripCalc.model.Trip;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SettlementServiceTest {

    @Mock
    private ExpenseService expenseService;

    @Mock
    private PersonService personService;

    @InjectMocks
    private SettlementService settlementService;

    // helpers
    private Person makePerson(Long id, String name) {
        Person p = new Person();
        p.setId(id);
        p.setName(name);
        return p;
    }

    private Expense makeExpense(Person paidBy, double amount, List<Person> splitAmong) {
        Expense e = new Expense();
        e.setPaidBy(paidBy);
        e.setAmount(amount);
        e.setSplitAmong(splitAmong);
        return e;
    }

    @Test
    void settlement_evenSplit_oneOwes() {
        // Alice paid €60, split among Alice + Bob + Carol equally (€20 each)
        // Alice is owed €40, Bob owes €20, Carol owes €20
        Person alice = makePerson(1L, "Alice");
        Person bob   = makePerson(2L, "Bob");
        Person carol = makePerson(3L, "Carol");

        Expense dinner = makeExpense(alice, 60.0, List.of(alice, bob, carol));

        when(personService.getPeopleOnTrip(1L)).thenReturn(List.of(alice, bob, carol));
        when(expenseService.getExpensesForTrip(1L)).thenReturn(List.of(dinner));

        List<String> result = settlementService.calculateSettlement(1L);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(s -> s.contains("Bob") && s.contains("Alice")));
        assertTrue(result.stream().anyMatch(s -> s.contains("Carol") && s.contains("Alice")));
    }

    @Test
    void settlement_alreadyEven_returnsSettledMessage() {
        // Alice paid €30, Bob paid €30, each pays for themselves only
        Person alice = makePerson(1L, "Alice");
        Person bob   = makePerson(2L, "Bob");

        Expense e1 = makeExpense(alice, 30.0, List.of(alice));
        Expense e2 = makeExpense(bob,   30.0, List.of(bob));

        when(personService.getPeopleOnTrip(1L)).thenReturn(List.of(alice, bob));
        when(expenseService.getExpensesForTrip(1L)).thenReturn(List.of(e1, e2));

        List<String> result = settlementService.calculateSettlement(1L);

        assertEquals(1, result.size());
        assertEquals("Everyone is settled up!", result.get(0));
    }

    @Test
    void settlement_multipleExpenses_correctDebts() {
        // Alice paid €90 for all 3, Bob paid €30 for himself only
        // Total: Alice paid €90, Bob paid €30
        // Each person's fair share of €120 total = €40
        // Alice: paid €90, owes €40 → net +€50
        // Bob:   paid €30, owes €40 → net -€10
        // Carol: paid €0,  owes €40 → net -€40
        Person alice = makePerson(1L, "Alice");
        Person bob   = makePerson(2L, "Bob");
        Person carol = makePerson(3L, "Carol");

        Expense e1 = makeExpense(alice, 90.0, List.of(alice, bob, carol));
        Expense e2 = makeExpense(bob,   30.0, List.of(bob));

        when(personService.getPeopleOnTrip(1L)).thenReturn(List.of(alice, bob, carol));
        when(expenseService.getExpensesForTrip(1L)).thenReturn(List.of(e1, e2));

        List<String> result = settlementService.calculateSettlement(1L);

        assertFalse(result.isEmpty());
        assertNotEquals("Everyone is settled up!", result.get(0));
    }

    @Test
    void settlement_noExpenses_returnsSettledMessage() {
        Person alice = makePerson(1L, "Alice");
        Person bob   = makePerson(2L, "Bob");

        when(personService.getPeopleOnTrip(1L)).thenReturn(List.of(alice, bob));
        when(expenseService.getExpensesForTrip(1L)).thenReturn(List.of());

        List<String> result = settlementService.calculateSettlement(1L);

        assertEquals("Everyone is settled up!", result.get(0));
    }
}