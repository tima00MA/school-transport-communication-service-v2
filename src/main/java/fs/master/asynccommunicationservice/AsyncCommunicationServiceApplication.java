package fs.master.asynccommunicationservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AsyncCommunicationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsyncCommunicationServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(@Value("${server.port}") String port) {
        return args -> {
            System.out.println("==========================================");
            System.out.println("TON APP EST LANCÉE SUR LE PORT : " + port);
            System.out.println("→ Swagger : http://localhost:" + port + "/swagger-ui.html");
            System.out.println("→ Postman : http://localhost:" + port + "/api/notifications/send-async");
            System.out.println("==========================================");
        };
    }
}