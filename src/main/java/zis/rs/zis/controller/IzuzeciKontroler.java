package zis.rs.zis.controller;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import zis.rs.zis.util.KonekcijaSaBazomIzuzetak;
import zis.rs.zis.util.TransformacioniIzuzetak;
import zis.rs.zis.util.ValidacioniIzuzetak;

/**
 * Opsluzuje REST izuzetke
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class IzuzeciKontroler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(KonekcijaSaBazomIzuzetak.class)
    public ResponseEntity<String> opsluziKonekcijuSBazomIzuzetak(KonekcijaSaBazomIzuzetak e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ValidacioniIzuzetak.class)
    public ResponseEntity<String> opsluziValidacioniIzuzetak(ValidacioniIzuzetak e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransformacioniIzuzetak.class)
    public ResponseEntity<String> opsluziTransformacioniIzuzetak(TransformacioniIzuzetak e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
