package com.kilanov.userswebservice.service;

import com.kilanov.userswebservice.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto create(UserDto dto);

    UserDto getUserByEmail(String email);
}
