# Base de datos

PostgreSQL es la base de ejecución normal. `spring.jpa.hibernate.ddl-auto=validate`; Flyway crea el esquema. Las pruebas de contexto usan H2 con `create-drop` y Flyway deshabilitado, solo para aislar pruebas Java.

| Migración | Contenido |
|---|---|
| V1 | 26 tablas principales generadas desde el mapeo JPA heredado, claves y relaciones. |
| V2 | Estados de reserva, fechas válidas, monto no negativo e índice de disponibilidad. |
| V3 | Usuarios de acceso con email único, hash BCrypt, cargo y turno. |
| V4 (solo perfil `dev`) | Turnos, pisos, tipos, habitaciones, huéspedes y reservas de prueba. |

Relación central: `huesped` 1:N `reserva` N:1 `habitacion`; `reserva` N:1 `estado_reserva` y 1:1 `check_in` / `check_out`. Las habitaciones se relacionan con piso, tipo, estado y equipamiento. Servicios y comprobantes se relacionan con reservas.

`estado_reserva` recibe los cinco estados operativos en V2. El administrador inicial se crea desde variables de entorno, no desde SQL. El perfil `dev` añade datos PostgreSQL reproducibles. Las otras pantallas Angular conservan también datos locales independientes.

La disponibilidad se comprueba en Java con una consulta de rangos cruzados mientras se mantiene el bloqueo de la habitación. La base impide rangos nulos/invertidos y dispone de un índice, pero no contiene todavía un constraint de exclusión que proteja escrituras directas fuera de la API.
