package fs.master.asynccommunicationservice.model;

import java.time.LocalDateTime;

public record MessageLogDTO(Long id, String queue, String payload, LocalDateTime timestamp) {
}