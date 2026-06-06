package com.am37.tripCalc.dto;

import lombok.Data;
import java.util.List;

@Data
public class TripResponseDTO {
    private Long id;
    private String name;
    private List<PersonResponseDTO> people;
    private List<ExpenseResponseDTO> expenses;
}