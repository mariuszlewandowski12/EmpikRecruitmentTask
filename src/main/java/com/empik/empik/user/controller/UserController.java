package com.empik.empik.user.controller;

import com.empik.empik.user.model.dto.UserDto;
import com.empik.empik.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/users/{login}", produces = APPLICATION_JSON_VALUE)
    public UserDto viewMismatchedNotams(@PathVariable String login) {
        log.info("Received GET request for /users/"+login);
        return userService.getLoginData(login);
    }

}
