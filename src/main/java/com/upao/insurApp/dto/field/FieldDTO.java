package com.upao.insurApp.dto.field;

import com.upao.insurApp.dto.reserve.ReserveDTO;
import com.upao.insurApp.models.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FieldDTO {
    private Integer fieldId;
    private Integer price;
    private String typeField;
    private Integer numberField;
    private List<ReserveDTO> reserves;

    public FieldDTO(Field field) {
        this.fieldId = field.getFieldId();
        this.price = field.getPrice();
        this.typeField = field.getTypeField();
        this.numberField = field.getNumberField();
        this.reserves = field.getReserves().stream()
                .map(ReserveDTO::new)
                .collect(Collectors.toList());
    }

}
