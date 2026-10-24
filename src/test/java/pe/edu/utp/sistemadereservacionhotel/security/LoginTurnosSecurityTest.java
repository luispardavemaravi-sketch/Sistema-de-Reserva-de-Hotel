package pe.edu.utp.sistemadereservacionhotel.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.utp.sistemadereservacionhotel.model.personal.Turno;
import pe.edu.utp.sistemadereservacionhotel.repository.personal.TurnoRepository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LoginTurnosSecurityTest {
    @Autowired MockMvc mvc;
    @Autowired TurnoRepository turnos;
    @Autowired UsuarioRepository usuarios;
    @Autowired JwtService jwt;

    // Deliberately no test transaction: JSON serialization must work after the service returns.
    @Test
    void publicSelectorExposesOnlyIdAndTimesAndAdminListSerializesDays() throws Exception {
        Turno turno = turnos.saveAndFlush(new Turno(null, LocalTime.of(23, 0),
                LocalTime.of(7, 0), Set.of(DayOfWeek.MONDAY)));
        Usuario admin = new Usuario();
        admin.setEmail("shift-security@example.invalid");
        admin.setNombres("Security");
        admin.setApellidos("Test");
        admin.setCargo("ADMINISTRADOR");
        admin.setTurnoId(turno.getIdTurno());
        admin.setPasswordHash("unused-test-hash");
        usuarios.saveAndFlush(admin);
        try {
            mvc.perform(get("/api/auth/turnos-disponibles"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[?(@.idTurno == " + turno.getIdTurno() + ")]").value(hasSize(1)))
                    .andExpect(jsonPath("$[0].*").value(hasSize(3)))
                    .andExpect(jsonPath("$[0].horaInicio").exists())
                    .andExpect(jsonPath("$[0].horaFinal").exists())
                    .andExpect(jsonPath("$[0].diasSemana").doesNotExist());
            mvc.perform(get("/api/personal/turnos/" + turno.getIdTurno())
                            .header("Authorization", "Bearer " + jwt.issue(admin)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.diasSemana[0]").value("MONDAY"));
            admin.setCargo("RECEPCIONISTA");
            usuarios.saveAndFlush(admin);
            mvc.perform(get("/api/personal/turnos")
                            .header("Authorization", "Bearer " + jwt.issue(admin)))
                    .andExpect(status().isForbidden());
        } finally {
            usuarios.deleteById(admin.getId());
            turnos.deleteById(turno.getIdTurno());
        }
    }

    @Test
    void administrativeReadsAndWritesRequireAuthentication() throws Exception {
        for (String path : new String[]{"", "/1", "/dia/MONDAY", "/activo-en/08:00"}) {
            mvc.perform(get("/api/personal/turnos" + path)).andExpect(status().isUnauthorized());
        }
        mvc.perform(post("/api/personal/turnos").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
        mvc.perform(put("/api/personal/turnos/1").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
        mvc.perform(delete("/api/personal/turnos/1")).andExpect(status().isUnauthorized());
        mvc.perform(post("/api/auth/turnos-disponibles")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/auth/turnos-disponibles/1")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/personal/turnos").header("Authorization", "Bearer invalid"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void corsAllowsFrontendAndRejectsOtherOrigins() throws Exception {
        mvc.perform(options("/api/auth/login").header("Origin", "http://localhost:4300")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Access-Control-Request-Headers", "content-type"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4300"));
        mvc.perform(get("/api/auth/turnos-disponibles").header("Origin", "http://localhost:4300"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4300"));
        mvc.perform(options("/api/auth/login").header("Origin", "https://untrusted.example")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isForbidden());
    }
}
