package mx.com.pascalsolutions.apirest.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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

    /**
     * Este bean le dice a Spring Security que IGNORE por completo la cadena de filtros
     * para las rutas especificadas. Esta es la forma más efectiva de hacer una ruta pública.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
            "/api/atm/**",
            "/api/user/login",
            "/api/user/register"
        );
    }

    /**
     * Este bean configura la seguridad para TODAS LAS DEMÁS rutas que NO fueron ignoradas.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        // 1. Aplicar la configuración de CORS.
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        
        // 2. Desactivar CSRF.
        http.csrf(AbstractHttpConfigurer::disable);

        // 3. Configurar la API para que sea "stateless" (sin estado).
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 4. Configurar las reglas de autorización.
        http.authorizeHttpRequests(auth -> auth
            // Como las rutas públicas ya fueron ignoradas, aquí solo necesitamos
            // decir que cualquier otra ruta requiere autenticación.
            .anyRequest().authenticated()
        );
        
        // 5. Añadir tu filtro JWT para que se aplique a las rutas protegidas.
        http.addFilterAfter(new JWTAuthorizationFilter(SECRET), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Define la configuración de CORS.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); 
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
