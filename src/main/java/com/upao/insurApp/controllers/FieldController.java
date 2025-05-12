package com.upao.insurApp.controllers;

import com.upao.insurApp.dto.field.FieldRequestDTO;
import com.upao.insurApp.dto.field.FieldResponseDTO;
import com.upao.insurApp.models.Field;
import com.upao.insurApp.models.Reserve;
import com.upao.insurApp.repos.FieldRepository;
import com.upao.insurApp.services.FieldService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/fields")
public class FieldController {

    private final FieldService fieldService;
    private final FieldRepository fieldRepository;

    public FieldController(FieldService fieldService, FieldRepository fieldRepository) {
        this.fieldService = fieldService;
        this.fieldRepository = fieldRepository;
    }

    // Crear un campo
    @PostMapping("/createField")
    public ResponseEntity<Field> createField(@RequestBody FieldRequestDTO dto) {
        Field field = new Field();
        field.setDescription(dto.getDescription());
        field.setTypeField(dto.getTypeField());
        field.setNumberField(dto.getNumberField());

        return ResponseEntity.ok(fieldRepository.save(field));
    }

    // Se lista las canchas deportivas
    @GetMapping("/search")
    public ResponseEntity<List<Field>> getAllFields() {
        List<Field> fields = fieldService.getAllFields();
        return ResponseEntity.ok(fields);
    }

    // Buscar campos por tipo
    @GetMapping("/searchTypeField")
    public ResponseEntity<List<Field>> buscarFieldsPorTipo(@RequestParam String typeField) {
        return ResponseEntity.ok(fieldService.findByTypeField(typeField));
    }

    // Obtener horarios de reservas por campo y fecha
    @GetMapping("/{fieldId}/reserves")
    public ResponseEntity<List<Reserve>> obtenerHorarios(
            @PathVariable Integer fieldId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bookingDate) {

        return ResponseEntity.ok(fieldService.getReserveByTypeFieldAndDate(fieldId, bookingDate));
    }

    // Mostrar horarios disponibles para un campo en específico de una fecha
    @GetMapping("/{fieldId}/available-times")
    public ResponseEntity<List<String>> getAvailableTimes(
            @PathVariable Integer fieldId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bookingDate) {

        return ResponseEntity.ok(fieldService.getAvailableTimeSlots(fieldId, bookingDate));
    }

}
