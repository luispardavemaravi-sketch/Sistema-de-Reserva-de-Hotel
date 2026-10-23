INSERT INTO estado_reserva (id_estado, nombre_estado, es_modificable)
VALUES (1, 'Pendiente', true), (2, 'Confirmada', true),
       (3, 'Cancelada', false), (4, 'En curso', false), (5, 'Finalizada', false);
SELECT setval(pg_get_serial_sequence('estado_reserva', 'id_estado'), 5, true);

ALTER TABLE reserva ADD CONSTRAINT reserva_fechas_validas
    CHECK (fecha_salida_planificada > fecha_entrada_planificada);
ALTER TABLE reserva ADD CONSTRAINT reserva_monto_no_negativo
    CHECK (monto_total_estimado >= 0);
CREATE INDEX reserva_disponibilidad_idx
    ON reserva (id_habitacion, fecha_entrada_planificada, fecha_salida_planificada);
