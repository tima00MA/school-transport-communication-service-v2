package fs.master.asynccommunicationservice.repository;

// Importations JPA
import fs.master.asynccommunicationservice.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    // Trouve les abonnements par file
    List<Subscription> findByQueue(String queue);
}