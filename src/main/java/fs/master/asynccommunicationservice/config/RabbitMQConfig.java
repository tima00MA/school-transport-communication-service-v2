package fs.master.asynccommunicationservice.config;

// Importations nécessaires pour RabbitMQ et Spring
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // Définitions des noms de files RabbitMQ pour les événements asynchrones
    public static final String BUS_UPDATED_QUEUE = "bus.updated"; // File pour mise à jour des bus
    public static final String STUDENT_CREATED_QUEUE = "student.created"; // File pour création d'élève
    public static final String GROUPS_CREATED_QUEUE = "groups.created"; // File pour création de groupes
    public static final String ETA_UPDATED_QUEUE = "eta.updated"; // File pour mise à jour ETA

    // Création des files RabbitMQ (durables pour persistance)
    @Bean
    public Queue busUpdatedQueue() {
        return new Queue(BUS_UPDATED_QUEUE, true); // File durable pour bus.updated
    }

    @Bean
    public Queue studentCreatedQueue() {
        return new Queue(STUDENT_CREATED_QUEUE, true); // File durable pour student.created
    }

    @Bean
    public Queue groupsCreatedQueue() {
        return new Queue(GROUPS_CREATED_QUEUE, true); // File durable pour groups.created
    }

    @Bean
    public Queue etaUpdatedQueue() {
        return new Queue(ETA_UPDATED_QUEUE, true); // File durable pour eta.updated
    }

    // Configuration du RabbitTemplate pour publier des messages JSON
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory); // Template pour envoyer des messages
        template.setMessageConverter(new Jackson2JsonMessageConverter()); // Utilise JSON pour sérialisation
        return template;
    }

}