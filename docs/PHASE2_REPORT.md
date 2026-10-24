# Integración full stack — avance del 23 de octubre de 2026

Esta sesión entrega un incremento verificable, no la fase completa. PostgreSQL, Flyway y el recorrido desde el navegador quedan pendientes de ejecución.

## Arquitectura y entorno comprobados

- Backend activo: `pom.xml` y `src/`, Spring Boot 4.0.6, Java release 21, controladores REST → servicios transaccionales → repositorios JPA.
- `backend/` es una copia independiente heredada. Se conserva sin cambios de código.
- Angular 22 utiliza `provideHttpClient`, interceptor Bearer, guards por sesión y rol, y componentes con carga diferida. Node local: 24.15.0; npm: 11.12.1.
- Aunque `java -version` del shell devuelve 25, el registro de Maven confirma ejecución de las pruebas con Java 21.0.4. No se cambió la versión del proyecto.
- PostgreSQL 17 está declarado en Compose, puerto Windows 5433; API 8080 y Angular 4200. Flyway está habilitado y Hibernate usa `validate`; las pruebas locales usan H2 con Flyway deshabilitado.
- Docker Desktop y Compose disponibles fuera del sandbox. `docker compose config --quiet` y `docker compose ps` fallaron por `DB_PASSWORD` ausente. No existe `.env` al verificar. El usuario eligió configurar las variables localmente. No se ejecutó `up`, ni se modificaron bases o volúmenes.

## Matriz auditada

Todas las rutas de la tabla llevan el prefijo `/api`.

| Módulo | Contratos reales | Estado y alcance verificado |
|---|---|---|
| Autenticación | `POST /auth/login`, `POST /auth/usuarios`, `GET /personal/turnos` | Login HTTP real; turnos ahora obtenidos del servidor. Pruebas de login/roles, turnos arbitrarios, JWT restringido a la API y cierre ante 401. Sin E2E de navegador. |
| Reservas | `GET/POST /reservas`, `GET/PUT/DELETE /reservas/{id}`, `POST /reservas/{id}/{confirmacion,entrada,salida}` | Pantalla ya conectada para noches. Integración HTTP/JPA en H2 comprueba alta, lectura posterior, edición, solapamiento y transiciones. Alta y edición rechazan huéspedes inactivos. Horas/extras/acompañantes pendientes. |
| Huéspedes | `GET/POST /huespedes`, `GET/PUT/DELETE /huespedes/{id}` | Pantalla ya conectada; prueba HTTP de alta, edición, consulta y baja lógica conservando la fila. |
| Habitaciones | `/habitaciones`, `/habitaciones/tipo`, `/habitaciones/estado`, `/habitaciones/precios`, `/habitaciones/equipamiento`, `/pisos` | Lectura real usada por reservas y comprobada por HTTP; administración visual sigue demo. DTO de habitación no expone piso ni estado operativo. |
| Recepción | Transiciones de `/reservas/{id}/...` | Contratos disponibles y probados desde API; pantalla propia sigue demo. Usar Reservas para transiciones persistidas. |
| Servicios | `/servicios/catalogo`, `/servicios/adicionales`, `/servicios/consumo`, `/servicios/areas` | Pantalla demo. Servicio HTTP antiguo apunta a `/servicios`; requiere adaptar DTO y rutas antes de conectarlo. Consumos seleccionan el primer precio encontrado y requieren revisión de cantidades/estado de estancia. |
| Finanzas | `/finanzas/caja`, `/finanzas/comprobante`, `/finanzas/detalle-comprobante`, `/finanzas/metodo-pago`, `/finanzas/impuesto`, `/finanzas/promocion` | Pantalla demo; servicio antiguo apunta a `/comprobantes`. No se conectaron cobros ni se generaron movimientos financieros. Falta verificar contratos y reglas contables. |
| Personal | `/personal/empleados`, `/personal/turnos` | Turnos del login conectados. Gestión de empleados sigue demo; servicio antiguo de empleados apunta a `/empleados` y su modelo requiere adaptar campos. Escrituras restringidas a administrador. |
| Dashboard | Sin endpoint agregado; stores locales | Pendiente: métricas actuales no equivalen a cifras persistidas. |

`HotelDemoStore` sigue inyectado en todas las pantallas de gestión, incluidas Reservas y Huéspedes como adaptación de presentación. `HotelAdminStore` aparece en login y las demás pantallas excepto Huéspedes. Sus señales compartidas pueden mezclar datos cargados por API y demostraciones al navegar. Se agregó una advertencia visible que identifica los módulos aún demo, incluidos cobros y comprobantes.

## Cambios de esta sesión

- `.github/workflows/ci.yml`: matriz para ejecutar `clean verify` en raíz y `backend/`; conserva todas las pruebas heredadas.
- `frontend/src/app/core/services/personal/personal.service.ts`: lectura de turnos con el DTO real (`idTurno`, `horaInicio`, `horaFinal`, `diasSemana`). No se declara integrado el CRUD antiguo de empleados.
- `frontend/src/app/core/services/auth/auth.service.ts`: elimina la dependencia del catálogo demo para validar el turno; el servidor decide si corresponde al usuario. Horarios no cargados se muestran como desconocidos.
- `frontend/src/app/features/auth/login.component.ts`: selector desde API, estados de carga/error y bloqueo si no hay turnos disponibles.
- `frontend/src/app/core/services/auth/auth.service.spec.ts`: pruebas del contrato de turnos y de un ID fuera del catálogo demo.
- `frontend/src/app/core/guards/auth.interceptor.spec.ts`: evita envío de JWT a otra URL, comprueba 401 y 403.
- `frontend/src/app/shared/layout/admin-layout.component.html`: identifica explícitamente funciones demo.
- `src/main/java/pe/edu/utp/sistemadereservacionhotel/service/reserva/impl/ReservaServiceImpl.java`: rechaza huéspedes inactivos al crear/editar.
- `src/test/java/pe/edu/utp/sistemadereservacionhotel/security/ReservationFlowApiTest.java`: flujo HTTP con seguridad, JPA y base H2 exclusiva; consulta SQL posterior a las operaciones.
- Documentación: este informe, matriz y API.

