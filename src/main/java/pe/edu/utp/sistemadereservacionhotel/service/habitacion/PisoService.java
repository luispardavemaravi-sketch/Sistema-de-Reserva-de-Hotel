package pe.edu.utp.sistemadereservacionhotel.service.habitacion;


import pe.edu.utp.sistemadereservacionhotel.dto.PisoDTO;

import java.util.List;


public interface PisoService {
    PisoDTO registrarPiso(PisoDTO dto);

    PisoDTO actualizarPiso(Long id, PisoDTO dto);

    void eliminarPiso(Long id);

    List<PisoDTO> listarTodos();

    PisoDTO buscarPorId(Long id);

    PisoDTO buscarPorNumero(Integer numero);

    List<PisoDTO> buscarPorSector(String sector);
}