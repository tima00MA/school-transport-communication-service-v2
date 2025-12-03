package fs.master.asynccommunicationservice.service;

import fs.master.asynccommunicationservice.dto.BusDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class BusCommunicationService {

    // On crée le RestTemplate directement ici → plus besoin d'injection
    private final RestTemplate restTemplate = new RestTemplate();

    // Valeur par défaut si pas dans application.yml
    @Value("${bus.service.url:http://localhost:8091/api/buses}")
    private String busServiceUrl;

    // Liste de tous les bus
    public List<BusDTO> getAllBuses() {
        try {
            ResponseEntity<BusDTO[]> response = restTemplate.getForEntity(busServiceUrl, BusDTO[].class);
            BusDTO[] buses = response.getBody();
            return buses != null ? List.of(buses) : Collections.emptyList();
        } catch (Exception e) {
            // Si le service est down → on retourne vide (ton app ne plante pas)
            return Collections.emptyList();
        }
    }

    // Récupérer un bus par son id
    public BusDTO getBusById(Long id) {
        try {
            return restTemplate.getForObject(busServiceUrl + "/" + id, BusDTO.class);
        } catch (Exception e) {
            // Retourne un bus "inconnu" pour éviter le crash
            BusDTO mock = new BusDTO();
            try { mock.getClass().getMethod("setId", Long.class).invoke(mock, id); } catch (Exception ignored) {}
            try { mock.getClass().getMethod("setMatricule", String.class).invoke(mock, "INCONNU"); } catch (Exception ignored) {}
            try { mock.getClass().getMethod("setCapacity", Integer.class).invoke(mock, 0); } catch (Exception ignored) {}
            return mock;
        }
    }
}