package englishapp.api.userservice.dto.apiGetAllUser;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OutputParamApiGetAllUser {
    private List<UserInfo> users;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class UserInfo {
        private String userId;
        private String userName;
        private String email;
        private int typeUser;
        private String createAt;
    }
}
