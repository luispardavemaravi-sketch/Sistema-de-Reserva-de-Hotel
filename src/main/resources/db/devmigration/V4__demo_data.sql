INSERT INTO turno (id_turno, hora_inicio, hora_final) VALUES
    (1, '07:00', '15:00'), (2, '15:00', '23:00'), (3, '23:00', '07:00');
SELECT setval(pg_get_serial_sequence('turno', 'id_turno'), 3, true);

INSERT INTO piso (id_piso, numero_piso, sector) VALUES
    (1, 1, 'Principal'), (2, 2, 'Principal');
SELECT setval(pg_get_serial_sequence('piso', 'id_piso'), 2, true);

INSERT INTO tipo_habitacion (id_tipo, nombre, capacidad_maxima, precio_base) VALUES
    (1, 'Estándar', 2, 35.00), (2, 'Matrimonial', 2, 40.00), (3, 'Suite con Jacuzzi', 2, 45.00);
SELECT setval(pg_get_serial_sequence('tipo_habitacion', 'id_tipo'), 3, true);

INSERT INTO estado_habitacion (id_estado, nombre_estado, es_reservable) VALUES
    (1, 'Disponible', true), (2, 'Mantenimiento', false), (3, 'Limpieza', false), (4, 'Ocupada', false);
SELECT setval(pg_get_serial_sequence('estado_habitacion', 'id_estado'), 4, true);

INSERT INTO habitacion (id_habitacion, numero_habitacion, precio_actual, estado_activo, id_piso, id_tipo, id_estado_habitacion)
VALUES (1, '101', 35.00, true, 1, 1, 1), (2, '102', 40.00, true, 1, 2, 1),
       (3, '201', 45.00, true, 2, 3, 1);
SELECT setval(pg_get_serial_sequence('habitacion', 'id_habitacion'), 3, true);

INSERT INTO huesped (id_huesped, nombre, apellidos, documento_identidad, email, telefono, estado_activo)
VALUES (1, 'Lucía', 'Méndez', '12345678', 'lucia.demo@example.invalid', '900000001', true),
       (2, 'Carlos', 'Benavides', '23456789', 'carlos.demo@example.invalid', '900000002', true);
SELECT setval(pg_get_serial_sequence('huesped', 'id_huesped'), 2, true);

INSERT INTO reserva (id_reserva, codigo_reserva, fecha_reserva, fecha_entrada_planificada,
    fecha_salida_planificada, monto_total_estimado, id_habitacion, id_huesped, id_estado_reserva,
    created_date, created_by)
VALUES (1, 'DEMO-001', CURRENT_TIMESTAMP, CURRENT_DATE + 2, CURRENT_DATE + 4,
    70.00, 1, 1, 1, CURRENT_TIMESTAMP, 'demo'),
       (2, 'DEMO-002', CURRENT_TIMESTAMP, CURRENT_DATE + 5, CURRENT_DATE + 7,
    80.00, 2, 2, 2, CURRENT_TIMESTAMP, 'demo');
SELECT setval(pg_get_serial_sequence('reserva', 'id_reserva'), 2, true);
