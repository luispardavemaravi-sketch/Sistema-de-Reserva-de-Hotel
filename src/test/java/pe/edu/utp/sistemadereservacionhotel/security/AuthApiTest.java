package pe.edu.utp.sistemadereservacionhotel.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthApiTest {
    @Autowired MockMvc mvc;
    @Autowired UsuarioRepository usuarios;
    @Autowired PasswordEncoder passwords;
    @Autowired JwtService jwt;

    @Test
    void apiRejectsAnonymousAndAcceptsValidLogin() throws Exception {
        mvc.perform(get("/api/reservas")).andExpect(status().isUnauthorized());
        Usuario user = new Usuario();
        user.setEmail("auth-test@example.com");
        user.setNombres("Prueba");
        user.setApellidos("Hotel");
        user.setCargo("RECEPCIONISTA");
        user.setTurnoId(1L);
        user.setPasswordHash(passwords.encode("test-password"));
        usuarios.save(user);

        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"usuario\":\"auth-test@example.com\",\"password\":\"wrong\",\"turnoId\":1}"))
                .andExpect(status().isUnauthorized());
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"usuario\":\"auth-test@example.com\",\"password\":\"test-password\",\"turnoId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.user.cargo").value("RECEPCIONISTA"));
        mvc.perform(post("/api/habitaciones").header("Authorization", "Bearer " + jwt.issue(user))
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isForbidden());
    }
}
