package com.upao.insurApp.dto.payment;

import com.upao.insurApp.models.Payment;
import com.upao.insurApp.models.Reserve;
import com.upao.insurApp.models.enums.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    private Integer paymentId;
    private Integer amount;
    private LocalDateTime paymentDate;
    private PaymentStatus status;

    public PaymentDTO(Payment payment) {
        this.paymentId = payment.getPaymentId();
        this.amount = payment.getAmount();
        this.paymentDate = payment.getPaymentDate();
        this.status = payment.getStatus();
    }
}
