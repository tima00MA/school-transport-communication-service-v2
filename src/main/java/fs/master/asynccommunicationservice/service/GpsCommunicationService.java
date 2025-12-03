package fs.master.asynccommunicationservice.service;

import fs.master.asynccommunicationservice.dto.CreateLocationRequest;
import fs.master.asynccommunicationservice.dto.LocationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GpsCommunicationService {

    // Ligne magique qui tue l’erreur rouge private final RestTemplate restTemplate = new RestTemplate();

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${gps.service.url}")
    private String gpsServiceUrl; // ex: http://localhost:8001/locations

    /**
     * Create a new location for given entityType (student|bus) and entityId.
     */
    public ResponseEntity<String> createLocation(String entityType, String entityId, CreateLocationRequest req) {
        String url = String.format("%s/%s/%s", gpsServiceUrl, entityType, entityId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateLocationRequest> entity = new HttpEntity<>(req, headers);
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    /**
     * Get latest location by entity id
     */
    public LocationDTO getLatestLocationByEntityId(String entityId) {
        String url = String.format("%s/%s", gpsServiceUrl, entityId);
        return restTemplate.getForObject(url, LocationDTO.class);
    }

    /**
     * Get locations for a specific entity (entity_type + entity_id)
     */
    public List<LocationDTO> getLocationsByEntity(String entityType, String entityId, int skip, int limit) {
        String url = String.format("%s/entity/%s/%s?skip=%d&limit=%d", gpsServiceUrl, entityType, entityId, skip, limit);
        LocationDTO[] arr = restTemplate.getForObject(url, LocationDTO[].class);
        return arr == null ? List.of() : Arrays.asList(arr);
    }

    /**
     * Get latest locations for all entities (optionally filtered by entityType)
     */
    public List<LocationDTO> getAllEntitiesLatestLocations(String entityType) {
        String url = gpsServiceUrl + "/entities/locations";
        if (entityType != null && !entityType.isEmpty()) {
            url += "?entity_type=" + entityType;
        }
        LocationDTO[] arr = restTemplate.getForObject(url, LocationDTO[].class);
        return arr == null ? List.of() : Arrays.asList(arr);
    }
}