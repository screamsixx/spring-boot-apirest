package mx.com.pascalsolutions.apirest.interfaces;

import java.math.BigDecimal;
import java.util.List;

import mx.com.pascalsolutions.apirest.models.ResumenCajeroItem;
import mx.com.pascalsolutions.apirest.models.RetiroResponse;

public interface AtmServiceI {
    RetiroResponse realizarRetiro(BigDecimal cantidad);
    List<ResumenCajeroItem> obtenerResumen();
}
