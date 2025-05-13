package englishapp.api.userservice.dto.apiGetPermissionOfUser;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OutputParamApiGetPermissionOfUser {

    private List<PermissionDTO> permissions;

    @Data
    @NoArgsConstructor
    public class PermissionDTO {
        private String id;
        private String name;
        private boolean isAllowed;
    }
}
