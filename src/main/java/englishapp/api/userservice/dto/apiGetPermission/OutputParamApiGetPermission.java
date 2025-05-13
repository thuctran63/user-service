package englishapp.api.userservice.dto.apiGetPermission;

import java.util.List;

import englishapp.api.userservice.models.Permission;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OutputParamApiGetPermission {
    private List<Permission> permissions;
}
