package fs.master.asynccommunicationservice.controller;

import fs.master.asynccommunicationservice.dto.*;
import fs.master.asynccommunicationservice.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/communication") // point d'entrée unique
@RequiredArgsConstructor
public class CommunicationController {
    private final AuthCommunicationService authService;
    private final GpsCommunicationService gpsService;
    private final StudentCommunicationService studentService;
    private final BusCommunicationService busService;
    private final NotificationCommunicationService notificationService;
    private final RouteCommunicationService routeService;
    private final  GroupementCommunicationService groupementService;

    // ------------------- STUDENT -------------------
    @GetMapping("/students")
    public List<StudentDTO> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/students/{id}")
    public StudentDTO getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PostMapping("/students")
    public StudentDTO createStudent(@RequestBody StudentDTO studentDTO) {
        return studentService.createStudent(studentDTO);
    }

    @GetMapping("/students/{id}/withBus")
    public StudentDTO getStudentWithBus(@PathVariable Long id) {
        return studentService.getStudentWithBus(id);
    }

    @GetMapping("/students/byBus/{busId}")
    public List<StudentDTO> getStudentsByBusId(@PathVariable Long busId) {
        return studentService.getStudentsByBusId(busId);
    }

    // ------------------- BUS -------------------
    @GetMapping("/buses")
    public List<BusDTO> getAllBuses() {
        return busService.getAllBuses();
    }

    @GetMapping("/buses/{id}")
    public BusDTO getBusById(@PathVariable Long id) {
        return busService.getBusById(id);
    }

    // ------------------- LOCATIONS -------------------
    @PostMapping("/locations/{entityType}/{entityId}")
    public ResponseEntity<String> createLocation(
            @PathVariable String entityType,
            @PathVariable String entityId,
            @RequestBody CreateLocationRequest request) {

        if (!"student".equalsIgnoreCase(entityType) && !"bus".equalsIgnoreCase(entityType)) {
            return ResponseEntity.badRequest().body("entityType must be 'student' or 'bus'");
        }
        return gpsService.createLocation(entityType.toLowerCase(), entityId, request);
    }

    @GetMapping("/locations/student/{studentId}")
    public ResponseEntity<LocationDTO> getStudentLatestLocation(@PathVariable String studentId) {
        LocationDTO dto = gpsService.getLatestLocationByEntityId(studentId);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/locations/bus/{busId}")
    public ResponseEntity<LocationDTO> getBusLatestLocation(@PathVariable String busId) {
        LocationDTO dto = gpsService.getLatestLocationByEntityId(busId);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/locations/entity/{entityType}/{entityId}")
    public List<LocationDTO> getLocationsByEntity(
            @PathVariable String entityType,
            @PathVariable String entityId,
            @RequestParam(defaultValue = "0") int skip,
            @RequestParam(defaultValue = "100") int limit) {
        return gpsService.getLocationsByEntity(entityType.toLowerCase(), entityId, skip, limit);
    }

    @GetMapping("/locations/entities/latest")
    public List<LocationDTO> getAllEntitiesLatestLocations(@RequestParam(required = false) String entityType) {
        return gpsService.getAllEntitiesLatestLocations(entityType);
    }

    @PostMapping("/locations/student/{studentId}")
    public ResponseEntity<String> createStudentLocation(@PathVariable String studentId,
                                                        @RequestBody CreateLocationRequest request) {
        return gpsService.createLocation("student", studentId, request);
    }

    @PostMapping("/locations/bus/{busId}")
    public ResponseEntity<String> createBusLocation(@PathVariable String busId,
                                                    @RequestBody CreateLocationRequest request) {
        return gpsService.createLocation("bus", busId, request);
    }
    //---------------Notification------------------------------------------
    // Dans CommunicationController.java
    @GetMapping("/notifications/history/{userId}")
    public List<NotificationHistoryDTO> getNotificationHistory(@PathVariable String userId) {
        return notificationService.getNotificationHistoryByUserId(userId);
    }

    @GetMapping("/notifications/types")
    public List<NotificationTypeDTO> getAllNotificationTypes() {
        return notificationService.getAllNotificationTypes();
    }

    @GetMapping("/notifications/subscriptions/{userId}")
    public List<NotificationSubscriptionDTO> getUserSubscriptions(@PathVariable String userId) {
        return notificationService.getUserSubscriptions(userId);
    }
    //-----------------route--------------------------------------------
    // ------------------- ROUTES -------------------

    @GetMapping("/routes/{busId}")
    public RouteDTO getOptimalRoute(@PathVariable Long busId) {
        return routeService.getOptimalRoute(busId);
    }

    @GetMapping("/routes/eta/{studentId}")
    public ETAResponseDTO getETA(@PathVariable Long studentId) {
        return routeService.getETA(studentId);
    }

    @PostMapping("/routes/generate")
    public List<RouteDTO> generateRoutes(@RequestParam String circuitType) {
        return routeService.generateRoutes(circuitType);
    }
    // ------------------- GROUPEMENT D'ÉLÈVES -------------------

    // Lister tous les groupes avec leurs élèves
    @GetMapping("/groupement/groups")
    public List<GroupeDTO> getAllGroups() {
        return groupementService.getAllGroups();
    }

    // Détail d’un groupe
    @GetMapping("/groupement/groups/{id}")
    public GroupeDTO getGroupById(@PathVariable Long id) {
        return groupementService.getGroupById(id);
    }

    // Générer automatiquement les groupes
    @PostMapping("/groupement/groups/generate")
    public List<GroupeDTO> generateGroups() {
        return groupementService.generateGroups();
    }

    // Liste des élèves d’un groupe
    @GetMapping("/groupement/groups/{id}/students")
    public List<StudentDTO> getStudentsByGroupId(@PathVariable Long id) {
        return groupementService.getStudentsByGroupId(id);
    }
    //--------------------Auth------------------------------

    // Récupérer un utilisateur par ID
    @GetMapping("/auth/user/{userId}")
    public UserDTO getUser(@PathVariable String userId) {
        return authService.getUserById(userId);
    }

    // Vérifier utilisateur
    @GetMapping("/auth/user/{userId}/check")
    public UserCheckDTO checkUser(@PathVariable String userId) {
        return authService.checkUser(userId);
    }

    // Récupérer tous les utilisateurs
    @GetMapping("/auth/users")
    public List<UserDTO> getAllUsers() {
        return authService.getAllUsers();
    }


}

