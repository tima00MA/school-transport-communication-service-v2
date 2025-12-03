package fs.master.asynccommunicationservice.dto;





import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusDTO {
    private Long id;
    private String registrationNumber;
    private Integer capacity;
    private String status;
    private DriverDTO driver;
}


