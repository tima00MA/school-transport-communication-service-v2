package fs.master.asynccommunicationservice.service;

import fs.master.asynccommunicationservice.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteCommunicationService {

    // Ligne magique qui fait disparaître l'erreur rouge
    private final RestTemplate restTemplate = new RestTemplate();

    private @Lazy final StudentCommunicationService studentService;
    private @Lazy final BusCommunicationService busService;
    private @Lazy final GpsCommunicationService gpsService;
    private @Lazy final NotificationCommunicationService notificationService;

    @Value("${route.service.url}")
    private String routeServiceUrl; // ex: http://localhost:8100/routes

    // ------------------- Routes -------------------

    public RouteDTO getOptimalRoute(Long busId) {
        String url = routeServiceUrl + "/" + busId;
        RouteDTO route = restTemplate.getForObject(url, RouteDTO.class);

        if (route != null) {
            // Récupérer Bus synchronement
            if (route.getBus() != null && route.getBus().getId() != null) {
                route.setBus(busService.getBusById(route.getBus().getId()));
            }

            // Récupérer Students synchronement
            if (route.getStudents() != null && !route.getStudents().isEmpty()) {
                List<StudentDTO> students = route.getStudents().stream()
                        .map(s -> studentService.getStudentById(s.getId()))
                        .collect(Collectors.toList());
                route.setStudents(students);
            }
        }

        return route;
    }

    public ETAResponseDTO getETA(Long studentId) {
        String url = routeServiceUrl + "/eta/" + studentId;
        ETAResponseDTO eta = restTemplate.getForObject(url, ETAResponseDTO.class);
        if (eta != null) {
            eta.setStudentId(studentId);
        }
        return eta;
    }

    public List<RouteDTO> generateRoutes(String circuitType) {
        String url = routeServiceUrl + "/generate?circuitType=" + circuitType;
        RouteDTO[] routes = restTemplate.postForObject(url, null, RouteDTO[].class);

        if (routes != null) {
            for (RouteDTO route : routes) {
                // Bus
                if (route.getBus() != null && route.getBus().getId() != null) {
                    route.setBus(busService.getBusById(route.getBus().getId()));
                }
                // Students
                if (route.getStudents() != null && !route.getStudents().isEmpty()) {
                    List<StudentDTO> students = route.getStudents().stream()
                            .map(s -> studentService.getStudentById(s.getId()))
                            .collect(Collectors.toList());
                    route.setStudents(students);
                }
            }
            return Arrays.asList(routes);
        }
        return List.of();
    }
}