package com.upao.insurApp.services;

import com.upao.insurApp.dto.field.AvailableTimeDTO;
import com.upao.insurApp.dto.field.FieldDTO;
import com.upao.insurApp.dto.field.FieldResponseDTO;
import com.upao.insurApp.exceptions.ResourceNotExistsException;
import com.upao.insurApp.models.Field;
import com.upao.insurApp.models.Reserve;
import com.upao.insurApp.models.User;
import com.upao.insurApp.repos.FieldRepository;
import com.upao.insurApp.repos.ReserveRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public List<AvailableTimeDTO> getAvailableTimeSlots(Integer fieldId, LocalDate bookingDate) {
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(23, 0);

        List<Reserve> reserves = reserveRepository.findByFieldIdAndBookingDate(fieldId, bookingDate);

        // Horarios Ocupados
        List<AvailableTimeDTO> availableSlots = new ArrayList<>();
        for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusHours(1)) {
            final LocalTime slotStart = time;
            final LocalTime slotEnd = time.plusHours(1);

            Optional<Reserve> overlappingReserve = reserves.stream()
                    .filter(r -> slotStart.isBefore(r.getTimetableEnd()) && slotEnd.isAfter(r.getTimetableStart()))
                    .findFirst();

            if (overlappingReserve.isPresent()) {
                User user = overlappingReserve.get().getUser();
                availableSlots.add(new AvailableTimeDTO(slotStart + " - " + slotEnd, true, user.getName() + " " + user.getSurname()));
            } else {
                availableSlots.add(new AvailableTimeDTO(slotStart + " - " + slotEnd, false, null));
            }
        }
        return availableSlots;
    }

    public FieldResponseDTO getField(int id) {
        Field field = fieldRepository.findById(id).orElseThrow(() -> new ResourceNotExistsException("El campo no existe"));
        return new FieldResponseDTO(field);
    }

}
