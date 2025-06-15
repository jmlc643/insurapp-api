package com.upao.insurApp.dto.reserve;

import com.upao.insurApp.dto.field.FieldResponseDTO;
import com.upao.insurApp.models.Reserve;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReserveResponseDTO {
    private Integer reserveId;
    private LocalDate bookingDate;
    private LocalTime timetableStart;
    private LocalTime timetableEnd;
    private Integer totalPrice;
    private FieldResponseDTO fieldId;
    private Integer userId;
    private String qrUrl;
    private Boolean isValidated;
    private String status;

    public ReserveResponseDTO(Reserve reserve) {
        this.reserveId = reserve.getReserveId();
        this.bookingDate = reserve.getBookingDate();
        this.timetableStart = reserve.getTimetableStart();
        this.timetableEnd = reserve.getTimetableEnd();
        this.totalPrice = reserve.getTotalPrice();
        this.fieldId = new FieldResponseDTO(reserve.getField());
        this.userId = reserve.getUser().getUserId();
        this.qrUrl = reserve.getQrUrl();
        this.isValidated = reserve.getIsValidated();
        this.status = reserve.getStatus().toString();
    }

    public Integer getReserveId() {return reserveId;}

    public LocalDate getBookingDate() {return bookingDate;}

    public LocalTime getTimetableStart() {return timetableStart;}

    public LocalTime getTimetableEnd() {return timetableEnd;}

    public Integer getTotalPrice() {return totalPrice;}

    public FieldResponseDTO getFieldId() {return fieldId;}

    public Integer getUserId() {return userId;}

    public String getQrUrl() {return qrUrl;}

    public Boolean getIsValidated() {return isValidated;}

    public String getStatus() {return status;}
}

