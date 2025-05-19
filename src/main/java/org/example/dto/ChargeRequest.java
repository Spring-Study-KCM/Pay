package org.example.dto;

import lombok.Getter;

@Getter
public class ChargeRequest {
    private Long realAccountId;
    private int amount;
    private String description;
}
