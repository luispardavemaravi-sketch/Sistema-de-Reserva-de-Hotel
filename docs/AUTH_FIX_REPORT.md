# Corrección del bloqueo de autenticación

Fecha: 23/10/2026 (America/Lima).

## Causas verificadas

- Angular necesitaba cargar un turno antes de enviar el login y consultaba `/api/personal/turnos`.
- Aunque el código permitía GET anónimo sobre esa ruta, el contenedor devolvía 401. Sus logs mostraron `HttpMessageNotWritableException`: `TurnoDTO` conservaba la colección lazy `diasSemana`, que Jackson intentaba leer después del cierre de la transacción. La autorización del despacho de error ocultaba el fallo original tras un 401.
- La migración de desarrollo V4 había creado tres turnos sin registros en `turno_dias`. Flyway validaba sus migraciones correctamente, pero eso no comprobaba la integridad funcional de esos datos.
- Docker construye el backend desde `pom.xml` y `src/` de la raíz; la copia en `backend/` no participa en este despliegue.

## Cambios

- Nuevo `GET /api/auth/turnos-disponibles`, público solo para ese método y ruta. Usa una proyección DTO con `idTurno`, `horaInicio` y `horaFinal`, sin entidades, usuarios, asignaciones ni días de trabajo.
- Incluye turnos con días configurados y horas distintas, también nocturnos. No filtra por hora o día actual: el login existente exige el turno asignado al usuario, no un horario vigente.
- Toda la ruta `/api/personal/turnos/**`, incluidas sus consultas, exige `ADMINISTRADOR`. No se deshabilitó Spring Security ni se amplió el acceso a otras rutas.
- El DTO administrativo copia los días dentro de la transacción para evitar la serialización lazy fuera de sesión; `open-in-view` continúa desactivado.
- `AuthService` consulta el nuevo endpoint y utiliza un contrato público separado. El componente de login conserva la selección obligatoria y el bloqueo del botón mientras carga o no hay turnos.
- CORS conserva un origen explícito configurable mediante `FRONTEND_ORIGIN`, con valor predeterminado `http://localhost:4300`; Compose ya lo configuraba correctamente.
- Nueva V5, exclusiva del perfil `dev`, completa los días de los tres turnos originales únicamente cuando conservan sus horarios originales y no tienen días. No borra filas, no modifica horarios personalizados y no altera migraciones aplicadas.
- Se conservó la validación de correo, contraseña, usuario activo y coincidencia del turno asignado. No se cambiaron credenciales ni se crearon usuarios en PostgreSQL para las comprobaciones.

## Pruebas y ejecución

- Maven `verify` ejecutado con `maven:3.9-eclipse-temurin-21`: 96 pruebas, cero fallos, cero errores y cero omitidas. Las pruebas automáticas usan H2; PostgreSQL se verificó adicionalmente con consultas y HTTP reales.
- Las nuevas pruebas de seguridad cubren el contrato público mínimo, turnos nocturnos, serialización administrativa sin transacción de prueba, rechazo de lecturas y escrituras anónimas, rechazo de recepcionista, JWT inválido, ruta/método públicos exactos y CORS permitido/rechazado. El test de login comprueba también el rechazo de un turno incorrecto.
- Angular: `npm test -- --watch=false`, 10 pruebas en 4 archivos aprobadas; `npm run build`, compilación de producción exitosa.
- `docker compose build api frontend` y `docker compose up -d api frontend` completados; PostgreSQL permaneció saludable y se conservó su volumen.
- Flyway aplicó V5 correctamente. Los turnos 1, 2 y 3 tienen siete días cada uno. Reejecutar su SQL devolvió `INSERT 0 0`.

| Petición real | Resultado |
| --- | --- |
| GET `/api/auth/turnos-disponibles`, sin JWT, origen 4300 | 200, tres turnos, solo tres campos, CORS correcto |
| GET `/api/personal/turnos` y `/1`, sin JWT | 401 |
| POST, PUT y DELETE administrativos, sin JWT | 401 |
| POST `/api/auth/turnos-disponibles`, sin JWT | 401 |
| Preflight de login desde 4300 | 200 |
| Preflight desde origen no autorizado | 403 |
| POST login con administrador configurado y turno 1 | 200, JWT y rol ADMINISTRADOR |
| GET turnos administrativos y reservas con ese JWT | 200 |
| Login con contraseña configurada pero turno 2 | 401 |
| GET administrativo con JWT inválido | 401 |
| Frontend `http://localhost:4300` | 200 |

Se comprobó el flujo por HTTP, sin una prueba automatizada de navegador. Los secretos y el JWT no se imprimieron ni se guardaron en el informe. Se respetaron los cambios previos del workspace y no se realizaron commits ni pushes.

Referencia técnica: [autorización y despachos de error en Spring Security 7](https://docs.spring.io/spring-security/reference/7.0/servlet/authorization/authorize-http-requests.html), [integración CORS](https://docs.spring.io/spring-security/reference/servlet/integrations/cors.html).
