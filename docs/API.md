# API

Base URL local: `http://localhost:8080/api`. OpenAPI: `http://localhost:8080/v3/api-docs`; Swagger UI: `http://localhost:8080/swagger-ui.html`.

| Método y ruta | Uso | Rol |
|---|---|---|
| `POST /auth/login` | `usuario`, `password`, `turnoId`; devuelve `user`, `token`, `turnoId` | Público |
| `POST /auth/usuarios` | Crear usuario con correo, nombres, apellidos, contraseña, cargo y turno | Administrador |
| `GET /auth/turnos-disponibles` | Selector de login: `idTurno`, `horaInicio`, `horaFinal`; turnos con días configurados y horas distintas, incluidos nocturnos | Público (solo GET) |
| `/personal/turnos/**` | Consulta y administración de turnos, incluido `diasSemana` | ADMINISTRADOR |
| `GET /habitaciones` | Catálogo de habitaciones | Administrador, recepcionista |
| `GET /huespedes` | Lista de huéspedes | Administrador, recepcionista |
| `GET /reservas` | Lista de reservas | Administrador, recepcionista |
| `POST /reservas` | Crear reserva de noche | Administrador, recepcionista |
| `PUT /reservas/{id}` | Cambiar fechas/habitación/huésped si no inició | Administrador, recepcionista |
| `POST /reservas/{id}/confirmacion` | Pendiente a confirmada | Administrador, recepcionista |
| `POST /reservas/{id}/entrada` | Confirmada a en curso, crea CheckIn | Administrador, recepcionista |
| `POST /reservas/{id}/salida` | En curso a finalizada, crea CheckOut | Administrador, recepcionista |
| `DELETE /reservas/{id}` | Cancelación lógica previa al CheckIn | Administrador, recepcionista |

`POST /reservas` recibe `{ "idHuesped": 1, "idHabitacion": 2, "fechaEntradaPlanificada": "2026-11-10", "fechaSalidaPlanificada": "2026-11-12" }`. La tarifa se calcula en el backend con el precio vigente y el número de noches. Las fechas usan `YYYY-MM-DD`, en intervalo `[entrada, salida)`.

Las rutas heredadas de personal, servicios y finanzas siguen en Swagger, pero su integración con Angular y sus permisos específicos requieren auditoría adicional. Un conflicto de disponibilidad devuelve 409; datos inválidos 400; recurso ausente 404; autenticación 401; autorización 403.

Las rutas heredadas `/reservas/check-in/**` y `/reservas/check-out/**` están cerradas en seguridad porque no actualizan el estado de la reserva. Usar las rutas de transición de esta tabla.

Crear o editar una reserva con un huésped inactivo devuelve 409. Véase [informe de integración](PHASE2_REPORT.md) para el alcance de las pruebas HTTP y los módulos pendientes.
