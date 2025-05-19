package com.upao.insurApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reserves")
public class Reserve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserve_id")
    private Integer ReserveId;

    @Column(name = "booking_date")
    private LocalDate bookingDate;

    @Column(name = "timetable_start")
    private LocalTime timetableStart;

    @Column(name = "timetable_end")
    private LocalTime timetableEnd;

    @Column(name = "totalPrice")
    private Integer totalPrice;

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setTimetableStart(LocalTime timetableStart) {
        this.timetableStart = timetableStart;
    }

    public void setTimetableEnd(LocalTime timetableEnd) {
        this.timetableEnd = timetableEnd;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "field_id")
    @JsonBackReference
    private Field field;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "reserve", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();

}