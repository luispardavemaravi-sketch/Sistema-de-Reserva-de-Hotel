package pe.edu.utp.sistemadereservacionhotel.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger/OpenAPI para el Sistema de Reservación de Hotel.
 * Acceso: http://localhost:8080/swagger-ui/index.html
 */
@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Reservación Hotel API")
                        .description("API REST para la gestión de reservas hoteleras: " +
                                "habitaciones, huéspedes, empleados, check-in/out y facturación")
                        .version("v1.0.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}