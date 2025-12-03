package fs.master.asynccommunicationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

// DTO pour User
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private UUID id;
    private String email;
    private String fullName;
    private Role role;
    private boolean enabled;
    private Double gpsLatitude;
    private Double gpsLongitude;
}