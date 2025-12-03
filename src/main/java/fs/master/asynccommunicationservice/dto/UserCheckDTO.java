package fs.master.asynccommunicationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCheckDTO {
    private UUID id;
    private Role role;  // <-- ton enum Role
    private boolean enabled;
}
