package com.am37.tripCalc.dto;

import lombok.Data;
import java.util.List;

@Data
public class ExpenseResponseDTO {
    private Long id;
    private String description;
    private Double amount;
    private String paidByName;
    private List<String> splitAmongNames;
}