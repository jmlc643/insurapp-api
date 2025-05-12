package com.upao.insurApp.dto.field;

import com.upao.insurApp.models.Field;

public class FieldResponseDTO {
    private final Integer fieldId;
    private final Integer price;
    private final String typeField;
    private final Integer numberField;

    public FieldResponseDTO(Field field) {
        this.fieldId = field.getFieldId();
        this.price = field.getPrice();
        this.typeField = field.getTypeField();
        this.numberField = field.getNumberField();
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public Integer getPrice() {
        return price;
    }

    public String getTypeField() {
        return typeField;
    }

    public Integer getNumberField() {
        return numberField;
    }
}

