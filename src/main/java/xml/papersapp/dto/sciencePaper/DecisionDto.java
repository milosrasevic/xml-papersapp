package xml.papersapp.dto.sciencePaper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecisionDto {

    private String paperTitle;
    private boolean accepted;
}
