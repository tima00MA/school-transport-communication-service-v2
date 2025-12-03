package fs.master.asynccommunicationservice.repository;

import fs.master.asynccommunicationservice.model.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageLogRepository extends JpaRepository<MessageLog, Long> {
}