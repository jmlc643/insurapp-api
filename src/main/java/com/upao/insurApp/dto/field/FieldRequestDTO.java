package com.upao.insurApp.dto.field;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldRequestDTO {
    private String description;
    private String typeField;
    private Integer numberField;
}