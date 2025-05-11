package com.upao.insurApp.services;

import com.upao.insurApp.models.Reserve;
import com.upao.insurApp.repos.ReserveRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReserveService {

    private final ReserveRepository reserveRepository;

    private ReserveService(ReserveRepository reserveRepository){
        this.reserveRepository = reserveRepository;
    }

    public boolean isTimeSlotAvailable(Integer fieldId, LocalDate bookingDate, LocalTime timetableStart, LocalTime timetableEnd) {
        List<Reserve> reserve = reserveRepository.findByFieldIdAndBookingDate(fieldId, bookingDate);

        for (Reserve r : reserve) {
            if (timetableStart.isBefore(r.getTimetableEnd()) && timetableEnd.isAfter(r.getTimetableStart())) {
                return false;
            }
        }
        return true;
    }

}
