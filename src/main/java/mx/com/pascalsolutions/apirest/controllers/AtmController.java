package mx.com.pascalsolutions.apirest.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import mx.com.pascalsolutions.apirest.interfaces.AtmServiceI;
import mx.com.pascalsolutions.apirest.models.ResumenCajeroItem;
import mx.com.pascalsolutions.apirest.models.RetiroRequest;
import mx.com.pascalsolutions.apirest.models.RetiroResponse;

@RestController

public class AtmController extends BaseController {

    // 1. Declarar la dependencia de la interfaz del servicio
    private final AtmServiceI atmService;

    // 2. Inyectar la dependencia a través del constructor
    // Spring buscará automáticamente una clase que implemente AtmServiceI
    // (AtmServiceImpl)
    // y la "inyectará" aquí.
    @Autowired
    public AtmController(AtmServiceI atmService) {
        this.atmService = atmService;
    }

    @GetMapping("/atm/resumen")
    public ResponseEntity<List<ResumenCajeroItem>> obtenerResumen() {
        List<ResumenCajeroItem> resumen = atmService.obtenerResumen();
        return ResponseEntity.ok(resumen);
    }

    @PostMapping("/atm/cashout")
    public ResponseEntity<?> retirarDinero(@Valid @RequestBody RetiroRequest retiroRequest) {
        try {
            // 3. Llamar al método del servicio y pasarle la cantidad
            RetiroResponse response = atmService.realizarRetiro(retiroRequest.getCantidad());
            // Si todo va bien, devolver la respuesta del servicio con un estado 200 OK
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // 4. Si el servicio lanza una excepción (porque el SP devolvió un error),
            // la atrapamos aquí y creamos una respuesta de error clara.
            Map<String, Object> errorBody = Map.of(
                    "status", HttpStatus.BAD_REQUEST.value(),
                    "error", "Error en la solicitud de retiro",
                    "message", e.getMessage() // Este es el mensaje de error que viene del SP
            );
            return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
        }
    }
}
