package xml.papersapp.dto.reviewAssignment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xml.papersapp.model.review_assignment.TBlinded;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewAssignmentDTO {

    private String email;
    private TBlinded blinded;

}
