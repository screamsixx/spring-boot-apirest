package mx.com.pascalsolutions.apirest.repository;

import mx.com.pascalsolutions.apirest.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para acceder a los datos de la entidad AppUser.
 * Hereda de JpaRepository para obtener métodos CRUD básicos de forma automática.
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    /**
     * Busca un usuario por su nombre de usuario (columna 'usuario').
     * Spring Data JPA infiere la consulta SQL a partir del nombre del método.
     * "findBy" + "Usuario" -> "SELECT * FROM usuarios WHERE usuario = ?"
     *
     * @param usuario El nombre de usuario a buscar.
     * @return Un Optional que contiene el AppUser si se encuentra, o un Optional vacío si no.
     */
    Optional<AppUser> findByUsuario(String usuario);

}
