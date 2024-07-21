package com.picpaysimplificado.dto;

import org.springframework.http.HttpStatus;

public record ExceptionDTO(String message, HttpStatus status) {
}