Los cambios preexistentes en `.env.example` y `Hotel-Backend.code-workspace` pertenecen al usuario y se conservaron. No se realizaron commits, pushes ni despliegues.

## Resultados y límites

- Backend principal: `.\mvnw.cmd clean verify`, **93 pruebas, 0 fallos, 0 errores, 0 omitidas**.
- Backend heredado: `.\mvnw.cmd clean verify` desde `backend/`, **90 pruebas, 0 fallos, 0 errores, 0 omitidas**.
- Frontend: `npm ci` completado; `npm test -- --watch=false`, **10 pruebas en 4 archivos, todas aprobadas**; `npm run build` completado.
- Las restricciones del sandbox impidieron inicialmente ejecutar el wrapper y resolver archivos Angular; las ejecuciones autorizadas fuera del sandbox completaron la validación.
- La fixture inicial de la nueva prueba HTTP tenía un documento de 7 caracteres y fue rechazada con 400. Se corrigió a 8 caracteres; no se debilitó la validación.
- La nueva prueba demuestra persistencia H2 entre peticiones y transiciones, no persistencia PostgreSQL, migraciones, concurrencia real ni recarga del navegador Angular.
- No se ejecutaron pruebas visuales, AXE ni E2E. No se verificaron respuestas 500 mediante fallo inducido.
- GitHub Actions: YAML corregido localmente; no se disparó ni se consultó una ejecución remota. No se afirma que CI remoto esté verde.

## Pendientes y riesgos

1. Configurar secretos y verificar Compose/Flyway/PostgreSQL en una base de desarrollo aislada. No usar datos reales para el recorrido funcional.
2. Completar el flujo Angular en navegador: login → huéspedes/habitaciones → reserva → recarga → confirmación → entrada → salida, y comprobar la fila en PostgreSQL.
3. Migrar estado compartido de demostración a servicios/estado por módulo; separar métricas reales de catálogo y configuración locales.
4. El formulario de huéspedes une nombre y apellidos y los divide por la última palabra; puede alterar apellidos compuestos al editar. Requiere campos separados.
5. La restauración de sesión no comprueba localmente la expiración JWT; la API invalida el acceso y el interceptor cierra la sesión con 401. Tras recargar, los horarios del turno aparecen desconocidos hasta cargar su catálogo.
6. Las transiciones controlan el estado de reserva, pero no validan el día efectivo de entrada ni la ocupación física por estancias prolongadas. Definir esas reglas antes de completar Recepción.
7. La nueva restricción de huésped inactivo se comprueba por HTTP para alta; falta un caso específico de edición con huésped inactivo y pruebas concurrentes de baja/reserva.
8. Servicios, Finanzas y Personal tienen rutas reales pero contratos frontend desalineados. No conectar escrituras por simple coincidencia de nombres. En finanzas falta verificar caja, importes y comprobantes antes de habilitar operaciones reales.

## Ejecución local

Desde la raíz, crear `.env` a partir de `.env.example` solo si todavía no existe. Configurar localmente `DB_PASSWORD`, `JWT_SECRET` (mínimo 32 caracteres), `ADMIN_EMAIL`, `ADMIN_PASSWORD`. No pegar secretos en el chat ni versionar `.env`. Compose usa el perfil dev; utilizar una base de desarrollo.

```powershell
docker compose config --quiet
docker compose up --build -d
docker compose ps
docker compose logs --tail 100 api db
```

Abrir `http://localhost:4200`; API `http://localhost:8080/api`; Swagger `http://localhost:8080/swagger-ui.html`. Iniciar sesión con las credenciales configuradas y el turno asignado. PostgreSQL se publica en `localhost:5433`, base y usuario `hotel`.

Para ejecutar la API desde Windows contra ese PostgreSQL, establecer `DATABASE_URL=jdbc:postgresql://localhost:5433/hotel`, `DB_USER=hotel`, `DB_PASSWORD`, `JWT_SECRET`, `ADMIN_EMAIL` y `ADMIN_PASSWORD` en la sesión. No iniciar a la vez API Docker y API manual en el puerto 8080. `.env` se carga por Compose; Maven no lo importa automáticamente.

```powershell
.\mvnw.cmd spring-boot:run
# Otra terminal:
cd frontend
npm ci
npm start
```

Pruebas desde raíz:

```powershell
.\mvnw.cmd clean verify
Push-Location backend
.\mvnw.cmd clean verify
Pop-Location
Push-Location frontend
npm ci
npm test -- --watch=false
npm run build
Pop-Location
```

Para producción, `PUBLIC_API_URL=https://SU-API/api` es leído por `frontend/scripts/write-env.mjs` durante `npm run build`; no hay archivo environment de producción separado. Configurar `FRONTEND_ORIGIN` en el backend para el origen real. No hay despliegue realizado en esta sesión.
