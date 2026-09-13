package com.matheus.dividasAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DividaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDividaNotFound(DividaNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        404,
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(DividaUpdateException.class)
    public ResponseEntity<ErrorResponse> handleDividaUpdate(DividaUpdateException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        409,
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex){
        String message = ex.getBindingResult().getFieldErrors() // *pega todos os campos que falharam na validação.
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())// converte os erros em texto.
                .collect(Collectors.joining("; ")); // junta tudo em uma única mensagem.

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        404,
                        message,
                        LocalDateTime.now()
                ));
    }

    // * Exemplo
    // cpfDevedor → deve não estar em branco
    // valorPego → deve ser maior que 0
    // valorComJuros → não deve ser nulo
}
