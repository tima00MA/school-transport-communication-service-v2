package fs.master.asynccommunicationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationHistoryDTO {
    private Long id;
    private String userId;
    private String message;
    private String status;
    private LocalDateTime timestamp;
}
