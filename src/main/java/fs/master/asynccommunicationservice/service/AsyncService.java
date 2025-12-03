package fs.master.asynccommunicationservice.service;

import fs.master.asynccommunicationservice.model.MessageDTO;
import fs.master.asynccommunicationservice.model.MessageLog;
import fs.master.asynccommunicationservice.model.MessageLogDTO;
import fs.master.asynccommunicationservice.model.Subscription;
import fs.master.asynccommunicationservice.repository.MessageLogRepository;
import fs.master.asynccommunicationservice.repository.SubscriptionRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsyncService {

    // ON CRÉE LE RestTemplate DIRECTEMENT ICI → PLUS JAMAIS D'ERREUR D'AUTOWIRING
    private final RestTemplate restTemplate = new RestTemplate();

    private final RabbitTemplate rabbitTemplate;
    private final MessageLogRepository messageLogRepository;
    private final SubscriptionRepository subscriptionRepository;

    // Constructeur classique (pas besoin de @RequiredArgsConstructor)
    public AsyncService(RabbitTemplate rabbitTemplate,
                        MessageLogRepository messageLogRepository,
                        SubscriptionRepository subscriptionRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.messageLogRepository = messageLogRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public void publishMessage(MessageDTO messageDTO) {
        // 1. Publie dans RabbitMQ
        rabbitTemplate.convertAndSend(messageDTO.queue(), messageDTO.payload());

        // 2. Logue dans PostgreSQL
        MessageLog log = new MessageLog();
        log.setQueue(messageDTO.queue());
        log.setPayload(messageDTO.payload());
        log.setTimestamp(LocalDateTime.now());
        messageLogRepository.save(log);

        // 3. NOTIFIE TOUS LES ABONNÉS EN HTTP PUSH (le vrai asynchrone !)
        notifySubscribers(messageDTO.queue(), messageDTO.payload());
    }

    public void subscribe(Subscription subscription) {
        subscriptionRepository.save(subscription);
    }

    public List<MessageLogDTO> getMessageLogs() {
        return messageLogRepository.findAll().stream()
                .map(log -> new MessageLogDTO(
                        log.getId(),
                        log.getQueue(),
                        log.getPayload(),
                        log.getTimestamp()
                ))
                .collect(Collectors.toList());
    }

    // LA MÉTHODE MAGIQUE → NOTIFICATION PUSH VERS TOUS LES ABONNÉS
    public void notifySubscribers(String queue, String payload) {
        List<Subscription> subscriptions = subscriptionRepository.findByQueue(queue);

        if (subscriptions == null || subscriptions.isEmpty()) {
            return;
        }

        // Préparer le body HTTP avec headers JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        for (Subscription sub : subscriptions) {
            String callbackUrl = sub.getCallbackUrl();
            if (callbackUrl == null || callbackUrl.trim().isEmpty()) {
                continue;
            }

            try {
                restTemplate.postForEntity(callbackUrl, entity, String.class);
                System.out.println("PUSH envoyé → " + callbackUrl);
            } catch (Exception e) {
                // Ne jamais faire planter tout le système si un abonné est down
                System.err.println("Échec PUSH vers " + callbackUrl + " → " + e.getMessage());
            }
        }
    }
}