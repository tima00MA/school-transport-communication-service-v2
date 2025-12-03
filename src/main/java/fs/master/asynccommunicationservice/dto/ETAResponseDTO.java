package fs.master.asynccommunicationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ETAResponseDTO {
    private Long studentId;
    private String eta; // estimated time of arrival
}