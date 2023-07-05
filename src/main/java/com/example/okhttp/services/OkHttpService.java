package com.example.okhttp.services;

import com.example.okhttp.models.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OkHttpService {
    private final OkHttpClient httpClient;
    private final String userServiceUrl;
    private final ObjectMapper objectMapper;

    @Autowired
    public OkHttpService(OkHttpClient httpClient,
                         @Value("${userService.url}") String userServiceUrl,
                         ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.userServiceUrl = userServiceUrl;
    }

    public List<UserDto> getAllUsers() {
        String url = userServiceUrl + "/user";
        Request request = new Request.Builder()
                .url(url)
                .build();

        return parseUsersFromResponse(executeRequest(request));
    }

    public UserDto getUserById(Long id) {
        String url = userServiceUrl + "/user/" + id;
        Request request = new Request.Builder()
                .url(url)
                .build();

        return parseUserFromResponse(executeRequest(request));
    }

    public UserDto createUser(UserDto user) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String userJson = "{" +
                "\"name\":\"" + user.getName() +
                "\",\"surname\":\"" + user.getSurname() +
                "\",\"age\":" + user.getAge() +
                "}";

        RequestBody requestBody = RequestBody.create(userJson, JSON);
        Request request = new Request.Builder()
                .url(userServiceUrl + "/user")
                .post(requestBody)
                .build();

        return parseUserFromResponse(executeRequest(request));
    }

    private List<UserDto> parseUsersFromResponse(String responseBody) {
        try {
            return objectMapper.readValue(responseBody, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private UserDto parseUserFromResponse(String responseBody) {
        try {
            return objectMapper.readValue(responseBody, UserDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String executeRequest(Request request) {
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                return response.body().string();
            } else {
                throw new RuntimeException("Ошибка! Код HTTP-статуса: " + response.code());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
