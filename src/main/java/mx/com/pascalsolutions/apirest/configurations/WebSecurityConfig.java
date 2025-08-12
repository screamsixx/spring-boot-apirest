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
        
        // 1. Aplicar la configuración de CORS PRIMERO.
        // Esto asegura que las cabeceras de CORS se añadan a CADA respuesta.
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        
        // 2. Desactivar CSRF (no se usa en APIs RESTful con JWT).
        http.csrf(AbstractHttpConfigurer::disable);

        // 3. Desactivar la gestión de sesiones (la API es "stateless").
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 4. Desactivar los mecanismos de login por defecto de Spring.
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        // 5. Configurar las reglas de autorización de las rutas.
        // Aquí le decimos a Spring que procese estas rutas, pero que les permita el acceso.
        http.authorizeHttpRequests(auth -> auth
            // Todas las rutas del cajero y de usuario son públicas.
            .requestMatchers("/api/atm/**", "/api/user/**").permitAll()
            // Cualquier otra ruta futura requerirá autenticación.
            .anyRequest().authenticated()
        );
        
        // 6. Añadir tu filtro JWT para que se aplique a las rutas protegidas (si las hubiera).
        http.addFilterAfter(new JWTAuthorizationFilter(SECRET), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Orígenes permitidos.
        configuration.setAllowedOrigins(List.of("*")); 
        
        // Métodos HTTP permitidos.
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Cabeceras permitidas.
        configuration.setAllowedHeaders(List.of("*"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
