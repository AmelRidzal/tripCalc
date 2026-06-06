package com.am37.tripCalc.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "people")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToMany(mappedBy = "splitAmong")
    private List<Expense> expenses = new ArrayList<>();
}