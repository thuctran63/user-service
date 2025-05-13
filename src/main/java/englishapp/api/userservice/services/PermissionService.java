package englishapp.api.userservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import englishapp.api.userservice.dto.apiAddNewPermission.InputParamAddNewPermission;
import englishapp.api.userservice.dto.apiGetPermission.OutputParamApiGetPermission;
import englishapp.api.userservice.models.Permission;
import englishapp.api.userservice.repositories.PermissionRepository;
import reactor.core.publisher.Mono;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    // getAllPermissions() method to retrieve all permissions from the database
    public Mono<OutputParamApiGetPermission> getAllPermissions() {
        return permissionRepository.findAll()
                .collectList()
                .map(permissions -> {
                    OutputParamApiGetPermission output = new OutputParamApiGetPermission();
                    output.setPermissions(permissions);
                    return output;
                });
    }

    // add new permission
    public Mono<Void> addPermission(InputParamAddNewPermission input) {
        // Create a new Permission object
        Permission permission = new Permission();
        permission.setName(input.getName());
        return permissionRepository.save(permission).then();
    }
}
