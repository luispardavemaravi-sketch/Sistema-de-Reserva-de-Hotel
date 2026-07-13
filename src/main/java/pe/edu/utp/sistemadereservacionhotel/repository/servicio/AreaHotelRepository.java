package pe.edu.utp.sistemadereservacionhotel.repository.servicio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.servicio.AreaHotel;

import java.util.List;
import java.util.Optional;

@Repository
public interface AreaHotelRepository extends JpaRepository<AreaHotel, Long> {
    Optional<AreaHotel> findByNombreArea(String nombreArea);

    boolean existsByNombreArea(String nombreArea);

    List<AreaHotel> findByNombreAreaContainingIgnoreCase(String nombre);

    List<AreaHotel> findByUbicacionContainingIgnoreCase(String ubicacion);
}