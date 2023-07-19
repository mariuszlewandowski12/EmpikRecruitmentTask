package com.empik.user.model.dto;

import lombok.Value;

@Value
public class GitHubUserDto {

    private String login;
    private int id;
    private String name;
    private String type;
    private String avatar_url;
    private String created_at;
    private int public_repos;
    private int followers;
}
