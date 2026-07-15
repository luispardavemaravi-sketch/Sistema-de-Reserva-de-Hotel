package pe.edu.utp.sistemadereservacionhotel.service.servicio;

import pe.edu.utp.sistemadereservacionhotel.dto.servicio.ConsumoServicioDTO;

import java.util.List;

public interface ConsumoServicioService {
    ConsumoServicioDTO registrarConsumo(ConsumoServicioDTO dto);

    ConsumoServicioDTO actualizarConsumo(Long id, ConsumoServicioDTO dto);

    void eliminarConsumo(Long id);

    List<ConsumoServicioDTO> listarPorReserva(Long idReserva);
}