package com.upao.insurApp.dto.stripe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StripeResponse {
    private String clientSecret;
    private String ephemeralSecret;
    private String id;
    private String paymentIntentId;
    private Integer paymentId;
}