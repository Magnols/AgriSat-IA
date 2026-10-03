package br.com.fiap.energia_esg_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarErroValidacao(MethodArgumentNotValidException ex) {
        Map<String, String> campos = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(erro -> campos.put(erro.getField(), erro.getDefaultMessage()));

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("status", 400);
        resposta.put("erro", "Erro de validacao");
        resposta.put("mensagem", "Existem campos invalidos na requisicao");
        resposta.put("campos", campos);
        resposta.put("dataHora", LocalDateTime.now());
        return ResponseEntity.badRequest().body(resposta);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> tratarErro404(ResourceNotFoundException ex) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("status", 404);
        resposta.put("erro", "Registro nao encontrado");
        resposta.put("mensagem", ex.getMessage());
        resposta.put("dataHora", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> tratarErroInterno(Exception ex) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("status", 500);
        resposta.put("erro", "Erro interno do servidor");
        resposta.put("mensagem", "Ocorreu um erro inesperado");
        resposta.put("dataHora", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resposta);
    }
}
