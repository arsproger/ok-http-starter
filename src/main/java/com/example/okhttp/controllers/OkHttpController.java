package com.example.okhttp.controllers;

import com.example.okhttp.services.OkHttpService;
import com.example.okhttp.models.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/okhttp/user")
public class OkHttpController {
    private final OkHttpService okHttpService;

    @Autowired
    public OkHttpController(OkHttpService okHttpService) {
        this.okHttpService = okHttpService;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return okHttpService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return okHttpService.getUserById(id);
    }

    @PostMapping
    public UserDto saveUser(@RequestBody UserDto userDto) {
        return okHttpService.createUser(userDto);
    }

}
