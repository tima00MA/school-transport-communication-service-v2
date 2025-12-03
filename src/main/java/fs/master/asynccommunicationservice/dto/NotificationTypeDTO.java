package fs.master.asynccommunicationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationTypeDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean isActive;
}
