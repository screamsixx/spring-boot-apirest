package mx.com.pascalsolutions.apirest.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración web global para la aplicación.
 * Esta clase se utiliza para configurar CORS (Cross-Origin Resource Sharing)
 * y permitir que el frontend se comunique con el backend desde cualquier origen.
 */
@Configuration
public class CorsWebConfig implements WebMvcConfigurer {

    /**
     * Configura los permisos de CORS para toda la aplicación.
     *
     * @param registry El registro de CORS donde se definen las reglas.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Aplica la configuración a todas las rutas que comiencen con /api/
            .allowedOrigins("*") // Permite peticiones desde cualquier origen (IP o dominio).
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Especifica los métodos HTTP permitidos.
            .allowedHeaders("*") // Permite que se envíen todas las cabeceras en la petición.
            .allowCredentials(false); // No se permite el envío de credenciales (cookies, etc.).
    }
}
