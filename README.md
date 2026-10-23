# Sistema de Reservación de Hotel

Monolito Spring Boot 4 / Java 21 con API REST, PostgreSQL y panel Angular 22. El código raíz (`pom.xml`, `src/`) es el backend activo; `backend/` es una copia heredada y no participa en los comandos de este documento.

## Estado actual

El backend tiene entidades para habitaciones, huéspedes, reservas, personal, servicios y finanzas. PostgreSQL usa migraciones Flyway y Hibernate valida el esquema. El login del panel consulta la API; **Reservas** y **Huéspedes** utilizan datos reales. Las operaciones de reserva por noche crean, modifican, confirman, registran entrada y salida, y cancelan en el servidor. Las demás pantallas todavía usan `HotelDemoStore` / `HotelAdminStore` con datos temporales de demostración. La interfaz de reservas por horas, extras y acompañantes muestra que su API aún no está disponible. El sistema completo **no está terminado** ni desplegado.

## Requisitos

- Java 21, Maven Wrapper y Node 24 con npm; o Docker Compose.
- PostgreSQL 17 para ejecución normal.
- `JWT_SECRET` de al menos 32 caracteres; `DB_PASSWORD`, `ADMIN_EMAIL` y `ADMIN_PASSWORD` para Compose.

## Inicio con Docker

1. Copiar `.env.example` a `.env` y sustituir los valores de ejemplo por secretos propios.
2. Ejecutar `docker compose up --build` desde la raíz.
3. Abrir `http://localhost:4200`; iniciar sesión con `ADMIN_EMAIL`, `ADMIN_PASSWORD` y el turno 1.
4. Swagger UI: `http://localhost:8080/swagger-ui.html`.

Compose inicia PostgreSQL con volumen, API y servidor de desarrollo Angular. Publica PostgreSQL en `localhost:5433` para evitar conflictos con instalaciones locales. Activa el perfil `dev`, que carga habitaciones, huéspedes y reservas reproducibles mediante Flyway. La base se crea en el primer arranque. Docker Desktop debe estar iniciado.

## Inicio manual

Crear la base `hotel` en PostgreSQL y exportar `DATABASE_URL=jdbc:postgresql://localhost:5432/hotel`, `DB_USER`, `DB_PASSWORD`, `JWT_SECRET`, `ADMIN_EMAIL` y `ADMIN_PASSWORD`. En PowerShell se pueden establecer como `$env:NOMBRE='valor'` para la sesión. Ejecutar `./mvnw spring-boot:run` (`.\mvnw.cmd spring-boot:run` en Windows). En otra terminal: `cd frontend`, `npm ci`, `npm start`. La API utiliza el puerto 8080 y Angular 4200. `FRONTEND_ORIGIN` permite otro origen CORS.

Para cargar datos de desarrollo en la ejecución manual, definir `SPRING_PROFILES_ACTIVE=dev` antes del primer arranque. El usuario administrador inicial se crea solo si `ADMIN_EMAIL` y `ADMIN_PASSWORD` están definidos y ese correo no existe. No se incluyen contraseñas de demostración. Un administrador puede crear otros usuarios con `POST /api/auth/usuarios`.

## Arquitectura y datos

Los controladores reciben DTO, los servicios aplican reglas y los repositorios JPA acceden a PostgreSQL. `V1__initial_schema.sql` contiene las tablas de las entidades heredadas; V2 añade estados e integridad de reservas; V3 añade usuarios con hash BCrypt. El intervalo de ocupación es `[entrada, salida)` y el servicio bloquea la fila de la habitación antes de comprobar cruces. El frontend usa JWT Bearer durante la sesión del navegador. Véanse [arquitectura](docs/ARCHITECTURE.md), [base de datos](docs/DATABASE.md), [API](docs/API.md) y [matriz de integración](docs/FRONTEND_BACKEND_MAPPING.md).

## Pruebas

- Backend: `.\mvnw.cmd clean verify` en Windows o `./mvnw clean verify` en Unix.
- Frontend: `cd frontend && npm test -- --watch=false && npm run build`.
- La prueba de contexto usa H2; además se ejecutó Compose con PostgreSQL 17 para verificar migraciones, persistencia y concurrencia de reservas desde la API.

Véase [informe de pruebas](docs/TEST_REPORT.md) para los resultados ejecutados y sus límites.

## Despliegue

Vercel: proyecto con raíz `frontend`, `npm ci`, `npm run build`, salida `dist/hotel-admin-frontend/browser` y variable de build `PUBLIC_API_URL=https://SU-API/api`. Se incluye `vercel.json` para rutas SPA. El backend puede ejecutarse en un hosting de contenedores con PostgreSQL administrado; necesita `DATABASE_URL`, `DB_USER`, `DB_PASSWORD`, `JWT_SECRET`, `FRONTEND_ORIGIN` y, para crear el primer administrador, `ADMIN_EMAIL`/`ADMIN_PASSWORD`. Véase [despliegue](docs/DEPLOYMENT.md). No hay URL pública verificada.

## Problemas frecuentes

- Error de conexión al iniciar: verificar PostgreSQL y `DATABASE_URL`.
- 401: token ausente, vencido o credenciales/turno incorrectos.
- 403: rol insuficiente para la ruta.
- Error CORS: `FRONTEND_ORIGIN` debe coincidir exactamente con el origen del panel.
- Docker Compose no conecta: iniciar Docker Desktop y comprobar `.env`.
