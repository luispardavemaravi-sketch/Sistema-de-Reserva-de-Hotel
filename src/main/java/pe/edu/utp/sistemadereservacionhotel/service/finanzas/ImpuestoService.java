package pe.edu.utp.sistemadereservacionhotel.service.finanzas;

import pe.edu.utp.sistemadereservacionhotel.dto.ImpuestoDTO;

import java.util.List;

/**
 * Contrato de operaciones de negocio para el dominio de impuestos.
 * Expone métodos transaccionales garantizando el encapsulamiento a través de DTOs.
 */
public interface ImpuestoService {

    ImpuestoDTO registrarImpuesto(ImpuestoDTO dto);

    ImpuestoDTO actualizarImpuesto(Long id, ImpuestoDTO dto);

    void eliminarImpuesto(Long id);

    List<ImpuestoDTO> listarTodos();

    ImpuestoDTO buscarPorId(Long id);

    ImpuestoDTO buscarPorNombre(String nombre);
}