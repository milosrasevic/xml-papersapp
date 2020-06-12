package xml.papersapp.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDTO {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String profession;
    private WorkplaceDTO workplace;
    private String phoneNumber;

    public RegistrationDTO(String email, String firstName, String lastName, String profession, WorkplaceDTO workplace, String phoneNumber) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profession = profession;
        this.workplace = workplace;
        this.phoneNumber = phoneNumber;
    }
}
