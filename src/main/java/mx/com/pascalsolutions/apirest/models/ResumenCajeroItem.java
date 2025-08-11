package mx.com.pascalsolutions.apirest.models;

import java.math.BigDecimal;

public class ResumenCajeroItem {
    private String descripcionDenominacion;
    private BigDecimal valorMonetario;
    // CAMBIO: Se usa Integer en lugar de int para poder aceptar valores nulos (para la fila del total).
    private Integer cantidadDisponible;

    // Constructor actualizado
    public ResumenCajeroItem(String descripcionDenominacion, BigDecimal valorMonetario, Integer cantidadDisponible) {
        this.descripcionDenominacion = descripcionDenominacion;
        this.valorMonetario = valorMonetario;
        this.cantidadDisponible = cantidadDisponible;
    }

    // Getters y Setters actualizados
    public String getDescripcionDenominacion() { return descripcionDenominacion; }
    public void setDescripcionDenominacion(String descripcionDenominacion) { this.descripcionDenominacion = descripcionDenominacion; }
    public BigDecimal getValorMonetario() { return valorMonetario; }
    public void setValorMonetario(BigDecimal valorMonetario) { this.valorMonetario = valorMonetario; }
    public Integer getCantidadDisponible() { return cantidadDisponible; }
    public void setCantidadDisponible(Integer cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }
}
