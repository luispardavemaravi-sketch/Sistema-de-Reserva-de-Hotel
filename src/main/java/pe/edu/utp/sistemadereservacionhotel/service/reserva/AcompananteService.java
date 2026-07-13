package pe.edu.utp.sistemadereservacionhotel.service.reserva;

import pe.edu.utp.sistemadereservacionhotel.model.reserva.Acompanante;

import java.util.List;
import java.util.Optional;

public interface AcompananteService {

    Acompanante save(Acompanante acompanante);

    Acompanante update(Acompanante acompanante);

    void delete(Long id);

    List<Acompanante> findAll();

    Optional<Acompanante> findById(Long id);

    List<Acompanante> findByReserva(Long idReserva);

    List<Acompanante> findByDocumentoIdentidad(String documento);

    List<Acompanante> findByNombre(String nombre);

    List<Acompanante> findByParentesco(String parentesco);

    long count();
}