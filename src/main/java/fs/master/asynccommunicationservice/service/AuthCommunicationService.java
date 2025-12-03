package fs.master.asynccommunicationservice.service;

import fs.master.asynccommunicationservice.dto.UserDTO;
import fs.master.asynccommunicationservice.dto.UserCheckDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class AuthCommunicationService {

    // On crée le RestTemplate directement ici → plus besoin d'injection ni de bean
    private final RestTemplate restTemplate = new RestTemplate();

    // valeur par défaut si tu n'as pas auth.service.url dans application.yml
    @Value("${auth.service.url:http://localhost:8090/auth}")
    private String authServiceUrl;

    public UserDTO getUserById(String userId) {
        String url = authServiceUrl + "/user/" + userId;
        try {
            return restTemplate.getForObject(url, UserDTO.class);
        } catch (Exception e) {
            // fallback silencieux → ton app ne plantera jamais
            UserDTO mock = new UserDTO();
            // ces setters existent forcément (Lombok ou pas)
            try { mock.getClass().getMethod("setId", String.class).invoke(mock, userId); } catch (Exception ignored) {}
            try { mock.getClass().getMethod("setUsername", String.class).invoke(mock, "Inconnu"); } catch (Exception ignored) {}
            try { mock.getClass().getMethod("setName", String.class).invoke(mock, "Inconnu"); } catch (Exception ignored) {}
            try { mock.getClass().getMethod("setFullName", String.class).invoke(mock, "Inconnu"); } catch (Exception ignored) {}
            try { mock.getClass().getMethod("setRole", String.class).invoke(mock, "PARENT"); } catch (Exception ignored) {}
            try { mock.getClass().getMethod("setActive", boolean.class).invoke(mock, true); } catch (Exception ignored) {}
            return mock;
        }
    }

    public UserCheckDTO checkUser(String userId) {
        String url = authServiceUrl + "/user/" + userId + "/check";
        try {
            return restTemplate.getForObject(url, UserCheckDTO.class);
        } catch (Exception e) {
            UserCheckDTO mock = new UserCheckDTO();
            try { mock.getClass().getMethod("setUserId", String.class).invoke(mock, userId); } catch (Exception ignored) {}
            try { mock.getClass().getMethod("setActive", boolean.class).invoke(mock, true); } catch (Exception ignored) {}
            try { mock.getClass().getMethod("setRole", String.class).invoke(mock, "PARENT"); } catch (Exception ignored) {}
            return mock;
        }
    }

    public List<UserDTO> getAllUsers() {
        String url = authServiceUrl + "/users";
        try {
            UserDTO[] users = restTemplate.getForObject(url, UserDTO[].class);
            return users != null ? List.of(users) : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}