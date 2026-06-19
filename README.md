# 🏨 Sistema de Reserva de Hotel
 
Backend REST API para la gestión hotelera: reservas, huéspedes, habitaciones y empleados.
 
Proyecto desarrollado como parte del curso de **Diseño de Patrones (32207)** — Universidad Tecnológica del Perú (UTP).
 
---
 
## 📋 Descripción
 
API REST construida con **Spring Boot** que permite administrar las operaciones principales de un hotel, incluyendo el registro de empleados, huéspedes, habitaciones y el ciclo de vida completo de una reserva (creación, confirmación, cancelación, etc.).
 
---
 
## 🛠️ Tecnologías utilizadas
 
- **Java**
- **Spring Boot**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **Lombok**
- **Jakarta Validation**
---
 
## 🏗️ Arquitectura
 
El proyecto sigue una arquitectura en capas (layered architecture), organizada por módulos:
 
```
src/main/java/.../sistemareservahotel
├── controller      # Controladores REST
├── service         # Lógica de negocio (interfaces + implementaciones)
├── repository      # Acceso a datos (Spring Data JPA)
├── entity          # Entidades JPA (modelo de dominio)
├── dto             # Objetos de transferencia de datos
└── exception        # Manejo de excepciones personalizadas
```
 
### Patrones de diseño aplicados
 
- **Singleton** — *(agregar breve descripción de dónde se aplica)*
- **Patrón creacional adicional** — *(agregar breve descripción, ej. Builder / Factory Method)*
---
 
## 📦 Módulos implementados
 
| Módulo | Estado |
|---|---|
| Empleado | ✅ Implementado |
| Huésped | ✅ Implementado |
| Reserva | ✅ Implementado |
| EstadoReserva | ✅ Implementado |
| Habitación | ⏳ Pendiente |
 
---
 
## ⚙️ Configuración y ejecución
 
### Requisitos previos
 
- JDK 17+
- PostgreSQL instalado y corriendo
- Maven 3.8+
### Pasos
 
1. Clonar el repositorio:
```bash
   git clone https://github.com/luispardavemaravi-sketch/Sistema-de-Reserva-de-Hotel.git
```
 
2. Configurar la base de datos en `src/main/resources/application.properties`:
```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/hotel_db
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_password
   spring.jpa.hibernate.ddl-auto=update
```
 
3. Ejecutar el proyecto:
```bash
   mvn spring-boot:run
```
 
La API quedará disponible en `http://localhost:8080`.
 
---
 
## 📊 Diagramas
 
*(agregar enlace o imagen del diagrama de paquetes/clases una vez esté terminado)*
 
---
 
## 👥 Equipo
 
| Nombre | Rol |
|---|---|
| Luisangel | Desarrollador Backend / Diagramas |
| — | — |
| — | — |
| — | — |
 
---
 
## 📄 Licencia
 
Proyecto académico — Universidad Tecnológica del Perú (UTP).
