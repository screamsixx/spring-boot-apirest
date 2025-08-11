package mx.com.pascalsolutions.apirest.services;

import mx.com.pascalsolutions.apirest.interfaces.AtmServiceI; // <-- EDITADO
import mx.com.pascalsolutions.apirest.models.DesgloseItem;
import mx.com.pascalsolutions.apirest.models.ResumenCajeroItem;
import mx.com.pascalsolutions.apirest.models.RetiroResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service // Marca esta clase como un componente de servicio de Spring
public class AtmServiceImpl implements AtmServiceI {

    private final SimpleJdbcCall jdbcCall;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AtmServiceImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_retiro_cajero")
                .returningResultSet("desglose", new DesgloseItemMapper());
    }

    @Override
    public RetiroResponse realizarRetiro(BigDecimal cantidad) {
        // ... (lógica existente para el retiro)
        try {
            Map<String, Object> inParams = Map.of("cantidad_a_retirar", cantidad);
            Map<String, Object> result = jdbcCall.execute(inParams);
            List<DesgloseItem> desglose = (List<DesgloseItem>) result.get("desglose");
            String mensaje = "Retiro exitoso. Desglose del dinero dispensado:";
            return new RetiroResponse(mensaje, desglose);
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getMessage() != null) {
                 throw new RuntimeException(e.getCause().getMessage());
            }
            throw new RuntimeException("Ocurrió un error inesperado durante el retiro.");
        }
    }

    @Override
    public List<ResumenCajeroItem> obtenerResumen() {
        // CAMBIO: Se actualizó la consulta SQL para incluir la fila del total.
        String sql = "SELECT descripcion_denominacion, valor_monetario, cantidad_disponible " +
                     "FROM dinero_cajero " +
                     "UNION ALL " +
                     "SELECT 'Total' AS descripcion_denominacion, SUM(valor_monetario * cantidad_disponible) AS valor_monetario, NULL AS cantidad_disponible " +
                     "FROM dinero_cajero;";
        try {
            return jdbcTemplate.query(sql, new ResumenCajeroItemMapper());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el resumen del cajero desde la base de datos.", e);
        }
    }

    // RowMapper para el desglose del retiro (sin cambios)
    private static final class DesgloseItemMapper implements RowMapper<DesgloseItem> {
        @Override
        public DesgloseItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new DesgloseItem(rs.getBigDecimal("denominacion"), rs.getString("descripcion"), rs.getInt("cantidad"));
        }
    }

    // RowMapper para el resumen (Actualizado)
    private static final class ResumenCajeroItemMapper implements RowMapper<ResumenCajeroItem> {
        @Override
        public ResumenCajeroItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ResumenCajeroItem(
                    rs.getString("descripcion_denominacion"),
                    rs.getBigDecimal("valor_monetario"),
                    // CAMBIO: Se usa getObject para manejar correctamente los valores NULL de la base de datos.
                    rs.getObject("cantidad_disponible", Integer.class)
            );
        }
    }
}
