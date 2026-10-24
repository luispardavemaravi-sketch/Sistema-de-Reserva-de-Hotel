package pe.edu.utp.sistemadereservacionhotel.security;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.utp.sistemadereservacionhotel.dto.personal.TurnoDisponibleDTO;
import pe.edu.utp.sistemadereservacionhotel.repository.personal.TurnoRepository;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UsuarioRepository usuarios;
    private final PasswordEncoder passwords;
    private final JwtService jwt;
    private final TurnoRepository turnos;

    @GetMapping("/turnos-disponibles")
    public List<TurnoDisponibleDTO> turnosDisponibles() {
        return turnos.findDisponiblesParaLogin();
    }

    public record LoginRequest(@NotBlank String usuario, @NotBlank String password, @NotNull Long turnoId) {}
    public record UserView(Long id, String nombres, String apellidos, String numeroDocumento,
                           String correo, String cargo, boolean activo, Long turnoId) {}
    public record LoginResponse(UserView user, String token, Long turnoId) {}
    public record CreateUserRequest(@Email @NotBlank String correo, @NotBlank String nombres,
                                    @NotBlank String apellidos, @NotBlank String password,
                                    @NotBlank String cargo, @NotNull Long turnoId) {}

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        Usuario user = usuarios.findByEmailIgnoreCase(request.usuario()).filter(Usuario::isActivo)
                .filter(u -> passwords.matches(request.password(), u.getPasswordHash()))
                .filter(u -> u.getTurnoId().equals(request.turnoId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));
        return new LoginResponse(view(user), jwt.issue(user), user.getTurnoId());
    }

    @PostMapping("/usuarios")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public UserView create(@Valid @RequestBody CreateUserRequest request) {
        if (!java.util.Set.of("ADMINISTRADOR", "RECEPCIONISTA", "CAJERO").contains(request.cargo()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cargo inválido");
        if (usuarios.findByEmailIgnoreCase(request.correo()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Correo ya registrado");
        Usuario user = new Usuario();
        user.setEmail(request.correo().trim().toLowerCase());
        user.setNombres(request.nombres());
        user.setApellidos(request.apellidos());
        user.setPasswordHash(passwords.encode(request.password()));
        user.setCargo(request.cargo());
        user.setTurnoId(request.turnoId());
        return view(usuarios.save(user));
    }

    private UserView view(Usuario user) {
        return new UserView(user.getId(), user.getNombres(), user.getApellidos(), "",
                user.getEmail(), user.getCargo(), user.isActivo(), user.getTurnoId());
    }
}
