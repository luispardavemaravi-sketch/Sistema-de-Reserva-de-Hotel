package pe.edu.utp.sistemadereservacionhotel.repository.reserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Acompanante;

import java.util.List;

@Repository
public interface AcompananteRepository extends JpaRepository<Acompanante, Long> {

    List<Acompanante> findByReserva_IdReserva(Long idReserva);

    List<Acompanante> findByDocumentoIdentidad(String documentoIdentidad);

    List<Acompanante> findByNombreCompletoContainingIgnoreCase(String nombre);

    List<Acompanante> findByParentesco(String parentesco);
}