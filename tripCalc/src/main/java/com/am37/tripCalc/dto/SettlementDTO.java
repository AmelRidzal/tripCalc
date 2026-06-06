package com.am37.tripCalc.dto;

import lombok.Data;
import java.util.List;

@Data
public class SettlementDTO {
    private Long tripId;
    private String tripName;
    private List<String> settlements;
}