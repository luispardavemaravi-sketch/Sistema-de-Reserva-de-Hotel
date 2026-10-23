# Informe de pruebas

Ejecuciones locales el 23 de octubre de 2026:

| Prueba | Resultado |
|---|---|
| `mvnw.cmd clean verify` | Correcto; 92 pruebas Java según reportes Surefire |
| `npm run build` | Correcto, salida `frontend/dist/hotel-admin-frontend/browser` |
| `npm test -- --watch=false` | Correcto: 3 archivos, 6 pruebas |
| `npm audit --audit-level=high` | Correcto: 0 vulnerabilidades tras fijar Vite 8.3.5 |
| `docker compose config --quiet` | Correcto con variables de ejemplo en memoria |
| PostgreSQL 17 real y Flyway | Correcto con Docker Compose: V1 a V4 aplicadas; Hibernate validó el esquema |
| API de reservas con PostgreSQL | Login, lectura (3 habitaciones, 2 huéspedes, 2 reservas de desarrollo), creación, 409 por cruce, fechas contiguas, confirmación, entrada, salida y cancelación verificados |
| API de huéspedes con PostgreSQL | Crear, actualizar teléfono y dar de baja: HTTP 204 y ausencia en lista activa |
| Concurrencia | Dos POST simultáneos para la misma habitación/fechas: HTTP 200 y 409; una sola fila guardada |
| Persistencia | API reiniciada: reservas 3 `Finalizada` y 4 `Cancelada` visibles; consulta SQL directa confirmó las filas |
| Frontend servido por Compose | `GET /login` devolvió 200 y HTML Angular |
| Flujo end-to-end visual en navegador | No ejecutado; la prueba de interfaz no automatiza clics |
| Despliegue público | No ejecutado |

La suite Java incluye pruebas unitarias de reservas y una prueba HTTP para 401/login/403. La suite Angular cubre autenticación y analítica básica, pero no un recorrido end-to-end. Las credenciales del ensayo eran locales y temporales.
