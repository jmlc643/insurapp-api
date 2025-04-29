package com.upao.insurApp.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ValidationErrorResponse {
    private Integer status;
    private String message;
    private Map<String, List<String>> errors;
}