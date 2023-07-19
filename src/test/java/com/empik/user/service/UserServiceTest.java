package com.empik.user.service;

import static org.junit.jupiter.api.Assertions.*;

import com.empik.user.model.dto.GitHubUserDto;
import com.empik.user.model.dto.UserDto;
import com.empik.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserService userService;

    @AfterEach
    void tearDown() {

        reset(userRepository);
        reset(restTemplate);
    }

    @Test
    void whenCalledForExistingLogin_shouldReturnResult() {
        //given

        Mockito.when(restTemplate.getForEntity(
                        Mockito.anyString(),
                        ArgumentMatchers.any(Class.class)
                ))
                .thenReturn(ResponseEntity.ok(
                        GitHubUserDto.builder()
                                .login("user1")
                                .id(112)
                                .name("exampleName")
                                .type("user")
                                .avatar_url("http://example.com")
                                .created_at("some date")
                                .public_repos(2)
                                .followers(5)
                                .build()));

        //when
        UserDto user = userService.getLoginData("user1");

        //then
        verify(userRepository, times(1)).addOrIncreaseUsageCount("user1");
        assertNotNull(user);
        assertEquals(user.getId(), "112");
        assertEquals(user.getLogin(), "user1");
        assertEquals(user.getName(), "exampleName");
        assertEquals(user.getType(), "user");
        assertEquals(user.getAvatarUrl(), "http://example.com");
        assertEquals(user.getCreatedAt(), "some date");
        assertEquals(user.getCalculations(), "4.8");
    }

    @Test
    void whenCalledForExistingLoginWithZeroFollowers_shouldCalculateInfinity() {
        //given

        Mockito.when(restTemplate.getForEntity(
                        Mockito.anyString(),
                        ArgumentMatchers.any(Class.class)
                ))
                .thenReturn(ResponseEntity.ok(
                        GitHubUserDto.builder()
                                .login("user1")
                                .id(112)
                                .name("exampleName")
                                .type("user")
                                .avatar_url("http://example.com")
                                .created_at("some date")
                                .public_repos(2)
                                .followers(0)
                                .build()));

        //when
        UserDto user = userService.getLoginData("user1");

        //then
        verify(userRepository, times(1)).addOrIncreaseUsageCount("user1");
        assertNotNull(user);
        assertEquals(user.getCalculations(), "Infinity");
    }

    @Test
    void whenCalledForNonExistingLogin_shouldSaveToDb() {
        //given

        Mockito.when(restTemplate.getForEntity(
                        Mockito.anyString(),
                        ArgumentMatchers.any(Class.class)
                ))
                .thenThrow(new RestClientException("message"));

        //when
        assertThrows(RestClientException.class, ()->userService.getLoginData("user1"));

        //then
        verify(userRepository, times(1)).addOrIncreaseUsageCount("user1");
    }

}