# Arquitectura

`src/main/java/pe/edu/utp/sistemadereservacionhotel` conserva el monolito original. Los paquetes `controller`, `dto`, `service`, `repository` y `model` se agrupan por habitaciones, huéspedes, reservas, servicios, personal y finanzas. `security` añade usuarios, JWT Bearer y reglas por rol; `exception` devuelve problemas HTTP.

`frontend/` contiene Angular 22 standalone con rutas diferidas. `reservas` utiliza `ReservaBackendService` y `huespedes` utiliza `HuespedService` para el contrato Java existente. Las demás pantallas conservan stores de demostración en memoria, por lo que aún requieren integración antes de producción. El directorio `backend/` es una copia anterior del backend raíz, no un segundo servicio.

El servidor acepta Bearer tokens de ocho horas. Los tokens se guardan en `sessionStorage` durante la sesión del navegador. Las contraseñas se comparan con BCrypt. CORS acepta solo `FRONTEND_ORIGIN`. La API usa rutas `/api/...`; no hay `context-path` adicional. CSRF está deshabilitado para esta API sin cookies y sin sesión del servidor.

El servicio de reservas bloquea la habitación con `PESSIMISTIC_WRITE` antes de consultar superposición; los cambios de estado bloquean también la reserva. El intervalo usa entrada inclusiva y salida exclusiva. La escritura se realiza dentro de una transacción. Dos POST simultáneos con PostgreSQL real devolvieron 200 y 409.
