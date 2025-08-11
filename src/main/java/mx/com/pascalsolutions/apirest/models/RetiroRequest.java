package mx.com.pascalsolutions.apirest.models;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class RetiroRequest {
    @NotNull(message = "La cantidad no puede ser nula.")
    @Positive(message = "La cantidad a retirar debe ser un valor positivo.")
    private BigDecimal cantidad;

    // Getters y Setters
    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }
}