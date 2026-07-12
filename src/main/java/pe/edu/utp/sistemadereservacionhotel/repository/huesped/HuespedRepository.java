package pe.edu.utp.sistemadereservacionhotel.repository.huesped;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.huesped.Huesped;

import java.util.List;
import java.util.Optional;

@Repository
public interface HuespedRepository extends JpaRepository<Huesped, Long> {

    Optional<Huesped> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Huesped> findByDocumentoIdentidad(String documentoIdentidad);

    boolean existsByDocumentoIdentidad(String documentoIdentidad);

    List<Huesped> findByNombreContainingIgnoreCase(String nombre);

    List<Huesped> findByApellidosContainingIgnoreCase(String apellidos);


}
