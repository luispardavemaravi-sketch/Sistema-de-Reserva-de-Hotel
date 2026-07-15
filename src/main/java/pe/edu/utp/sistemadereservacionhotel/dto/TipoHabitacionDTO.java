package pe.edu.utp.sistemadereservacionhotel.dto;

import java.math.BigDecimal;

public record TipoHabitacionDTO(Long id, String nombre, Integer capacidadMaxima, BigDecimal precioBase) {
}