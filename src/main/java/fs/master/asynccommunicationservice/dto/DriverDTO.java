package fs.master.asynccommunicationservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String licenseNumber;
}
