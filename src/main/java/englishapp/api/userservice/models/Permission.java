package englishapp.api.userservice.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "permissions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {
    @Id
    private String idPermission;
    private String name;
}
