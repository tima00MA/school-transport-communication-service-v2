package fs.master.asynccommunicationservice.model;

// Importations JPA
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String serviceId; // ID du microservice
    private String queue; // File RabbitMQ
    private String callbackUrl; // URL de callback

    public Subscription() {}

    public Subscription(String serviceId, String queue, String callbackUrl) {
        this.serviceId = serviceId;
        this.queue = queue;
        this.callbackUrl = callbackUrl;
    }
}