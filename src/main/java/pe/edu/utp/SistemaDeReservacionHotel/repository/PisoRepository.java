package pe.edu.utp.SistemaDeReservacionHotel.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.SistemaDeReservacionHotel.model.Piso;

import java.util.List;
import java.util.Optional;

@Repository
public interface PisoRepository extends JpaRepository<Piso, Long> {

    Optional<Piso> findByNumeroPiso(Integer numeroPiso);

    boolean existsByNumeroPiso(Integer numeroPiso);

    List<Piso> findBySectorContainingIgnoreCase(String sector);
}