package xml.papersapp.dto.reviewAssignment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewAssignmentsDTO {

    private String title;
    private List<ReviewAssignmentDTO> reviewAssignmentDTO;
}
