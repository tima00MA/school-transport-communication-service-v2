package fs.master.asynccommunicationservice.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupeDTO {
    private Long id;
    private String nom;
    private Integer taille;
    private List<StudentDTO> eleves;
}
