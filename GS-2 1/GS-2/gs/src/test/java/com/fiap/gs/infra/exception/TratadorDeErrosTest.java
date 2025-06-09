package com.fiap.gs.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TratadorDeErrosTest {

    private final TratadorDeErros tratadorDeErros = new TratadorDeErros();

    @Test
    void testTratarErro404() {
        ResponseEntity<?> response = tratadorDeErros.TratarErro404();
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testTratarErro400() {
        FieldError fieldError = mock(FieldError.class);
        when(fieldError.getField()).thenReturn("campoTeste");
        when(fieldError.getDefaultMessage()).thenReturn("Mensagem de erro");

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<?> response = tratadorDeErros.TratarErro400(exception);
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());

        List<?> erros = (List<?>) response.getBody();
        assertEquals(1, erros.size());
        assertTrue(erros.get(0).toString().contains("campoTeste"));
        assertTrue(erros.get(0).toString().contains("Mensagem de erro"));
    }
}