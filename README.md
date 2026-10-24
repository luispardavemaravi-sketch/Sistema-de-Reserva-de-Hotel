
<div align="center">

# 🏨 HOTEL PARDA

### Sistema de Gestión y Reservación Hotelera

**Una plataforma Full Stack para modernizar la administración hotelera.**

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-22-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)

**Spring Security · JWT · Spring Data JPA · Hibernate · Flyway · TypeScript · GitHub Actions**

[📖 Documentación](docs/ARCHITECTURE.md) ·
[🔌 API REST](docs/API.md) ·
[🗃️ Base de datos](docs/DATABASE.md) ·
[🚀 Despliegue](docs/DEPLOYMENT.md)

---

</div>

## 🌟 Acerca del proyecto

**Hotel Parda** es un sistema de administración hotelera diseñado para centralizar las operaciones de un establecimiento mediante tecnologías modernas y buenas prácticas de ingeniería de software.

Combina una API REST desarrollada con **Java 21 y Spring Boot 4**, una interfaz administrativa en **Angular 22** y persistencia relacional mediante **PostgreSQL 17**.

La plataforma contempla:

- 🛎️ Recepción y control de estancias
- 📅 Reservas y disponibilidad
- 🏢 Habitaciones, pisos y tarifas
- 👥 Registro y administración de huéspedes
- 🧾 Servicios y consumos
- 💰 Finanzas y caja
- 👨‍💼 Gestión del personal
- 📊 Dashboard e indicadores operativos
- 🔐 Autenticación JWT y control de acceso

> 🚧 **Estado:** Proyecto en desarrollo activo. Login, reservas por noche y huéspedes cuentan con integración backend. Los demás módulos conservan funciones de demostración o integración parcial.

## 🛠️ Stack tecnológico

| Área | Tecnologías |
|:---|:---|
| **Backend** | Java 21, Spring Boot 4, Spring Web MVC |
| **Seguridad** | Spring Security, JWT, BCrypt |
| **Persistencia** | Spring Data JPA, Hibernate, PostgreSQL 17 |
| **Migraciones** | Flyway |
| **Frontend** | Angular 22, TypeScript, HTML5, CSS3 |
| **Comunicación** | REST API, JSON, Angular HttpClient, RxJS |
| **Pruebas** | JUnit 5, Mockito, Vitest |
| **Documentación** | Swagger UI, OpenAPI |
| **DevOps** | Docker, Docker Compose, GitHub Actions |
| **Construcción** | Maven Wrapper, npm |

## 🏗️ Arquitectura

```text
                ┌──────────────────────────┐
                │       HOTEL PARDA        │
                │       Angular 22         │
                │     localhost:4300       │
                └─────────────┬────────────┘
                              │
                          HTTP / JSON
                              │
                ┌─────────────▼────────────┐
                │      SPRING BOOT 4       │
                │       Java 21            │
                │     localhost:8080       │
                │                          │
                │ Controllers / DTOs       │
                │ Services / Security      │
                │ Repositories / JPA       │
                └─────────────┬────────────┘
                              │
                           JDBC / JPA
                              │
                ┌─────────────▼────────────┐
                │      POSTGRESQL 17       │
                │      Flyway / SQL        │
                │     localhost:5433       │
                └──────────────────────────┘
```

El backend sigue una arquitectura monolítica en capas:

**Controller → Service → Repository → PostgreSQL**

La API incorpora validaciones, autorización por roles, operaciones transaccionales y migraciones versionadas.

## 🧩 Módulos del sistema

| Módulo | Descripción | Integración |
|:---|:---|:---|
| 🔐 Autenticación | Login, JWT, roles y turnos | Implementada |
| 📊 Dashboard | Métricas y gráficos | Demo |
| 🛎️ Recepción | Rack, ocupación y estancias | Demo |
| 📅 Reservas | Crear, editar, confirmar, entrada, salida y cancelar | Parcial real |
| 🏨 Habitaciones | Inventario, pisos, tipos, estados y tarifas | Demo |
| 👥 Huéspedes | Registro, consulta, actualización y baja lógica | Real |
| 🧾 Servicios | Catálogo y consumos adicionales | Demo |
| 💳 Finanzas | Caja, pagos y comprobantes | Demo |
| 👨‍💼 Personal | Empleados, turnos y gestión | Parcial |

**Importante:** las cifras financieras y operativas mostradas en módulos de demostración no representan transacciones reales.

## ✨ Características principales

### 📅 Gestión de reservas

- Reservas por noche.
- Asociación de habitaciones y huéspedes.
- Cálculo de tarifas en backend.
- Control de disponibilidad.
- Prevención de reservas solapadas.
- Confirmación, check-in y check-out.
- Cancelación y control de estados.
- Validación de fechas y reglas de negocio.

### 🔒 Seguridad

- Spring Security.
- Autenticación basada en JWT.
- Hash de contraseñas con BCrypt.
- Autorización por roles.
- CORS configurable.
- Restricciones sobre recursos protegidos.

### 🗄️ Persistencia

- PostgreSQL 17.
- Hibernate y Spring Data JPA.
- Migraciones con Flyway.
- Validación del esquema.
- Transacciones para operaciones críticas.

## 📁 Estructura del proyecto

