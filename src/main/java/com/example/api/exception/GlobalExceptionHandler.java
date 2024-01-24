package com.example.api.exception;

import com.example.api.model.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
    log.error("Erro de validação", ex);

    Map<String, String> errors = new HashMap<>();
    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      String field = getFieldFromPath(violation.getPropertyPath());
      String message = violation.getMessage();

      // Personaliza a mensagem de erro para o campo 'zipCode'
      if ("zipCode".equals(field)) {
        message = "O campo 'zipCode' não atende aos requisitos de validação.";
      }

      errors.put(field, message);
    }

    String errorMessage = errors.toString();  // Convertendo o Map para uma representação de string

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        errorMessage
    );

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    BindingResult bindingResult = ex.getBindingResult();
    Map<String, String> errors = new HashMap<>();

    for (FieldError fieldError : bindingResult.getFieldErrors()) {
      errors.put(fieldError.getField(), fieldError.getDefaultMessage());
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler(WebClientResponseException.class)
  public ResponseEntity<ErrorResponse> handleWebClientResponseException(WebClientResponseException ex) {
    log.error("Erro na chamada do WebClient", ex);

    // Verificamos se o código de status é 400 (Bad Request)
    if (ex.getRawStatusCode() == HttpStatus.BAD_REQUEST.value()) {
      ErrorResponse errorResponse = new ErrorResponse(
          HttpStatus.BAD_REQUEST.value(),
          HttpStatus.BAD_REQUEST.getReasonPhrase(),
          "Requisição inválida. Verifique os parâmetros fornecidos."
      );

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Tratar outros códigos de status, se necessário

    // Caso não seja um erro 400, podemos retornar um erro genérico
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        "Ocorreu um erro interno no servidor. Por favor, tente novamente mais tarde ou entre em contato com o suporte."
    );

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {

    log.error("Ocorreu um erro interno no servidor", e);

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        "Ocorreu um erro interno no servidor. Por favor, tente novamente mais tarde ou entre em contato com o suporte."
    );

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  private String getFieldFromPath(Path path) {
    // Implemente esta lógica para obter o nome do campo a partir do caminho
    // Normalmente, a última parte do caminho representa o nome do campo
    // Exemplo: path: property.subproperty.field
    // Resultado esperado: field
    // ...
    return path.toString();  // Este é um exemplo simples, ajuste conforme necessário
  }
}
