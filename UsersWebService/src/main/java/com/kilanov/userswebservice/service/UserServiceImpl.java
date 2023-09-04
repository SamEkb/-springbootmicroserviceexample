package com.kilanov.userswebservice.service;

import com.kilanov.userswebservice.dto.UserDto;
import com.kilanov.userswebservice.entity.AuthorityEntity;
import com.kilanov.userswebservice.entity.RoleEntity;
import com.kilanov.userswebservice.entity.UserEntity;
import com.kilanov.userswebservice.repository.UserRepository;
import com.kilanov.userswebservice.ui.response.AlbumResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.modelmapper.convention.MatchingStrategies.STRICT;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final RestTemplate restTemplate;
    private final Environment environment;
    private final AlbumsServiceClient albumsServiceClient;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder, RestTemplate restTemplate,
                           Environment environment, AlbumsServiceClient albumsServiceClient) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.restTemplate = restTemplate;
        this.environment = environment;
        this.albumsServiceClient = albumsServiceClient;
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

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (RoleEntity role : userEntity.getRoles()) {
            var authority = new SimpleGrantedAuthority(role.getName());
            authorities.add(authority);

            for (AuthorityEntity roleAuthority : role.getAuthorities()) {
                var authorityEntity = new SimpleGrantedAuthority(roleAuthority.getName());
                authorities.add(authorityEntity);
            }
        }

        return new User(
                userEntity.getEmail(),
                userEntity.getEncryptedPassword(),
                true,
                true,
                true,
                true,
                authorities
        );
    }

    @Override
    public UserDto getUserById(String userId, String authorization) {
        var mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(STRICT);
        UserEntity userEntity = userRepository.getUserEntityByUserId(userId);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User does not exist");
        }

//        String albumsUrl = String.format(environment.getProperty("albums.url_path"), userId);
//
//        var albumsResponse = restTemplate.exchange(
//                albumsUrl,
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<AlbumResponse>>() {
//                }
//        );


        var result = mapper.map(userEntity, UserDto.class);

        logger.debug("Before albums were found");
        //List<AlbumResponse> albums = albumsResponse.getBody();
        List<AlbumResponse> albums = albumsServiceClient.getAlbums(userId, authorization);

        logger.debug("After albums were found");

        result.setAlbums(albums);

        return result;
    }

    @Override
    public void delete(String userId) {
        var user = userRepository.getUserEntityByUserId(userId);

        if (user == null) {
            return;
        }

        userRepository.deleteById(user.getId());
    }
}
