package com.upao.insurApp.dto.field;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AvailableTimeDTO {
    private String time;
    private boolean isReserved;
    private String client;
}
