-- V4 created the development shifts without their required days.
-- Only complete unchanged demo shifts with no schedule; preserve custom schedules.
INSERT INTO turno_dias (id_turno, dia_semana)
SELECT t.id_turno, d.dia
FROM turno t
CROSS JOIN (VALUES ('MONDAY'), ('TUESDAY'), ('WEDNESDAY'), ('THURSDAY'),
                   ('FRIDAY'), ('SATURDAY'), ('SUNDAY')) AS d(dia)
WHERE ((t.id_turno = 1 AND t.hora_inicio = TIME '07:00' AND t.hora_final = TIME '15:00')
    OR (t.id_turno = 2 AND t.hora_inicio = TIME '15:00' AND t.hora_final = TIME '23:00')
    OR (t.id_turno = 3 AND t.hora_inicio = TIME '23:00' AND t.hora_final = TIME '07:00'))
  AND NOT EXISTS (SELECT 1 FROM turno_dias existing WHERE existing.id_turno = t.id_turno);
