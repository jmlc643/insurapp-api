package com.upao.insurApp.dto.field;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldRequestDTO {
    private Integer price;
    private String typeField;
    private Integer numberField;
}