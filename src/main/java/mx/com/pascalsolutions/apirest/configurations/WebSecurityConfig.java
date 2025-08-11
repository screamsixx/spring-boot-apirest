package mx.com.pascalsolutions.apirest.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
class WebSecurityConfig {

    @Value("${security.static.secret}")
    private String SECRET;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Desactiva CSRF
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/api/user/login","/api/user/register").permitAll()
                .anyRequest().authenticated() // Requiere autenticación para cualquier otra solicitud
            )
            .addFilterAfter(new JWTAuthorizationFilter(SECRET), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}