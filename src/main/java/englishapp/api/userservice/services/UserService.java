package englishapp.api.userservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import englishapp.api.userservice.dto.apiGetAllUser.OutputParamApiGetAllUser;
import englishapp.api.userservice.dto.apiGetInfoUser.OutputParamApiGetInfoUser;
import englishapp.api.userservice.dto.apiGetNumberOfUser.OutputParamApiGetNumberOfUser;
import englishapp.api.userservice.dto.apiGetPermissionOfUser.OutputParamApiGetPermissionOfUser;
import englishapp.api.userservice.dto.apiUpdatePermission.InputParamApiUpdatePermission;
import englishapp.api.userservice.dto.apiUpdateUser.InputParamApiUpdateUser;
import englishapp.api.userservice.dto.apiUpdateUser.OutputParamApiUpdateUser;
import englishapp.api.userservice.repositories.PermissionRepository;
import englishapp.api.userservice.repositories.UserRepository;
import englishapp.api.userservice.utils.PasswordUtil;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    private static final Logger logger = LogManager.getLogger(UserService.class);

    public Mono<OutputParamApiGetAllUser> getAllUser() {
        return userRepository.findAll() // Lấy tất cả người dùng từ repository
                .map(user -> {
                    // Tạo đối tượng OutputParamApiGetAllUser trước để tạo UserInfo
                    OutputParamApiGetAllUser output = new OutputParamApiGetAllUser(); // Tạo đối tượng bao ngoài

                    // Tạo mỗi UserInfo từ User
                    OutputParamApiGetAllUser.UserInfo userInfo = output.new UserInfo( // Chú ý là bạn phải dùng
                                                                                      // output.new để tạo UserInfo
                            user.getUserId(),
                            user.getUserName(),
                            user.getEmail(),
                            user.getTypeUser(),
                            user.getCreatedAt().toString() // Đảm bảo chuyển đổi đúng định dạng
                    );
                    return userInfo;
                })
                .collectList() // Thu thập tất cả các UserInfo thành một List
                .map(userList -> {
                    // Gán danh sách UserInfo vào OutputParamApiGetAllUser
                    OutputParamApiGetAllUser output = new OutputParamApiGetAllUser();
                    output.setUsers(userList); // Gán danh sách người dùng
                    return output;
                });
    }

    public Mono<OutputParamApiGetInfoUser> getInfoUser(String userId) {
        return userRepository.findById(
                userId)
                .map(user -> {
                    OutputParamApiGetInfoUser output = new OutputParamApiGetInfoUser();
                    output.setUserName(user.getUserName());
                    output.setEmail(user.getEmail());
                    output.setTypeUser(user.getTypeUser());
                    output.setCreateAt(user.getCreatedAt().toString());
                    return output;
                });
    }

    public Mono<OutputParamApiUpdateUser> updateUser(String userId, InputParamApiUpdateUser input) {
        return userRepository.findById(userId)
                .flatMap(user -> {
                    if (input.getUserName() != null && !input.getUserName().isEmpty()) {
                        user.setUserName(input.getUserName());
                    }
                    return userRepository.save(user);
                })
                .map(user -> {
                    OutputParamApiUpdateUser output = new OutputParamApiUpdateUser();
                    output.setUserName(user.getUserName());
                    output.setEmail(user.getEmail());
                    output.setTypeUser(user.getTypeUser());
                    return output;
                });
    }

    public Mono<Void> changePassword(String userId, String oldPassword, String newPassword) {
        return userRepository.findById(userId)
                .flatMap(user -> {
                    if (!PasswordUtil.checkPassword(oldPassword, user.getPassword())) {
                        return Mono.error(new RuntimeException("Old password is incorrect"));
                    }
                    user.setPassword(PasswordUtil.hashPassword(newPassword));
                    return userRepository.save(user);
                })
                .then();
    }

    public Mono<OutputParamApiGetNumberOfUser> getNumberOfUser() {
        return userRepository.count()
                .map(count -> {
                    OutputParamApiGetNumberOfUser output = new OutputParamApiGetNumberOfUser();
                    output.setNumberOfUser(count.intValue());
                    return output;
                });
    }

    public Mono<Void> updatePermission(InputParamApiUpdatePermission input) {
        return userRepository.findById(
                input.getIdUser())
                .flatMap(user -> {
                    // Update user permissions here
                    // Assuming you have a method to update permissions in your User model
                    if (user.getListBlockEndpoint() == null) {
                        user.setListBlockEndpoint(new ArrayList<>());
                    }

                    if ("ALLOW".equalsIgnoreCase(input.getTypeUpdate())) {
                        user.getListBlockEndpoint().remove(input.getNamePermission());
                    } else if ("BLOCK".equalsIgnoreCase(input.getTypeUpdate())) {
                        user.getListBlockEndpoint().add(input.getNamePermission());
                    } else {
                        return Mono.error(new RuntimeException("Invalid typeUpdate value"));
                    }
                    // Save the updated user
                    return userRepository.save(user)
                            .then();
                })
                .doOnSuccess(v -> logger.info("Update permission successfully for userId: {}", input.getIdUser()))
                .doOnError(error -> logger.error("Error Update permission: {}", error.getMessage()));
    }

    public Mono<OutputParamApiGetPermissionOfUser> getPermissionOfUser(String idUser) {
        return userRepository.findById(idUser)
                .flatMap(user -> {
                    OutputParamApiGetPermissionOfUser output = new OutputParamApiGetPermissionOfUser();
                    ArrayList<OutputParamApiGetPermissionOfUser.PermissionDTO> permissions = new ArrayList<>();
                    return permissionRepository.findAll()
                            .map(permission -> {
                                OutputParamApiGetPermissionOfUser.PermissionDTO permissionDTO = output.new PermissionDTO();
                                permissionDTO.setId(permission.getIdPermission());
                                permissionDTO.setName(permission.getName());
                                permissionDTO.setAllowed(!user.getListBlockEndpoint().contains(permission.getName()));
                                permissions.add(permissionDTO);
                                return permissionDTO;
                            })
                            .collectList()
                            .map(permissionsList -> {
                                output.setPermissions(permissionsList);
                                return output;
                            });
                }).switchIfEmpty(Mono.empty()); // Trả về Mono.empty() nếu không tìm thấy người dùng;
    }

}
