package com.upao.insurApp.dto.payment;

import lombok.Data;

@Data
public class PaymentCaptureResponse {
    private boolean completed;
    private Integer purchaseId;
}
