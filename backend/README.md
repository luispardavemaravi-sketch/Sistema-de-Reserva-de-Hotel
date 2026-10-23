<!-- 
  README en HTML + CSS — Sistema de Reserva de Hotel
  NOTA: GitHub elimina las etiquetas <style> en los README.md por seguridad,
  por eso todos los estilos aquí están aplicados en línea (inline) para que
  se vean correctamente al pegarlos en el README.md del repositorio.
-->

<div align="center">

<h1>🏨 Sistema de Reserva de Hotel</h1>

<p style="font-size:16px; color:#555;">
Backend REST API para la gestión hotelera de alta disponibilidad.
</p>

<p style="font-size:14px; color:#777;">
Proyecto desarrollado para el curso de <b>Diseño de Patrones (32207)</b> — Universidad Tecnológica del Perú (UTP).
</p>

<p>
<img alt="Java" src="https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk&logoColor=white">
<img alt="Spring Boot" src="https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=springboot&logoColor=white">
<img alt="PostgreSQL" src="https://img.shields.io/badge/PostgreSQL-Database-4169E1?logo=postgresql&logoColor=white">
<img alt="Maven" src="https://img.shields.io/badge/Maven-3.8%2B-C71A36?logo=apachemaven&logoColor=white">
<img alt="Tests" src="https://img.shields.io/badge/Tests-JUnit%205%20%2B%20Mockito-25A162?logo=junit5&logoColor=white">
</p>

</div>

<hr style="border:none; border-top:1px solid #e1e4e8; margin:24px 0;">

<h2>📋 Descripción</h2>

<p>
API REST robusta desarrollada con <b>Spring Boot</b>, diseñada para la administración integral de un ecosistema hotelero.
La arquitectura garantiza la consistencia de datos mediante transaccionalidad, manejo centralizado de excepciones y
validación de reglas de negocio en la capa de servicio.
</p>

<h2>🛠️ Tecnologías utilizadas</h2>

<table style="width:100%; border-collapse:collapse; margin:12px 0;">
  <tr style="background-color:#f6f8fa;">
    <th style="text-align:left; padding:8px 12px; border:1px solid #d0d7de;">Tecnología</th>
    <th style="text-align:left; padding:8px 12px; border:1px solid #d0d7de;">Uso</th>
  </tr>
  <tr>
    <td style="padding:8px 12px; border:1px solid #d0d7de;"><b>Java 17+</b></td>
    <td style="padding:8px 12px; border:1px solid #d0d7de;">Lenguaje base del proyecto</td>
  </tr>
  <tr style="background-color:#f6f8fa;">
    <td style="padding:8px 12px; border:1px solid #d0d7de;"><b>Spring Boot 3.x</b></td>
    <td style="padding:8px 12px; border:1px solid #d0d7de;">Framework principal de la API</td>
  </tr>
  <tr>
    <td style="padding:8px 12px; border:1px solid #d0d7de;"><b>Spring Data JPA / Hibernate</b></td>
    <td style="padding:8px 12px; border:1px solid #d0d7de;">Persistencia y ORM</td>
  </tr>
  <tr style="background-color:#f6f8fa;">
    <td style="padding:8px 12px; border:1px solid #d0d7de;"><b>PostgreSQL</b></td>
    <td style="padding:8px 12px; border:1px solid #d0d7de;">Base de datos relacional</td>
  </tr>
  <tr>
    <td style="padding:8px 12px; border:1px solid #d0d7de;"><b>Lombok</b></td>
    <td style="padding:8px 12px; border:1px solid #d0d7de;">Reducción de boilerplate</td>
  </tr>
  <tr style="background-color:#f6f8fa;">
    <td style="padding:8px 12px; border:1px solid #d0d7de;"><b>Jakarta Validation</b></td>
    <td style="padding:8px 12px; border:1px solid #d0d7de;">Validación de datos de entrada</td>
  </tr>
  <tr>
    <td style="padding:8px 12px; border:1px solid #d0d7de;"><b>JUnit 5 / Mockito</b></td>
    <td style="padding:8px 12px; border:1px solid #d0d7de;">Suite de pruebas unitarias implementada</td>
  </tr>
