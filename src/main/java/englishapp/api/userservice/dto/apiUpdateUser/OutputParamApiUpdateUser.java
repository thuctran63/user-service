package englishapp.api.userservice.dto.apiUpdateUser;

import lombok.Data;

@Data
public class OutputParamApiUpdateUser {
    private String userName;
    private String email;
    private int typeUser;
}
