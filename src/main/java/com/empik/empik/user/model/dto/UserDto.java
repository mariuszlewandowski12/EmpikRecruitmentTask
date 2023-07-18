package com.empik.empik.user.model.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDto {

    private String id;//should rather be int but in example we have "id": "...",
    private String login;
    private String name;
    private String type;
    private String avatarUrl;
    private String createdAt;
    private String calculations;//should rather be float but in example we have "calculations": "..."

}
