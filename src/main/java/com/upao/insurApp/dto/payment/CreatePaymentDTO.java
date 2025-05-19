package com.upao.insurApp.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreatePaymentDTO {
    private Double amount;
    private Integer reserveId;
    private String returnUrl;
    private String cancelUrl;
}
