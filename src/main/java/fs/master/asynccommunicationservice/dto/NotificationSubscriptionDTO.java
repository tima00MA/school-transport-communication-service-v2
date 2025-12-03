package fs.master.asynccommunicationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationSubscriptionDTO {
    private Long id;
    private String userId;
    private Long notificationTypeId;
    private Boolean isSubscribed;
}
