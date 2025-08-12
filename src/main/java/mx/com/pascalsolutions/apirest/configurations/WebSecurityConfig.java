package mx.com.pascalsolutions.apirest.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
class WebSecurityConfig {

    @Value("${security.static.secret}")
    private String SECRET;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1. Habilita CORS usando la configuración del bean de abajo.
        // Esta es la pieza clave que faltaba.
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        
        // 2. Desactiva CSRF (esto ya lo tenías).
        http.csrf(csrf -> csrf.disable());

        // 3. Define las reglas de autorización (las tuyas, ya corregidas).
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/user/login", "/api/user/register").permitAll()
            // Hice un pequeño ajuste aquí para que cashout también sea público, como en tu código.
            .requestMatchers(HttpMethod.POST, "/api/atm/cashout").permitAll() 
            .requestMatchers(HttpMethod.GET, "/api/atm/resumen").permitAll()
            .anyRequest().authenticated()
        );
        
        // 4. Añade tu filtro JWT.
        http.addFilterAfter(new JWTAuthorizationFilter(SECRET), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Este bean define la configuración de CORS para toda la aplicación.
     * Es la fuente de configuración que utiliza http.cors() arriba.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite peticiones desde cualquier origen.
        configuration.setAllowedOrigins(Arrays.asList("*"));
        // Permite los métodos HTTP que necesitas.
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Permite todas las cabeceras.
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuración a todas las rutas de tu API.
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}