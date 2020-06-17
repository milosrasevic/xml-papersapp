package xml.papersapp.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xml.papersapp.model.user.TWorkplace;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkplaceDTO {

    private String name;
    private String address;
    private String country;
    private String city;
    private String zip;

    public WorkplaceDTO(TWorkplace workplace) {
        this.name = workplace.getName();
        this.address = workplace.getAddress();
        this.country = workplace.getCountry();
        this.city = workplace.getCity();
        this.zip = Integer.toString(workplace.getZip());
    }

}
