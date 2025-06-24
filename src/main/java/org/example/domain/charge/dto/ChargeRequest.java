package org.example.domain.charge.dto;

import lombok.Getter;

@Getter
public class ChargeRequest {
    private Long realAccountId;
    private int amount;
    private String description;
}
