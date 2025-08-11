package mx.com.pascalsolutions.apirest.models;


import java.util.List;

public class RetiroResponse {
    private String mensaje;
    private List<DesgloseItem> desglose;

    // Constructor, Getters y Setters...
    public RetiroResponse(String mensaje, List<DesgloseItem> desglose) {
        this.mensaje = mensaje;
        this.desglose = desglose;
    }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public List<DesgloseItem> getDesglose() { return desglose; }
    public void setDesglose(List<DesgloseItem> desglose) { this.desglose = desglose; }
}

