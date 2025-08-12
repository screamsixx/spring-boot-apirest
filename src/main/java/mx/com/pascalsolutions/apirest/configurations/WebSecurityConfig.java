package mx.com.pascalsolutions.apirest.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        
        // 2. Desactivar CSRF (Cross-Site Request Forgery).
        http.csrf(AbstractHttpConfigurer::disable);

        // 3. Desactivar la gestión de sesiones. Esencial para APIs REST y JWT.
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 4. Configurar las reglas de autorización de las rutas.
        http.authorizeHttpRequests(auth -> auth
            // Rutas públicas que no requieren autenticación
            .requestMatchers("/api/user/login", "/api/user/register").permitAll()
            .requestMatchers("/api/atm/**").permitAll() // Simplificado para permitir todo bajo /api/atm/
            
            // Todas las demás rutas requieren autenticación.
            .anyRequest().authenticated()
        );
        
        // 5. Añadir tu filtro personalizado para la autorización JWT.
        http.addFilterAfter(new JWTAuthorizationFilter(SECRET), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Orígenes permitidos (puedes ser más específico si quieres)
        configuration.setAllowedOrigins(List.of("*")); 
        
        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Cabeceras permitidas
        configuration.setAllowedHeaders(List.of("*"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplicar esta configuración a todas las rutas.
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}