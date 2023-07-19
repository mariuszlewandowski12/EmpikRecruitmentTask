package com.empik.user.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.empik.user.model.dto.UserDto;
import com.empik.user.service.UserService;
import org.hamcrest.core.StringContains;
import org.hibernate.HibernateException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;

import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @AfterEach
    public void tearDown() {
        reset(userService);
    }

    @Test
    public void whenCalledSuccessfully_shouldReturnUserDto() throws Exception {
        //given
        UserDto expected = UserDto.builder()
                .id("51")
                .login("login")
                .name("name")
                .type("type")
                .avatarUrl("url")
                .createdAt("some dat")
                .calculations("5.8")
                .build();
        when(userService.getLoginData("user1")).thenReturn(expected);
        //when
        mockMvc.perform(
                get("/users/user1"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenCalledWithRestException_shouldReturnBadRequest() throws Exception {
        //given
        when(userService.getLoginData("user1")).thenThrow(new RestClientException("error running github service"));

        //when
        mockMvc.perform(
                        get("/users/user1"))
                .andExpect(content().string(StringContains.containsString("User not found")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenDBProblems_shouldReturnInternalServerError() throws Exception {
        //given
        when(userService.getLoginData("user1")).thenThrow(new HibernateException("error accessing DB"));

        //when
        mockMvc.perform(
                        get("/users/user1"))
                .andExpect(content().string(StringContains.containsString("DB access error")))
                .andExpect(status().isInternalServerError());
    }
}
