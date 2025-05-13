package englishapp.api.userservice.dto.apiUpdatePermission;

import lombok.Data;

@Data
public class InputParamApiUpdatePermission {
    private String idUser;
    private String namePermission;
    private String typeUpdate; // "ALLOW" or "BLOCK"
}
