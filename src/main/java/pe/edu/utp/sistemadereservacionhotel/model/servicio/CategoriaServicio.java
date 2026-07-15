package pe.edu.utp.sistemadereservacionhotel.model.servicio;

/**
 * Enumeración que define las categorías cerradas para los servicios del hotel.
 * Sustituye el antipatrón de texto libre (String), asegurando la integridad
 * referencial y facilitando la indexación.
 */
public enum CategoriaServicio {
    RESTAURANTE,
    SPA_Y_BIENESTAR,
    TRANSPORTE,
    LIMPIEZA,
    ENTRETENIMIENTO,
    OTROS
}