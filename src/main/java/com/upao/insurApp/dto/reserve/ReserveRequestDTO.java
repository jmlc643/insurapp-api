package com.upao.insurApp.dto.reserve;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReserveRequestDTO {
    private LocalDate bookingDate;
    private LocalTime timetableStart;
    private LocalTime timetableEnd;
    private Integer totalPrice;
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

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Integer getFieldId() {
        return fieldId;
    }
}
