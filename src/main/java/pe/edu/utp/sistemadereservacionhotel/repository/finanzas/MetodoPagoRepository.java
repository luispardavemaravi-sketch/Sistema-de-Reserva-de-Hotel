package pe.edu.utp.sistemadereservacionhotel.repository.finanzas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.MetodoPago;

import java.util.List;
import java.util.Optional;

@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Long> {
    Optional<MetodoPago> findByNombreMetodo(String nombreMetodo);

    boolean existsByNombreMetodo(String nombreMetodo);

    List<MetodoPago> findByEsDigital(Boolean esDigital);
}