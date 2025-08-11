package mx.com.pascalsolutions.apirest.interfaces;

import java.util.concurrent.CompletableFuture;

/*La anotación @Async no se coloca dentro de la interfaz JwtTokenServiceI. 
Las interfaces solo declaran métodos, no contienen lógica de implementación.
La anotación @Async debe colocarse en el método getJWTToken de la implementación,
es decir, en la clase JwtTokenServiceImpl
*
*/

public interface JwtTokenServiceI {
  CompletableFuture<String> getJWTToken(String username);  
}