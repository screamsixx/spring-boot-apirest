package mx.com.pascalsolutions.apirest.models;

public class Client extends User{
	private String ip;
    private String nombre;
    
    public Client(String user, String pwd, String token, String ip, String nombre) {
        super(user, pwd, token); // Inicializamos los atributos heredados de User
        this.ip = ip;
        this.nombre = nombre;
    }
    // Getters y setters
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}