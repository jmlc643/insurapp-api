package com.upao.insurApp.dto.stripe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveRequest {
    private String clientName;
    private String phone;
    private String email;
    private Long amount;
    private String productName;
}
