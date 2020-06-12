package xml.papersapp.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkplaceDTO {

    private String name;
    private String address;
    private String country;
    private String city;
    private String zip;

}
