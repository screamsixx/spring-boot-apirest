package mx.com.pascalsolutions.apirest.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
class WebSecurityConfig {

    @Value("${security.static.secret}")
    private String SECRET;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        // --- CONFIGURACIÓN ESENCIAL PARA UNA API RESTful ---

        // 1. Aplicar la configuración de CORS PRIMERO.
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        
        // 2. Desactivar CSRF (Cross-Site Request Forgery) porque no usamos cookies.
        http.csrf(AbstractHttpConfigurer::disable);

        // 3. Desactivar la gestión de sesiones. ¡CRÍTICO! Esto le dice a Spring que no guarde ningún estado.
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 4. Desactivar los formularios de login y la autenticación básica HTTP. ¡CLAVE PARA EVITAR ERRORES INTERMITENTES!
        // Esto previene que Spring Security intente redirigir o mostrar pop-ups de autenticación.
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        // 5. Configurar las reglas de autorización de las rutas.
        http.authorizeHttpRequests(auth -> auth
            // Todas las rutas del cajero son públicas.
            .requestMatchers("/api/atm/**").permitAll()
            // Las rutas de usuario para login/registro son públicas.
            .requestMatchers("/api/user/login", "/api/user/register").permitAll()
            // Todas las demás rutas (si las hubiera) requieren autenticación.
            .anyRequest().authenticated()
        );
        
        // 6. Añadir tu filtro personalizado para la autorización JWT (si lo usas para otras rutas).
        // Si ninguna ruta es autenticada, esta línea podría incluso comentarse.
        http.addFilterAfter(new JWTAuthorizationFilter(SECRET), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Orígenes permitidos. Usar "*" es bueno para desarrollo. 
        // En producción podrías cambiarlo a "https://fancy-queijadas-53b9c4.netlify.app"
        configuration.setAllowedOrigins(List.of("*")); 
        
        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Cabeceras permitidas
        configuration.setAllowedHeaders(List.of("*"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
