package englishapp.api.userservice.dto.apiGetInfoUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutputParamApiGetInfoUser {
    private String userName;
    private String email;
    private int typeUser;
    private String createAt;
}
