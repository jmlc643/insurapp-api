package com.upao.insurApp.dto.reserve;

import com.upao.insurApp.dto.field.FieldSimpleDTO;
import com.upao.insurApp.dto.payment.PaymentDTO;
import com.upao.insurApp.models.Reserve;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveUserDTO {
    private LocalDate bookingDate;
    private LocalTime timetableStart;
    private LocalTime timetableEnd;
    private Integer totalPrice;
    private String qrUrl;
    private Boolean isValidated;
    private String status;

    private FieldSimpleDTO field;
    private List<PaymentDTO> payments;

    public ReserveUserDTO(Reserve reserve) {
        this.bookingDate = reserve.getBookingDate();
        this.timetableStart = reserve.getTimetableStart();
        this.timetableEnd = reserve.getTimetableEnd();
        this.totalPrice = reserve.getTotalPrice();
        this.qrUrl = reserve.getQrUrl();
        this.isValidated = reserve.getIsValidated();
        this.status = reserve.getStatus().toString();
        this.field = new FieldSimpleDTO(reserve.getField());
        this.payments = reserve.getPayments() != null
                ? reserve.getPayments().stream().map(PaymentDTO::new).collect(Collectors.toList())
                : Collections.emptyList();
    }
}
