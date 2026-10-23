package pe.edu.utp.sistemadereservacionhotel.config.swagger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controlador que redirige a la interfaz de Swagger UI.
 * Acceso: http://localhost:8080/swagger-ui
 */
@RestController
public class SwaggerController {

    @GetMapping("/swagger-ui")
    public RedirectView index() {
        return new RedirectView("swagger-ui/index.html");
    }
}