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
    private Double price;
    private FieldResponseDTO fieldId;
    private Integer userId;

    public ReserveResponseDTO(Reserve reserve) {
        this.reserveId = reserve.getReserveId();
        this.bookingDate = reserve.getBookingDate();
        this.timetableStart = reserve.getTimetableStart();
        this.timetableEnd = reserve.getTimetableEnd();
        this.price = reserve.getTotalPrice();
        this.fieldId = new FieldResponseDTO(reserve.getField());
        this.userId = reserve.getUser().getUserId();
    }

    public Integer getReserveId() {
        return reserveId;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public LocalTime getTimetableStart() {
        return timetableStart;
    }

    public LocalTime getTimetableEnd() {
        return timetableEnd;
    }

    public Double getPrice() {
        return price;
    }

    public FieldResponseDTO getFieldId() {
        return fieldId;
    }

    public Integer getUserId() {
        return userId;
    }
}

