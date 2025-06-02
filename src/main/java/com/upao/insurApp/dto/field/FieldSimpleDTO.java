package com.upao.insurApp.dto.field;

import com.upao.insurApp.models.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldSimpleDTO {
    private Integer fieldId;
    private Integer price;
    private String typeField;
    private Integer numberField;

    public FieldSimpleDTO(Field field) {
        this.fieldId = field.getFieldId();
        this.price = field.getPrice();
        this.typeField = field.getTypeField();
        this.numberField = field.getNumberField();
    }
}
