package com.picpaysimplificado.infra;

import com.picpaysimplificado.dto.ExceptionDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionDTO> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDTO> entityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> exception(Exception e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.internalServerError().body(exceptionDTO);
    }
}
