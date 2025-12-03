package fs.master.asynccommunicationservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationDTO {
    private String entityId;
    private String entityType; // "student" or "bus"
    private Double latitude;
    private Double longitude;
    private String timestamp; // ISO-8601 string, ex: 2025-02-17T10:00:00Z
}
