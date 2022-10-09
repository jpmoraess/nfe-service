package br.com.moraesit.nfeservice.api.exceptionhandler;

import br.com.moraesit.nfeservice.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErro> handleBusinessException(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiErro.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .mensagem(ex.getMessage())
                        .build());
    }
}
