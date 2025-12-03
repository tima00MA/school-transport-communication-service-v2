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
public class RouteDTO {
    private Long id;
    private String routeName;
    private BusDTO bus;                 // Communication synchrone avec Bus
    private List<StudentDTO> students;  // Communication synchrone avec Student
    private String polyline;
}