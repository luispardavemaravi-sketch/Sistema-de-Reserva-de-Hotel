package pe.edu.utp.sistemadereservacionhotel.service.habitacion;

import pe.edu.utp.sistemadereservacionhotel.dto.EquipamientoDTO;
import java.math.BigDecimal;
import java.util.List;

public interface EquipamientoService {

    EquipamientoDTO registrarEquipamiento(EquipamientoDTO dto);

    void eliminarEquipamiento(Long id);

    List<EquipamientoDTO> listarTodos();

    /**
     * Filtra por costo máximo utilizando BigDecimal para precisión contable.
     */
    List<EquipamientoDTO> buscarPorCostoMaximo(BigDecimal costoMaximo);
}