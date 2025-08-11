package mx.com.pascalsolutions.apirest.controllers;

import mx.com.pascalsolutions.apirest.domain.AppUser;
import mx.com.pascalsolutions.apirest.interfaces.JwtTokenServiceI;
import mx.com.pascalsolutions.apirest.models.Client;
import mx.com.pascalsolutions.apirest.services.AppUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
public class UserController extends BaseController {

    private final JwtTokenServiceI jwtTokenService;
    private final AppUserService appUserService; // Inyectamos el nuevo servicio

    @Autowired
    public UserController(JwtTokenServiceI jwtTokenService, AppUserService appUserService) {
        this.jwtTokenService = jwtTokenService;
        this.appUserService = appUserService; // Lo inicializamos en el constructor
    }

    /**
     * Endpoint para el login. Valida las credenciales contra la base de datos
     * y si son correctas, devuelve un token JWT.
     * URL: POST /user/login
     */
    @PostMapping("/user/login")
    public CompletableFuture<ResponseEntity<?>> login(@RequestParam("user") String username,
            @RequestParam("password") String pwd) {

        // 1. Autenticar contra la base de datos usando el servicio
        Optional<AppUser> userOptional = appUserService.authenticate(username, pwd);

        if (userOptional.isPresent()) {
            // 2. Si la autenticación es exitosa, genera el token
            return jwtTokenService.getJWTToken(username).thenApply(token -> {
                Client cliente = new Client(username, pwd, token, "Ip no identificada", "Sin nombre");
                // Devuelve el cliente con el token y un estado 200 (OK)
                return ResponseEntity.ok(cliente);
            });
        } else {
            // 3. Si la autenticación falla, devuelve un error 401 (Unauthorized)
            return CompletableFuture.completedFuture(
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos"));
        }
    }

    /**
     * Endpoint para registrar un nuevo usuario.
     * Espera un JSON con los datos del usuario en el cuerpo de la petición.
     * URL: POST /user/register
     */
    @PostMapping("/user/register")
    public ResponseEntity<?> register(@RequestBody AppUser newUser) { // <-- CAMBIO CLAVE AQUÍ
        Optional<AppUser> createdUser = appUserService.createUser(newUser);

        // Si el usuario se creó, devuelve el objeto guardado y un estado 201 (Created).
        // Si ya existía, devuelve un mensaje de error y un estado 409 (Conflict).
        return createdUser
                .<ResponseEntity<?>>map(user -> new ResponseEntity<>(user, HttpStatus.CREATED))
                .orElse(ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("El usuario ya existe, escoge otro nombre de usuario"));
    }
}
