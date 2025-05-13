package englishapp.api.userservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import api.common.englishapp.auth.RequiresAuth;
import api.common.englishapp.auth.UserData;
import api.common.englishapp.requests.CommonResponse;
import api.common.englishapp.requests.ResponseUtil;
import englishapp.api.userservice.dto.apiChangePassword.InputParamApiChangePassword;
import englishapp.api.userservice.dto.apiUpdateUser.InputParamApiUpdateUser;
import englishapp.api.userservice.services.UserService;
import reactor.core.publisher.Mono;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequiresAuth(roles = { "ADMIN" })
    @GetMapping("/getAllUser")
    public Mono<ResponseEntity<CommonResponse<?>>> getAllUser(ServerWebExchange exchange) {
        UserData userData = exchange.getAttribute("USER_DATA");
        if (userData == null) {
            return Mono.just(ResponseUtil.unAuthorized("userId is null"));
        }
        return userService.getAllUser().flatMap(data -> {
            return Mono.just(ResponseUtil.ok(data));
        }).onErrorResume(e -> {
            return Mono.just(ResponseUtil.serverError(e.getMessage()));
        });
    }

    @RequiresAuth
    @PostMapping("/getInfo")
    public Mono<ResponseEntity<CommonResponse<?>>> getInfoUser(ServerWebExchange exchange) {
        UserData userData = exchange.getAttribute("USER_DATA");
        if (userData == null) {
            return Mono.just(ResponseUtil.unAuthorized("userId is null"));
        }
        return userService.getInfoUser(userData.getUserId()).flatMap(data -> {
            return Mono.just(ResponseUtil.ok(data));
        }).onErrorResume(e -> {
            return Mono.just(ResponseUtil.serverError(e.getMessage()));
        });
    }

    @RequiresAuth
    @PostMapping("/update")
    public Mono<ResponseEntity<CommonResponse<?>>> updateUser(ServerWebExchange exchange,
            @RequestBody InputParamApiUpdateUser input) {
        UserData userData = exchange.getAttribute("USER_DATA");
        if (userData == null) {
            return Mono.just(ResponseUtil.unAuthorized("userId is null"));
        }
        return userService.updateUser(userData.getUserId(), input).flatMap(data -> {
            return Mono.just(ResponseUtil.ok(data));
        }).onErrorResume(e -> {
            return Mono.just(ResponseUtil.serverError(e.getMessage()));
        });
    }

    @RequiresAuth
    @PostMapping("/changePassword")
    public Mono<ResponseEntity<CommonResponse<?>>> changePassword(ServerWebExchange exchange,
            @RequestBody InputParamApiChangePassword input) {
        UserData userData = exchange.getAttribute("USER_DATA");
        if (userData == null) {
            return Mono.just(ResponseUtil.unAuthorized("userId is null"));
        }
        return userService.changePassword(userData.getUserId(), input.getOldPassword(), input.getNewPassword())
                .flatMap(data -> {
                    return Mono.just(ResponseUtil.ok(data));
                }).onErrorResume(e -> {
                    return Mono.just(ResponseUtil.serverError(e.getMessage()));
                });
    }
}
