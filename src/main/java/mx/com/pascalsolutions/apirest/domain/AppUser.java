package mx.com.pascalsolutions.apirest.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

/**
 * Representa la entidad de un usuario en la aplicación.
 * Esta clase se mapea a la tabla existente 'usuarios' en la base de datos.
 */
@Entity
@Table(name = "usuarios") // Mapea esta clase a la tabla 'usuarios'
public class AppUser {

    /**
     * Identificador único para cada usuario.
     * Es la clave primaria y se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre completo del usuario. Mapeado a la columna 'nombre'.
     */
    @Column(length = 50) // Define la longitud máxima del varchar
    private String nombre;

    /**
     * Nombre de usuario para el login. Mapeado a la columna 'usuario'.
     */
    @Column(length = 50)
    private String usuario;

    /**
     * Contraseña del usuario. Mapeado a la columna 'pass'.
     * Nota: En producción, esto debería almacenarse como un hash.
     */
    @Column(name = "pass", length = 50) // 'name' se usa si el campo y la columna se llaman diferente
    private String pass;

    /**
     * Constructor sin argumentos, requerido por JPA.
     */
    public AppUser() {
    }

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
