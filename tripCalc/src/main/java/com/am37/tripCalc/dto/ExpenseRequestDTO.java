package com.am37.tripCalc.dto;

import lombok.Data;
import java.util.List;

@Data
public class ExpenseRequestDTO {
    private Long paidById;
    private Double amount;
    private String description;
    private List<Long> splitAmongIds;
}