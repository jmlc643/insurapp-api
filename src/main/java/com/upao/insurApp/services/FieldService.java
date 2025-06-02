package com.upao.insurApp.services;

import com.upao.insurApp.dto.field.FieldDTO;
import com.upao.insurApp.models.Field;
import com.upao.insurApp.models.Reserve;
import com.upao.insurApp.repos.FieldRepository;
import com.upao.insurApp.repos.ReserveRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FieldService {

    private final FieldRepository fieldRepository;
    private final ReserveRepository reserveRepository;

    public FieldService(FieldRepository fieldRepository, ReserveRepository reserveRepository) {
        this.fieldRepository = fieldRepository;
        this.reserveRepository = reserveRepository;
    }

    public List<FieldDTO> getAllFields() {
        return fieldRepository.findAll().stream()
                .map(FieldDTO::new)
                .collect(Collectors.toList());
    }

    public List<Field> findByTypeField(String typeField) {
        return fieldRepository.findByTypeField(typeField);
    }

    public List<Reserve> getReserveByTypeFieldAndDate(Integer fieldId, LocalDate bookingDate) {
        return reserveRepository.findReserveByFieldAndDate(fieldId, bookingDate);
    }

    public List<String> getAvailableTimeSlots(Integer fieldId, LocalDate bookingDate) {
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(23, 0);

        List<Reserve> reservas = reserveRepository.findByFieldIdAndBookingDate(fieldId, bookingDate);

        // Horarios ocupados
        List<String> availableSlots = new ArrayList<>();
        for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusHours(1)) {
            final LocalTime slotStart = time;
            final LocalTime slotEnd = time.plusHours(1);

            boolean isOverlapping = reservas.stream().anyMatch(r ->
                    slotStart.isBefore(r.getTimetableEnd()) && slotEnd.isAfter(r.getTimetableStart())
            );

            if (!isOverlapping) {
                availableSlots.add(slotStart + " - " + slotEnd);
            }
        }
        return availableSlots;
    }

}
