package mx.com.pascalsolutions.apirest.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BaseController {
    // Este controlador base no necesita implementar lógica adicional, 
    // ya que la autorización se maneja en el interceptor.
}