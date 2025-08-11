package mx.com.pascalsolutions.apirest.models;


public class User{

	private String user;
	private String pwd;
	private String token;
	
	// Constructor por defecto
    public User() {
        // Aquí puedes establecer valores por defecto para los atributos
        this.user = "";
        this.pwd = "";
        this.token = "";
    }
    // Constructor con parámetros
    public User(String user, String pwd, String token) {
        this.user = user;
        this.pwd = pwd;
        this.token=token;
    }

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
