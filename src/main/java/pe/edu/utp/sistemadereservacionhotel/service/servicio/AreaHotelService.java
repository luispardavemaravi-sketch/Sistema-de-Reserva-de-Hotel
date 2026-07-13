package pe.edu.utp.sistemadereservacionhotel.service.servicio;

import pe.edu.utp.sistemadereservacionhotel.model.servicio.AreaHotel;

import java.util.List;
import java.util.Optional;

public interface AreaHotelService {
    AreaHotel save(AreaHotel areaHotel);

    AreaHotel update(AreaHotel areaHotel);

    void delete(Long id);

    List<AreaHotel> findAll();

    Optional<AreaHotel> findById(Long id);

    Optional<AreaHotel> findByNombreArea(String nombre);

    List<AreaHotel> findByNombre(String nombre);

    List<AreaHotel> findByUbicacion(String ubicacion);

    boolean existsByNombreArea(String nombre);

    long count();
}