</table>

<h2>🏗️ Arquitectura y Patrones Aplicados</h2>

<p>
El proyecto implementa una <b>arquitectura en capas</b> para garantizar el desacoplamiento y la mantenibilidad.
Se han integrado los siguientes patrones de diseño (GoF):
</p>

<table style="width:100%; border-collapse:collapse; margin:12px 0;">
  <tr style="background-color:#f6f8fa;">
    <th style="text-align:left; padding:8px 12px; border:1px solid #d0d7de; width:15%;">Patrón</th>
    <th style="text-align:left; padding:8px 12px; border:1px solid #d0d7de;">Aplicación en el Sistema</th>
  </tr>
  <tr>
    <td style="padding:8px 12px; border:1px solid #d0d7de;"><b>Singleton</b></td>
    <td style="padding:8px 12px; border:1px solid #d0d7de;">Gestión de beans mediante el contenedor de Spring (<code>@Service</code>, <code>@Repository</code>), asegurando una única instancia por tipo.</td>
  </tr>
  <tr style="background-color:#f6f8fa;">
    <td style="padding:8px 12px; border:1px solid #d0d7de;"><b>Builder</b></td>
    <td style="padding:8px 12px; border:1px solid #d0d7de;">Implementado para la construcción de objetos complejos (Reservas/Detalles) y configuraciones de DTOs, evitando constructores con múltiples parámetros.</td>
  </tr>
  <tr>
    <td style="padding:8px 12px; border:1px solid #d0d7de;"><b>Factory</b></td>
    <td style="padding:8px 12px; border:1px solid #d0d7de;">Utilizado para la instanciación de servicios y documentos de pago, permitiendo desacoplar la lógica de creación de la lógica de negocio.</td>
  </tr>
  <tr style="background-color:#f6f8fa;">
    <td style="padding:8px 12px; border:1px solid #d0d7de;"><b>Decorator</b></td>
    <td style="padding:8px 12px; border:1px solid #d0d7de;">Aplicado para extender dinámicamente las funcionalidades de los servicios adicionales (ej. agregar servicios premium a una reserva base).</td>
  </tr>
  <tr>
    <td style="padding:8px 12px; border:1px solid #d0d7de;"><b>Observer</b></td>
    <td style="padding:8px 12px; border:1px solid #d0d7de;">Implementado para el desacoplamiento de eventos; el sistema reacciona a cambios de estado (ej. cambio de estado de reserva) sin acoplar los servicios de notificación o auditoría.</td>
  </tr>
</table>

<h3>Estructura de paquetes</h3>

<pre style="background-color:#f6f8fa; border:1px solid #d0d7de; border-radius:6px; padding:14px; overflow-x:auto; font-family:'Courier New', monospace; font-size:13px;">
src/main/java/.../sistemareservahotel
├── controller      # Endpoints REST (API Layer)
├── service         # Lógica de negocio y orquestación
├── repository      # Persistencia de datos (Spring Data JPA)
├── model           # Entidades JPA (Dominio)
├── dto             # Objetos de transferencia de datos
├── exception       # Manejo de excepciones personalizadas (Patrón Fail-Fast)
└── test            # Suite de pruebas unitarias (JUnit 5 + Mockito)
</pre>

<h2>📦 Estado de Módulos</h2>

