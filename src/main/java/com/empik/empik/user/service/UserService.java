package com.empik.empik.user.service;

import com.empik.empik.user.entity.UserEntity;
import com.empik.empik.user.model.dto.UserDto;
import com.empik.empik.user.model.dto.GitHubUserDto;
import com.empik.empik.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate;
    private final UserRepository empikRepository;

    public UserDto getLoginData(String login) {

        Optional<UserEntity> entity = empikRepository.findByLogin(login);
        UserEntity e = entity.orElse(UserEntity.builder().login(login).RequestCount(0).build());
        e.setRequestCount(e.getRequestCount()+1);
        empikRepository.save(e);



        GitHubUserDto userDto = restTemplate.getForEntity("https://api.github.com/users/"+login, GitHubUserDto.class).getBody();


        //!!! if number of followers = 0 we have division by zero so the result is infinity. Is that correct???
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
