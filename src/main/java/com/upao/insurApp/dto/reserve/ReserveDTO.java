package com.upao.insurApp.dto.reserve;

import com.upao.insurApp.dto.field.FieldSimpleDTO;
import com.upao.insurApp.dto.payment.PaymentDTO;
import com.upao.insurApp.dto.user.ProfileDTO;
import com.upao.insurApp.models.Reserve;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReserveDTO {
    private LocalDate bookingDate;
    private LocalTime timetableStart;
    private LocalTime timetableEnd;
    private Integer totalPrice;
    private String qrUrl;
    private Boolean isValidated;

    private FieldSimpleDTO field;
    private ProfileDTO user;
    private List<PaymentDTO> payments;

    public ReserveDTO(Reserve reserve) {
        this.bookingDate = reserve.getBookingDate();
        this.timetableStart = reserve.getTimetableStart();
        this.timetableEnd = reserve.getTimetableEnd();
        this.totalPrice = reserve.getTotalPrice();
        this.qrUrl = reserve.getQrUrl();
        this.isValidated = reserve.getIsValidated();

        this.field = new FieldSimpleDTO(reserve.getField());

        this.user = new ProfileDTO(
                reserve.getUser().getName(),
                reserve.getUser().getSurname(),
                reserve.getUser().getEmail(),
                reserve.getUser().getPhone(),
                reserve.getUser().getDni()
        );

        this.payments = reserve.getPayments() != null
                ? reserve.getPayments().stream().map(PaymentDTO::new).collect(Collectors.toList())
                : Collections.emptyList();
    }

}
