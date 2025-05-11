package com.upao.insurApp.controllers;

import com.upao.insurApp.dto.reserve.ReserveRequestDTO;
import com.upao.insurApp.dto.reserve.ReserveResponseDTO;
import com.upao.insurApp.models.Field;
import com.upao.insurApp.models.Reserve;
import com.upao.insurApp.models.User;
import com.upao.insurApp.repos.FieldRepository;
import com.upao.insurApp.repos.ReserveRepository;
import com.upao.insurApp.repos.UserRepository;
import com.upao.insurApp.services.ReserveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
public class ReserveController {

    private final ReserveRepository reserveRepository;
    private final UserRepository userRepository;
    private final FieldRepository fieldRepository;
    private final ReserveService reserveService;

    public ReserveController(ReserveRepository reserveRepository, UserRepository userRepository, FieldRepository fieldRepository, ReserveService reserveService) {
        this.reserveRepository = reserveRepository;
        this.userRepository = userRepository;
        this.fieldRepository = fieldRepository;
        this.reserveService = reserveService;
    }

    // Lista la reserva por ID
    @GetMapping("/{reserveId}")
    public ResponseEntity<ReserveResponseDTO> getReserveById(@PathVariable Integer reserveId) {
        Optional<Reserve> reserve = reserveRepository.findById(reserveId);
        return reserve.map(r -> ResponseEntity.ok(new ReserveResponseDTO(r)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crea la reserva y se valida si hay o no disponibilidad para tal hora
    @PostMapping("/createReserve")
    public ResponseEntity<?> createReservation(@RequestBody ReserveRequestDTO dto) {
        Optional<User> userOpt = userRepository.findById(dto.getUserId());
        Optional<Field> fieldOpt = fieldRepository.findById(dto.getFieldId());

        if (userOpt.isEmpty() || fieldOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid user or field");
        }

        // Validar solapamiento de horarios
        if (!reserveService.isTimeSlotAvailable(dto.getFieldId(), dto.getBookingDate(), dto.getTimetableStart(), dto.getTimetableEnd())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Horario no disponible.");
        }

        Reserve reserve = new Reserve();
        reserve.setBookingDate(dto.getBookingDate());
        reserve.setPrice(dto.getPrice());
        reserve.setTimetableStart(dto.getTimetableStart());
        reserve.setTimetableEnd(dto.getTimetableEnd());
        reserve.setUser(userOpt.get());
        reserve.setField(fieldOpt.get());

        Reserve saved = reserveRepository.save(reserve);
        return ResponseEntity.ok(new ReserveResponseDTO(saved));
    }


}

