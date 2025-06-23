package com.upao.insurApp.services;

import com.upao.insurApp.dto.reserve.ReserveRequestDTO;
import com.upao.insurApp.models.Field;
import com.upao.insurApp.models.Reserve;
import com.upao.insurApp.models.User;
import com.upao.insurApp.models.enums.RStatus;
import com.upao.insurApp.repos.ReserveRepository;
import com.upao.insurApp.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReserveService {

    private final ReserveRepository reserveRepository;
    private final QrCodeService qrCodeService;
    @Value("${validate.reservation}")
    private String urlValidateReservation;

    @Autowired
    public ReserveService(ReserveRepository reserveRepository, QrCodeService qrCodeService, JwtUtils jwtUtils){
        this.reserveRepository = reserveRepository;
        this.qrCodeService = qrCodeService;
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

    // Map reserve
    public Reserve mapToReserve(ReserveRequestDTO dto, User user, Field field) {
        Reserve reserve = new Reserve();
        reserve.setBookingDate(dto.getBookingDate());
        reserve.setTimetableStart(dto.getTimetableStart());
        reserve.setTimetableEnd(dto.getTimetableEnd());
        reserve.setTotalPrice(dto.getTotalPrice());
        reserve.setUser(user);
        reserve.setField(field);
        return reserve;
    }

    // Create reserve and QR
    public Reserve createReservation(Reserve reserve) throws Exception {
        Reserve saved = reserveRepository.save(reserve);
        String qrData = urlValidateReservation + saved.getReserveId();
        String qrUrl = qrCodeService.generateQRAndUpload(qrData, "reserve_" + saved.getReserveId());
        saved.setQrUrl(qrUrl);
        Reserve updated = reserveRepository.save(saved);
        return updated;
    }

    // Validate reserve
    public Reserve validateReservation(Integer id) {
        Reserve reserve = reserveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // Ya estaba validada
        if (Boolean.TRUE.equals(reserve.getIsValidated())) {
            throw new RuntimeException("Esta reserva ya ha sido validada.");
        }

        reserve.setIsValidated(true);
        return reserveRepository.save(reserve);
    }

    public Void updateReservationStatus(Integer id, String status) {
        Reserve reserve = reserveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // Actualizar el estado de la reserva
        reserve.setStatus(RStatus.valueOf(status.toUpperCase()));
        reserveRepository.save(reserve);
        return null;
    }

}
