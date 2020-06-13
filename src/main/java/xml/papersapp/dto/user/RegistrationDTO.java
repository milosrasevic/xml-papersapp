package xml.papersapp.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xml.papersapp.model.user.TUser;

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

    public RegistrationDTO(TUser registeredUser) {
        this.email = registeredUser.getEmail();
        this.firstName = registeredUser.getFirstName();
        this.lastName = registeredUser.getLastName();
        this.profession = registeredUser.getProfession();
        this.workplace = new WorkplaceDTO(registeredUser.getWorkplace());
        this.phoneNumber = registeredUser.getPhoneNumber();
    }
}
