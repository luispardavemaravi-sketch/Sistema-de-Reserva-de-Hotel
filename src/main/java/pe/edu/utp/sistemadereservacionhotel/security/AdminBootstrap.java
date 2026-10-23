package pe.edu.utp.sistemadereservacionhotel.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminBootstrap implements CommandLineRunner {
    private final UsuarioRepository usuarios;
    private final PasswordEncoder passwords;
    @Value("${ADMIN_EMAIL:}") private String email;
    @Value("${ADMIN_PASSWORD:}") private String password;

    @Override
    public void run(String... args) {
        if (email.isBlank() || password.isBlank() || usuarios.findByEmailIgnoreCase(email).isPresent()) return;
        Usuario admin = new Usuario();
        admin.setEmail(email.trim().toLowerCase());
        admin.setNombres("Administrador");
        admin.setApellidos("Hotel");
        admin.setPasswordHash(passwords.encode(password));
        admin.setCargo("ADMINISTRADOR");
        admin.setTurnoId(1L);
        usuarios.save(admin);
    }
}
