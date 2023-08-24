package com.kilanov.userswebservice.service;

import com.kilanov.userswebservice.dto.UserDto;
import com.kilanov.userswebservice.entity.UserEntity;
import com.kilanov.userswebservice.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.modelmapper.convention.MatchingStrategies.STRICT;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public UserDto create(UserDto dto) {
        var mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(STRICT);
        dto.setUserId(UUID.randomUUID().toString());
        dto.setEncryptedPassword(encoder.encode(dto.getPassword()));

        var entity = mapper.map(dto, UserEntity.class);
        var savedEntity = userRepository.save(entity);

        return mapper.map(savedEntity, UserDto.class);
    }
}
