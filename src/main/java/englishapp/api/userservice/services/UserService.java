package englishapp.api.userservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import englishapp.api.userservice.dto.apiGetAllUser.OutputParamApiGetAllUser;
import englishapp.api.userservice.dto.apiGetInfoUser.OutputParamApiGetInfoUser;
import englishapp.api.userservice.dto.apiUpdateUser.InputParamApiUpdateUser;
import englishapp.api.userservice.dto.apiUpdateUser.OutputParamApiUpdateUser;
import englishapp.api.userservice.repositories.UserRepository;
import englishapp.api.userservice.utils.PasswordUtil;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

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
}
