package fs.master.asynccommunicationservice.service;

import fs.master.asynccommunicationservice.dto.NotificationHistoryDTO;
import fs.master.asynccommunicationservice.dto.NotificationTypeDTO;
import fs.master.asynccommunicationservice.dto.NotificationSubscriptionDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class NotificationCommunicationService {

    // On crée le RestTemplate ici → plus besoin d'injection
    private final RestTemplate restTemplate = new RestTemplate();

    // Valeur par défaut si pas dans application.yml
    @Value("${notification.service.url:http://localhost:8090/notifications}")
    private String notificationServiceUrl;

    /**
     * Récupérer l'historique des notifications d'un utilisateur
     */
    public List<NotificationHistoryDTO> getNotificationHistoryByUserId(String userId) {
        String url = notificationServiceUrl + "/history/" + userId;
        try {
            NotificationHistoryDTO[] arr = restTemplate.getForObject(url, NotificationHistoryDTO[].class);
            return arr != null ? List.of(arr) : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * Récupérer tous les types de notifications
     */
    public List<NotificationTypeDTO> getAllNotificationTypes() {
        String url = notificationServiceUrl + "/types";
        try {
            NotificationTypeDTO[] arr = restTemplate.getForObject(url, NotificationTypeDTO[].class);
            return arr != null ? List.of(arr) : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * Récupérer les abonnements d'un utilisateur
     */
    public List<NotificationSubscriptionDTO> getUserSubscriptions(String userId) {
        String url = notificationServiceUrl + "/subscriptions/" + userId;
        try {
            NotificationSubscriptionDTO[] arr = restTemplate.getForObject(url, NotificationSubscriptionDTO[].class);
            return arr != null ? List.of(arr) : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}