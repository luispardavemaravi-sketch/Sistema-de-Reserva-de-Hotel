# Matriz de integración

| Pantalla | Acción | Endpoint / servicio | Tabla | Estado |
|---|---|---|---|---|
| Login | Iniciar sesión | `POST /api/auth/login` / `AuthController` | `usuario` | Conectado; sin acceso local simulado |
| Reservas | Listar y filtrar | `GET /api/reservas` / `ReservaService` | `reserva` | Lectura real; filtro de texto en cliente |
| Reservas | Crear/editar por noche | `POST/PUT /api/reservas` / `ReservaService` | `reserva` | Conectado |
| Reservas | Confirmar, entrada, salida y cancelar | `POST/DELETE /api/reservas/{id}/...` | `reserva`, `check_in`, `check_out` | Conectado |
| Reservas | Selección de habitación y huésped | `GET /api/habitaciones`, `GET /api/huespedes` | `habitacion`, `huesped` | Conectado |
| Reservas | Horas, extras, acompañantes | Sin contrato compatible completo | Varias | Muestra error; pendiente |
| Dashboard | Métricas | `HotelDemoStore`, `HotelAdminStore` | Ninguna | Demo, no apto para métricas reales |
| Recepción | Estancias y estados | `HotelDemoStore` | Ninguna | Demo |
| Habitaciones | Administración visual | `HotelDemoStore` | Ninguna | Demo |
| Huéspedes | Listar, crear, editar y dar de baja | `GET/POST/PUT/DELETE /api/huespedes` / `HuespedService` | `huesped` | Conectado; baja lógica |
| Servicios | Catálogo y consumos | Stores locales | Ninguna | Demo |
| Finanzas | Caja y comprobantes | Stores locales | Ninguna | Demo; no representa cobros verificados |
| Personal | Gestión | Stores locales | Ninguna | Demo |

Los servicios HTTP antiguos de `frontend/src/app/core/services/` no estaban conectados a las pantallas y sus DTO no coincidían con el backend. Se añadió `ReservaBackendService` para la pantalla de reservas. Las demás pantallas y los datos de inicio del store deben migrarse a respuestas de la API antes de declarar el sistema terminado.
