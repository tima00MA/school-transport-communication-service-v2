package fs.master.asynccommunicationservice.service;

import fs.master.asynccommunicationservice.dto.BusDTO;
import fs.master.asynccommunicationservice.dto.StudentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class StudentCommunicationService {

    // On crée le RestTemplate ici → plus besoin d'injection
    private final RestTemplate restTemplate = new RestTemplate();

    // Injection lazy pour éviter la dépendance circulaire avec BusCommunicationService
    private final @Lazy BusCommunicationService busService;

    // Valeur par défaut si pas dans application.yml
    @Value("${student.service.url:http://localhost:8092/students}")
    private String studentServiceUrl;

    // Constructeur obligatoire à cause de @Lazy
    public StudentCommunicationService(@Lazy BusCommunicationService busService) {
        this.busService = busService;
    }

    // 1. Liste de tous les étudiants
    public List<StudentDTO> getAllStudents() {
        try {
            ResponseEntity<StudentDTO[]> response = restTemplate.getForEntity(studentServiceUrl, StudentDTO[].class);
            StudentDTO[] students = response.getBody();
            return students != null ? List.of(students) : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    // 2. Récupérer un étudiant par ID
    public StudentDTO getStudentById(Long id) {
        try {
            return restTemplate.getForObject(studentServiceUrl + "/" + id, StudentDTO.class);
        } catch (Exception e) {
            StudentDTO mock = new StudentDTO();
            mock.setId(id);
            mock.setFirstName("Étudiant");
            mock.setLastName("Inconnu");
            mock.setBusId(null);
            return mock;
        }
    }

    // 3. Créer un étudiant
    public StudentDTO createStudent(StudentDTO studentDTO) {
        try {
            return restTemplate.postForObject(studentServiceUrl, studentDTO, StudentDTO.class);
        } catch (Exception e) {
            studentDTO.setId(999L); // mock ID
            return studentDTO;
        }
    }

    // 4. Mettre à jour un étudiant
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        try {
            restTemplate.put(studentServiceUrl + "/" + id, studentDTO);
            return getStudentById(id);
        } catch (Exception e) {
            return getStudentById(id);
        }
    }

    // 5. Étudiant enrichi avec son bus
    public StudentDTO getStudentWithBus(Long studentId) {
        StudentDTO student = getStudentById(studentId);
        if (student != null && student.getBusId() != null) {
            try {
                BusDTO bus = busService.getBusById(student.getBusId());
                student.setBus(bus);
            } catch (Exception ignored) {}
        }
        return student;
    }

    // 6. Liste des étudiants d’un bus (optimisée)
    public List<StudentDTO> getStudentsByBusId(Long busId) {
        List<StudentDTO> allStudents = getAllStudents();
        if (allStudents.isEmpty()) return Collections.emptyList();

        BusDTO bus = null;
        try {
            bus = busService.getBusById(busId);
        } catch (Exception ignored) {}

        BusDTO finalBus = bus;
        return allStudents.stream()
                .filter(s -> busId.equals(s.getBusId()))
                .peek(s -> s.setBus(finalBus))
                .toList();
    }

    // 7. Vérifier si le bus est plein
    public boolean isBusFull(Long busId) {
        try {
            BusDTO bus = busService.getBusById(busId);
            int capacity = bus.getCapacity() != null ? bus.getCapacity() : 0;
            int current = getStudentsByBusId(busId).size();
            return current >= capacity;
        } catch (Exception e) {
            return true; // par sécurité
        }
    }

    // 8. Vérifier si un étudiant est dans un bus
    public boolean isStudentInBus(Long studentId, Long busId) {
        try {
            StudentDTO student = getStudentById(studentId);
            return busId.equals(student.getBusId());
        } catch (Exception e) {
            return false;
        }
    }
}