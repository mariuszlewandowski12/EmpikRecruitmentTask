package com.empik.user.service;

import static com.empik.user.configuration.Constants.USER_SERVICE_URL;

import com.empik.user.model.dto.UserDto;
import com.empik.user.model.dto.GitHubUserDto;
import com.empik.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    public UserDto getLoginData(String login) {

        userRepository.addOrIncreaseUsageCount(login);

        GitHubUserDto userDto = restTemplate.getForEntity(USER_SERVICE_URL+login, GitHubUserDto.class).getBody();

        //!!! if number of followers = 0 we have division by zero so the result is infinity. Is that correct??? Assuming Yes!
        return UserDto.builder().id(Integer.toString(userDto.getId()))
                .login(userDto.getLogin())
                .name(userDto.getName())
                .type(userDto.getType())
                .avatarUrl(userDto.getAvatar_url())
                .createdAt(userDto.getCreated_at())
                .calculations(Double.toString(6.0/userDto.getFollowers()*(2+userDto.getPublic_repos())))
                .build();
    }
}
