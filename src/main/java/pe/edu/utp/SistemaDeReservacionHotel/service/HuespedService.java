package pe.edu.utp.SistemaDeReservacionHotel.service;

import pe.edu.utp.SistemaDeReservacionHotel.model.Huesped;

import java.util.List;
import java.util.Optional;

public interface HuespedService {

    Huesped save(Huesped huesped);

    Huesped update(Huesped huesped);

    void delete(Long id);

    List<Huesped> findAll();

    Optional<Huesped> findById(Long id);

    Optional<Huesped> findByEmail(String email);

    Optional<Huesped> findByDocumentoIdentidad(String documento);

    List<Huesped> findByNombre(String nombre);

    List<Huesped> findByApellidos(String apellidos);

    boolean existsByEmail(String email);

    boolean existsByDocumentoIdentidad(String documento);

    long count();
}