<table style="width:100%; border-collapse:collapse; margin:12px 0;">
  <tr style="background-color:#f6f8fa;">
    <th style="text-align:left; padding:8px 12px; border:1px solid #d0d7de;">Módulo</th>
    <th style="text-align:left; padding:8px 12px; border:1px solid #d0d7de;">Estado</th>
  </tr>
  <tr>
    <td style="padding:8px 12px; border:1px solid #d0d7de;">Empleado / RRHH</td>
    <td style="padding:8px 12px; border:1px solid #d0d7de;">✅ Implementado</td>
  </tr>
  <tr style="background-color:#f6f8fa;">
    <td style="padding:8px 12px; border:1px solid #d0d7de;">Huésped</td>
    <td style="padding:8px 12px; border:1px solid #d0d7de;">✅ Implementado</td>
  </tr>
  <tr>
    <td style="padding:8px 12px; border:1px solid #d0d7de;">Reserva / Check-In / Check-Out</td>
    <td style="padding:8px 12px; border:1px solid #d0d7de;">✅ Implementado</td>
  </tr>
  <tr style="background-color:#f6f8fa;">
    <td style="padding:8px 12px; border:1px solid #d0d7de;">Gestión de Habitaciones</td>
    <td style="padding:8px 12px; border:1px solid #d0d7de;">✅ Implementado</td>
  </tr>
  <tr>
    <td style="padding:8px 12px; border:1px solid #d0d7de;">Servicios y Catálogo</td>
    <td style="padding:8px 12px; border:1px solid #d0d7de;">✅ Implementado</td>
  </tr>
  <tr style="background-color:#f6f8fa;">
    <td style="padding:8px 12px; border:1px solid #d0d7de;">Finanzas</td>
    <td style="padding:8px 12px; border:1px solid #d0d7de;">✅ Implementado</td>
  </tr>
</table>

<h2>⚙️ Configuración y ejecución</h2>

<h3>Requisitos previos</h3>
<ul>
  <li>JDK 17+</li>
  <li>PostgreSQL</li>
  <li>Maven 3.8+</li>
</ul>

<h3>Pasos de despliegue</h3>

<p><b>1. Clonar el repositorio:</b></p>
<pre style="background-color:#f6f8fa; border:1px solid #d0d7de; border-radius:6px; padding:14px; overflow-x:auto; font-family:'Courier New', monospace; font-size:13px;">git clone https://github.com/luispardavemaravi-sketch/Sistema-de-Reserva-de-Hotel.git</pre>

<p><b>2. Configurar <code>application.properties</code>:</b></p>
<pre style="background-color:#f6f8fa; border:1px solid #d0d7de; border-radius:6px; padding:14px; overflow-x:auto; font-family:'Courier New', monospace; font-size:13px;">spring.datasource.url=jdbc:postgresql://localhost:5432/hotel_db
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASS}
spring.jpa.hibernate.ddl-auto=update</pre>

<p><b>3. Ejecutar pruebas de integridad antes del despliegue:</b></p>
<pre style="background-color:#f6f8fa; border:1px solid #d0d7de; border-radius:6px; padding:14px; overflow-x:auto; font-family:'Courier New', monospace; font-size:13px;">mvn test</pre>

<p><b>4. Ejecutar el proyecto:</b></p>
<pre style="background-color:#f6f8fa; border:1px solid #d0d7de; border-radius:6px; padding:14px; overflow-x:auto; font-family:'Courier New', monospace; font-size:13px;">mvn spring-boot:run</pre>

<hr style="border:none; border-top:1px solid #e1e4e8; margin:24px 0;">

<table style="width:100%; border-collapse:collapse;">
  <tr>
    <td style="padding:8px 0;">
      <h3 style="margin-bottom:4px;">👥 Autor</h3>
      <p style="margin-top:0;">Luisangel Ronal Pardave Maravi | Desarrollador Backend</p>
    </td>
  </tr>
  <tr>
    <td style="padding:8px 0;">
      <h3 style="margin-bottom:4px;">📄 Licencia</h3>
      <p style="margin-top:0;">Proyecto académico — Universidad Tecnológica del Perú (UTP).</p>
    </td>
  </tr>
</table>
