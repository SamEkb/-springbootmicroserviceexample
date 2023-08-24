package com.kilanov.userswebservice.service;

import com.kilanov.userswebservice.dto.UserDto;
import com.kilanov.userswebservice.entity.UserEntity;
import com.kilanov.userswebservice.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public UserDto getUserByEmail(String email) {
        var mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(STRICT);
        var userEntity = userRepository.getUserEntityByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User does not exist");
        }

        return mapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var userEntity = userRepository.getUserEntityByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User does not exist");
        }

        return new User(
                userEntity.getEmail(),
                userEntity.getEncryptedPassword(),
                true,
                true,
                true,
                true,
                new ArrayList<>()
        );
    }
}