```text
Sistema-de-Reserva-de-Hotel/
│
├── .github/
│   └── workflows/
│       └── ci.yml
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
│
├── frontend/
│   ├── src/
│   ├── package.json
│   └── Dockerfile.dev
│
├── backend/             # Implementación heredada
├── docs/
├── .env.example
├── compose.yaml
├── Dockerfile
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

> El backend activo se encuentra en la raíz (`pom.xml` y `src/`). La carpeta `backend/` es heredada.

## 🚀 Instalación y ejecución

### 1. Clonar repositorio

```bash
git clone https://github.com/luispardavemaravi-sketch/Sistema-de-Reserva-de-Hotel.git
cd Sistema-de-Reserva-de-Hotel
```

### 2. Configurar entorno

Copia `.env.example` como `.env` y establece valores propios:

```dotenv
DB_PASSWORD=your_database_password
DB_PORT=5433
JWT_SECRET=your_secure_random_secret_at_least_32_characters
ADMIN_EMAIL=admin@example.com
ADMIN_PASSWORD=your_admin_password
```

**Nunca publiques contraseñas, tokens ni el archivo `.env`.**

### 3. Iniciar Docker

Con Docker Desktop iniciado:

```bash
docker compose config --quiet
docker compose up --build -d
docker compose ps
```

### 4. Acceder a la aplicación

| Servicio | Dirección |
|:---|:---|
| 🌐 Frontend | http://localhost:4300 |
| ⚙️ Backend | http://localhost:8080/api |
| 📚 Swagger UI | http://localhost:8080/swagger-ui.html |
| 🗄️ PostgreSQL | localhost:5433 |

El puerto 4300 permite ejecutar Hotel Parda sin interferir con aplicaciones que utilizan Angular en el puerto 4200.

El backend debe permitir el origen `http://localhost:4300` en su configuración CORS.

## 🧪 Pruebas y compilación

### Backend — Windows

```powershell
.\mvnw.cmd clean verify
```

### Backend — Linux / macOS

```bash
./mvnw clean verify
```

### Frontend

```bash
cd frontend
npm ci
npm test -- --watch=false
npm run build
```

El proyecto también incluye configuración de CI en:

`.github/workflows/ci.yml`

Los resultados documentados se encuentran en [TEST_REPORT.md](docs/TEST_REPORT.md).

## 🔌 API REST

La API se documenta mediante Swagger/OpenAPI.

**Swagger local:** http://localhost:8080/swagger-ui.html

| Método | Endpoint | Funcionalidad |
|:---|:---|:---|
| `POST` | `/api/auth/login` | Iniciar sesión |
| `GET` | `/api/habitaciones` | Consultar habitaciones |
| `GET` | `/api/huespedes` | Consultar huéspedes |
| `POST` | `/api/huespedes` | Registrar huésped |
| `GET` | `/api/reservas` | Consultar reservas |
| `POST` | `/api/reservas` | Crear reserva |
| `PUT` | `/api/reservas/{id}` | Modificar reserva |
| `POST` | `/api/reservas/{id}/confirmacion` | Confirmar reserva |
| `POST` | `/api/reservas/{id}/entrada` | Registrar entrada |
| `POST` | `/api/reservas/{id}/salida` | Registrar salida |
| `DELETE` | `/api/reservas/{id}` | Cancelar reserva |

Los endpoints protegidos requieren autenticación y permisos adecuados.

## ☁️ Despliegue

### Frontend — Vercel

| Configuración | Valor |
|:---|:---|
| Root Directory | `frontend` |
| Install Command | `npm ci` |
| Build Command | `npm run build` |
| Output Directory | `dist/hotel-admin-frontend/browser` |
| Variable de build | `PUBLIC_API_URL` |

```dotenv
PUBLIC_API_URL=https://api.tu-dominio.com/api
```

### Backend — Java / Docker

Variables requeridas según el entorno:

```text
DATABASE_URL
DB_USER
DB_PASSWORD
JWT_SECRET
FRONTEND_ORIGIN
ADMIN_EMAIL
ADMIN_PASSWORD
```

Se recomienda PostgreSQL administrado, HTTPS, secretos fuera del repositorio y copias de seguridad.

**El despliegue público aún no ha sido verificado.**

## 🗺️ Roadmap

- [x] Arquitectura inicial Spring Boot + Angular
- [x] Persistencia PostgreSQL y Flyway
- [x] Autenticación JWT
- [x] Integración de reservas por noche
- [x] Integración de huéspedes
- [ ] Integración completa de habitaciones
- [ ] Integración de recepción
- [ ] Integración de servicios
- [ ] Integración de finanzas
- [ ] Integración de personal
- [ ] Dashboard con métricas reales
- [ ] Pruebas end-to-end completas
- [ ] Despliegue público

## 📚 Documentación

| Documento | Contenido |
|:---|:---|
| [Arquitectura](docs/ARCHITECTURE.md) | Organización técnica |
| [Base de datos](docs/DATABASE.md) | Modelo y persistencia |
| [API](docs/API.md) | Contratos REST |
| [Integración](docs/FRONTEND_BACKEND_MAPPING.md) | Estado de los módulos |
| [Pruebas](docs/TEST_REPORT.md) | Verificaciones |
| [Despliegue](docs/DEPLOYMENT.md) | Configuración de publicación |

---

<div align="center">

### 💙 HOTEL PARDA

**Tecnología al servicio de una administración hotelera más eficiente.**

Desarrollado con Java, Spring Boot, Angular y PostgreSQL.

**Estado: Desarrollo activo**

</div>
