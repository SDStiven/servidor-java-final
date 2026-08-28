package com.labanta.servidorlocal.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ServicoNaoEmcontradoException.class)
    public ResponseEntity<Map<String, String>> handleServicoNaoEncontradoException(ServicoNaoEmcontradoException ex) {
        // Enviar um aviso ao administradoe da platraforma
        log.warn("tentativa ao recursop inesistente:{}", ex.getMessage());

        Map<String, String> resposta = new HashMap<>();
        resposta.put("erro", "Recuso não encontrado");
        resposta.put("detalhes", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> tratarIllegalArgumentException(
            IllegalArgumentException ex) {

        Map<String, String> erro = new HashMap<>();

        erro.put("erro", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erro);
    }

    @ExceptionHandler(UtilizadorExistenteException.class)
    public ResponseEntity<Map<String, String>> tratarUtilizadorExistente(UtilizadorExistenteException ex) {

        Map<String, String> erro = new HashMap<>();

        erro.put("erro", "Username já está em uso");
        erro.put("detalhes", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erro);
    }

}
