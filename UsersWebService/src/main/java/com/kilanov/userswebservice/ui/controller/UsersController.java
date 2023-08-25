package com.kilanov.userswebservice.ui.controller;

import com.kilanov.userswebservice.dto.UserDto;
import com.kilanov.userswebservice.service.UserService;
import com.kilanov.userswebservice.ui.requests.CreateUserRequest;
import com.kilanov.userswebservice.ui.response.UserResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.modelmapper.convention.MatchingStrategies.STRICT;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/users")
public class UsersController {
    private UserService service;

    public UsersController(UserService service) {
        this.service = service;
    }

    @GetMapping("/status/check")
    public String status() {
        return "Working";
    }

    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        var mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(STRICT);
        var dto = mapper.map(request, UserDto.class);
        var result = service.create(dto);
        var response = mapper.map(result, UserResponse.class);

        return ResponseEntity.status(CREATED).body(response);
    }
}
