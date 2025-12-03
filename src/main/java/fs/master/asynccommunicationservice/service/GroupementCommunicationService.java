package fs.master.asynccommunicationservice.service;

import fs.master.asynccommunicationservice.dto.GroupeDTO;
import fs.master.asynccommunicationservice.dto.StudentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class GroupementCommunicationService {

    // On crée le RestTemplate directement ici → plus besoin d'injection
    private final RestTemplate restTemplate = new RestTemplate();

    // Valeur par défaut si pas dans application.yml
    @Value("${groupement.service.url:http://localhost:8000}")
    private String groupementServiceUrl;

    // Liste tous les groupes
    public List<GroupeDTO> getAllGroups() {
        String url = groupementServiceUrl + "/groups";
        try {
            GroupeDTO[] arr = restTemplate.getForObject(url, GroupeDTO[].class);
            return arr != null ? List.of(arr) : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    // Détail d’un groupe avec ses élèves
    public GroupeDTO getGroupById(Long id) {
        String url = groupementServiceUrl + "/groups/" + id;
        try {
            return restTemplate.getForObject(url, GroupeDTO.class);
        } catch (Exception e) {
            GroupeDTO mock = new GroupeDTO();
            mock.setId(id);
            mock.setNom("Groupe inconnu");
            mock.setEleves(Collections.emptyList());
            return mock;
        }
    }

    // Générer les groupes (à partir des élèves)
    public List<GroupeDTO> generateGroups() {
        String url = groupementServiceUrl + "/groups/generate";
        try {
            GroupeDTO[] arr = restTemplate.postForObject(url, null, GroupeDTO[].class);
            return arr != null ? List.of(arr) : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    // Liste des élèves d’un groupe
    public List<StudentDTO> getStudentsByGroupId(Long groupId) {
        GroupeDTO groupe = getGroupById(groupId);
        return groupe != null && groupe.getEleves() != null ? groupe.getEleves() : Collections.emptyList();
    }
}