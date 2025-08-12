package mx.com.pascalsolutions.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Esta clase intercepta excepciones lanzadas por los controladores
 * y las convierte en respuestas HTTP claras y consistentes para el cliente.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Maneja específicamente las excepciones de validación de los DTOs (ej. @NotNull, @Positive).
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        
        // Creamos un mapa para el cuerpo del error JSON
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        
        // Obtenemos todos los errores de validación y los formateamos en una lista
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        
        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);
    }
}