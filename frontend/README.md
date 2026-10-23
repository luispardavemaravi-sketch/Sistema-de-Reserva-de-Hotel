# Hotel Parda · Administración

Panel administrativo para hoteles pequeños, hostales y estancias por horas. Está construido con Angular 22 y componentes independientes.

## Iniciar

```bash
npm install
npm start
```

Abra `http://localhost:4200/`.

## Acceso de prueba

| Cargo         | Usuario               | Contraseña |
| ------------- | --------------------- | ---------- |
| Administrador | `admin@hotel.com`     | `admin123` |
| Recepcionista | `recepcion@hotel.com` | `recep123` |
| Cajero        | `caja@hotel.com`      | `caja123`  |

El login permite escoger un turno y recordar la sesión. Mientras `/api/auth/login` no esté disponible, estas cuentas funcionan localmente. El control local de roles sirve para probar la interfaz; para producción, el backend debe validar credenciales, permisos y operaciones con JWT.

## Módulos

- **Resumen general:** ocupación, ingresos, rotación, gráficos y próximas salidas.
- **Recepción:** rack, ingreso inmediato, extensiones, consumos, traslados y salida.
- **Reservas:** estancias por horas o noches, acompañantes y acciones de cada reserva.
- **Habitaciones:** habitaciones, pisos, tipos, tarifas y equipamiento configurables.
- **Huéspedes, servicios y personal:** registros, catálogo, consumos, empleados y turnos.
- **Finanzas:** periodos, caja, movimientos, comprobantes, impuestos, promociones y medios de pago.
- **Configuración del hotel:** nombre, RUC, moneda y preferencia de modo oscuro.

La información se conserva en `localStorage` del navegador. Este proyecto es un frontend sin API ni sincronización entre dispositivos; para operación comercial compartida se requiere conectar los almacenes de datos a un backend.

## Verificar

```bash
npm run build
npm test -- --watch=false
```
