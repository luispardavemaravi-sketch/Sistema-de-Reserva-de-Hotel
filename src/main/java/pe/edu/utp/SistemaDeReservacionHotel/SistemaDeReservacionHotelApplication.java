package pe.edu.utp.SistemaDeReservacionHotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SistemaDeReservacionHotelApplication {

    public static void main(String[] args) {
        System.out.println("Versión de Java en ejecución: " + System.getProperty("java.version"));
        SpringApplication.run(SistemaDeReservacionHotelApplication.class, args);
    }

}
