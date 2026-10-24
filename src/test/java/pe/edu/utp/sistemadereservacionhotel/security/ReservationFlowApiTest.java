package pe.edu.utp.sistemadereservacionhotel.security;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** HTTP + JPA integration with an isolated H2 database; does not validate PostgreSQL/Flyway. */
@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:reservation-flow;DB_CLOSE_DELAY=-1")
@AutoConfigureMockMvc
class ReservationFlowApiTest {
    @Autowired MockMvc mvc;
    @Autowired JdbcTemplate jdbc;
    @Autowired UsuarioRepository usuarios;
    @Autowired PasswordEncoder passwords;
    private String bearer;

    @BeforeEach
    void prepare() throws Exception {
        jdbc.update("INSERT INTO estado_reserva(id_estado, es_modificable, nombre_estado) VALUES (1, true, 'Pendiente')");
        // Explicit column names keep fixtures independent of Hibernate column ordering.
        jdbc.update("INSERT INTO estado_reserva(id_estado, es_modificable, nombre_estado) VALUES (2,true,'Confirmada'),(3,false,'Cancelada'),(4,false,'En curso'),(5,false,'Finalizada')");
        jdbc.update("INSERT INTO tipo_habitacion(id_tipo,nombre,capacidad_maxima,precio_base) VALUES (100,'API test',2,100)");
        jdbc.update("INSERT INTO habitacion(id_habitacion,numero_habitacion,precio_actual,estado_activo,id_tipo) VALUES (100,'TEST-100',100,true,100)");
        Usuario user = new Usuario();
        user.setEmail("flow@example.com");
        user.setNombres("Test");
        user.setApellidos("Flow");
        user.setCargo("RECEPCIONISTA");
        user.setTurnoId(1L);
        user.setPasswordHash(passwords.encode("test-password"));
        usuarios.saveAndFlush(user);
        String response = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"usuario\":\"flow@example.com\",\"password\":\"test-password\",\"turnoId\":1}"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        bearer = "Bearer " + JsonPath.<String>read(response, "$.token");
    }

    @Test
    void persistsGuestAndReservationAndEnforcesLifecycleAndPermissions() throws Exception {
        String guest = "{\"nombre\":\"Ana\",\"apellidos\":\"Prueba\",\"documentoIdentidad\":\"TEST1234\",\"email\":\"guest@example.com\",\"telefono\":\"999999999\"}";
        String created = mvc.perform(post("/api/huespedes").header("Authorization", bearer)
                .contentType(MediaType.APPLICATION_JSON).content(guest))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Number guestId = JsonPath.read(created, "$.idHuesped");
        mvc.perform(put("/api/huespedes/" + guestId).header("Authorization", bearer)
                .contentType(MediaType.APPLICATION_JSON).content(guest.replace("Ana", "Andrea")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.nombre").value("Andrea"));
        mvc.perform(get("/api/huespedes").header("Authorization", bearer)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Andrea"));
        mvc.perform(get("/api/habitaciones").header("Authorization", bearer)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroHabitacion").value("TEST-100"));

        String body = "{\"idHuesped\":" + guestId + ",\"idHabitacion\":100,\"fechaEntradaPlanificada\":\"2030-01-01\",\"fechaSalidaPlanificada\":\"2030-01-03\"}";
        String reservation = mvc.perform(post("/api/reservas").header("Authorization", bearer)
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk())
                .andExpect(jsonPath("$.montoTotalEstimado").value(200))
                .andReturn().getResponse().getContentAsString();
        Number id = JsonPath.read(reservation, "$.idReserva");
        assertEquals(1, jdbc.queryForObject("SELECT count(*) FROM reserva WHERE id_reserva=?", Integer.class, id));
        mvc.perform(get("/api/reservas/" + id).header("Authorization", bearer))
                .andExpect(status().isOk()).andExpect(jsonPath("$.estadoReserva").value("Pendiente"));
        mvc.perform(post("/api/reservas").header("Authorization", bearer)
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isConflict());
        mvc.perform(post("/api/reservas/" + id + "/entrada").header("Authorization", bearer))
                .andExpect(status().isConflict());
        mvc.perform(put("/api/reservas/" + id).header("Authorization", bearer)
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk());
        for (String[] step : new String[][]{{"confirmacion", "Confirmada"}, {"entrada", "En curso"}, {"salida", "Finalizada"}}) {
            mvc.perform(post("/api/reservas/" + id + "/" + step[0]).header("Authorization", bearer))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.estadoReserva").value(step[1]));
        }
        assertEquals(1, jdbc.queryForObject("SELECT count(*) FROM check_in WHERE id_reserva=?", Integer.class, id));
        assertEquals(1, jdbc.queryForObject("SELECT count(*) FROM check_out WHERE id_reserva=?", Integer.class, id));
        mvc.perform(delete("/api/reservas/" + id).header("Authorization", bearer)).andExpect(status().isConflict());
        mvc.perform(delete("/api/huespedes/" + guestId).header("Authorization", bearer)).andExpect(status().isNoContent());
        assertEquals(false, jdbc.queryForObject("SELECT estado_activo FROM huesped WHERE id_huesped=?", Boolean.class, guestId));
        mvc.perform(post("/api/reservas").header("Authorization", bearer)
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isConflict());
        mvc.perform(get("/api/reservas")).andExpect(status().isUnauthorized());
        mvc.perform(post("/api/habitaciones").header("Authorization", bearer)
                .contentType(MediaType.APPLICATION_JSON).content("{}" )).andExpect(status().isForbidden());
    }
}
