package com.upao.insurApp.dto.reserve;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReserveRequestDTO {
    private LocalDate bookingDate;
    private LocalTime timetableStart;
    private LocalTime timetableEnd;
    private Double price;
    private Integer userId;
    private Integer fieldId;

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

    public Integer getUserId() {
        return userId;
    }

    public Integer getFieldId() {
        return fieldId;
    }
}
