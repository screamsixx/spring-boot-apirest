package mx.com.pascalsolutions.apirest.models;

import java.math.BigDecimal;

public class DesgloseItem {
    private BigDecimal denominacion;
    private String descripcion;
    private int cantidad;

    // Constructor, Getters y Setters...
    public DesgloseItem(BigDecimal denominacion, String descripcion, int cantidad) {
        this.denominacion = denominacion;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
    }
    
    public BigDecimal getDenominacion() { return denominacion; }
    public void setDenominacion(BigDecimal denominacion) { this.denominacion = denominacion; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
