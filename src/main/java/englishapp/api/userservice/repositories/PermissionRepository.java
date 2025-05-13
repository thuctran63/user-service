package englishapp.api.userservice.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import englishapp.api.userservice.models.Permission;

@Repository
public interface PermissionRepository extends ReactiveMongoRepository<Permission, String> {
    // Custom query methods can be defined here if needed

}
