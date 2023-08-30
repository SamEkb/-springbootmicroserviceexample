package com.kilanov.userswebservice.service;

import com.kilanov.userswebservice.dto.UserDto;
import com.kilanov.userswebservice.entity.UserEntity;
import com.kilanov.userswebservice.repository.UserRepository;
import com.kilanov.userswebservice.ui.response.AlbumResponse;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.modelmapper.convention.MatchingStrategies.STRICT;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final RestTemplate restTemplate;
    private final Environment environment;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder, RestTemplate restTemplate,
                           Environment environment) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.restTemplate = restTemplate;
        this.environment = environment;
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

    @Override
    public UserDto getUserById(String userId) {
        var mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(STRICT);
        UserEntity userEntity = userRepository.getUserEntityByUserId(userId);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User does not exist");
        }

        String albumsUrl = String.format(environment.getProperty("albums.url_path"), userId);

        var albumsResponse = restTemplate.exchange(
                albumsUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AlbumResponse>>() {
                }
        );

        var result = mapper.map(userEntity, UserDto.class);

        List<AlbumResponse> albums = albumsResponse.getBody();
        result.setAlbums(albums);

        return result;
    }
}
