package br.edu.atitus.geopoint.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ErroResposta(int status, String mensagem) { }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErroResposta> tratarNaoEncontrado(NotFoundException ex) {
        return montar(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErroResposta> tratarRequisicaoInvalida(BadRequestException ex) {
        return montar(HttpStatus.BAD_REQUEST, ex.getMessage());
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResposta> tratarJsonInvalido(HttpMessageNotReadableException ex) {
        return montar(HttpStatus.BAD_REQUEST,
          "Corpo da requisição inválido. Verifique o JSON e se latitude e longitude são números.");
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResposta> tratarIdInvalido(MethodArgumentTypeMismatchException ex) {
        return montar(HttpStatus.BAD_REQUEST, "O ID informado não é um UUID válido.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> tratarErroInesperado(Exception ex) {

        if (ex instanceof ErrorResponse erro) {
            return ResponseEntity.status(erro.getStatusCode())
                    .body(new ErroResposta(erro.getStatusCode().value(), ex.getMessage()));
        }
        return montar(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno inesperado.");
    }

    private ResponseEntity<ErroResposta> montar(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(new ErroResposta(status.value(), mensagem));
    }
}