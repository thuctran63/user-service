package englishapp.api.userservice.dto.apiChangePassword;

import lombok.Data;

@Data
public class InputParamApiChangePassword {
    private String oldPassword;
    private String newPassword;
}
