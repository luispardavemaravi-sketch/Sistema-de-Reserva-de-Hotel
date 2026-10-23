package pe.edu.utp.sistemadereservacionhotel.service.finanzas;

import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.CajaAperturaDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.CajaResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Servicio transaccional que orquesta la lógica de negocio para la gestión de cajas.
 */
public interface CajaService {

    CajaResponseDTO abrirCaja(CajaAperturaDTO dto);

    CajaResponseDTO cerrarCaja(Long idCaja, BigDecimal montoCierre);

    void eliminarCaja(Long id);

    CajaResponseDTO buscarPorId(Long id);

    List<CajaResponseDTO> listarTodas();

    List<CajaResponseDTO> buscarPorEmpleado(Long idEmpleado);

    List<CajaResponseDTO> buscarCajasAbiertas();

    List<CajaResponseDTO> buscarPorRangoFecha(LocalDate inicio, LocalDate fin);
}