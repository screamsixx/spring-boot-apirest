package mx.com.pascalsolutions.apirest.services;

import mx.com.pascalsolutions.apirest.domain.AppUser;
import mx.com.pascalsolutions.apirest.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Servicio para manejar la lógica de negocio relacionada con los usuarios.
 */
@Service
public class AppUserService {

    // Inyectamos el repositorio para poder interactuar con la base de datos.
    private final AppUserRepository appUserRepository;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    /**
     * Autentica a un usuario verificando su nombre de usuario y contraseña.
     *
     * @param username El nombre de usuario a verificar.
     * @param password La contraseña a verificar.
     * @return Un Optional que contiene el AppUser si la autenticación es exitosa.
     */
    public Optional<AppUser> authenticate(String username, String password) {
        // 1. Buscamos al usuario en la base de datos por su nombre de usuario.
        Optional<AppUser> userOptional = appUserRepository.findByUsuario(username);

        // 2. Verificamos si el usuario existe y si la contraseña coincide.
        if (userOptional.isPresent()) {
            AppUser user = userOptional.get();
            // IMPORTANTE: En una aplicación real, NUNCA compares contraseñas en texto plano.
            // Deberías usar un PasswordEncoder (como BCrypt) para comparar el hash.
            // Ejemplo: if (passwordEncoder.matches(password, user.getPass())) { ... }
            if (password.equals(user.getPass())) {
                return userOptional; // Las credenciales son correctas.
            }
        }

        // Si el usuario no existe o la contraseña es incorrecta, devolvemos un Optional vacío.
        return Optional.empty();
    }

    /**
     * Crea un nuevo usuario en la base de datos.
     *
     * @param newUser El objeto AppUser con los datos del nuevo usuario.
     * @return Un Optional con el usuario guardado (incluyendo su nuevo ID),
     * o un Optional vacío si el nombre de usuario ya existe.
     */
    public Optional<AppUser> createUser(AppUser newUser) {
        // 1. Verificamos si ya existe un usuario con ese nombre de usuario para evitar duplicados.
        if (appUserRepository.findByUsuario(newUser.getUsuario()).isPresent()) {
            // Si ya existe, no hacemos nada y devolvemos un Optional vacío.
            return Optional.empty();
        }

        // 2. Si no existe, guardamos el nuevo usuario en la base de datos.
        // El método save() devuelve la entidad guardada, que ahora incluirá el ID generado.
        AppUser savedUser = appUserRepository.save(newUser);
        return Optional.of(savedUser);
    }
}
