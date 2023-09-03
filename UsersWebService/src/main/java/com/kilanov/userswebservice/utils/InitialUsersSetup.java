package com.kilanov.userswebservice.utils;

import com.kilanov.userswebservice.entity.AuthorityEntity;
import com.kilanov.userswebservice.entity.RoleEntity;
import com.kilanov.userswebservice.entity.UserEntity;
import com.kilanov.userswebservice.enums.Roles;
import com.kilanov.userswebservice.repository.AuthorityRepository;
import com.kilanov.userswebservice.repository.RoleRepository;
import com.kilanov.userswebservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Component
public class InitialUsersSetup {
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public InitialUsersSetup(RoleRepository roleRepository, AuthorityRepository authorityRepository,
                             UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Transactional
    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {
        var readAuthority = createAuthority("READ");
        var writeAuthority = createAuthority("WRITE");
        var deleteAuthority = createAuthority("DELETE");

        //createRole(Roles.ROLE_USER.name(), Arrays.asList(readAuthority, writeAuthority));
        var roleAdmin = createRole(Roles.ROLE_ADMIN.name(), Arrays.asList(readAuthority, writeAuthority,
                deleteAuthority));

        var userAdmin = createUser(roleAdmin);

        if (userRepository.getUserEntityByEmail(userAdmin.getEmail()) == null) {
            userRepository.save(userAdmin);
        }
    }

    @Transactional
    private UserEntity createUser(RoleEntity role) {
        var entity = new UserEntity();
        entity.setRoles(Collections.singletonList(role));
        entity.setEmail("admin@admin.com");
        entity.setFirstName("admin");
        entity.setLastName("admin");
        entity.setUserId(UUID.randomUUID().toString());
        entity.setEncryptedPassword(encoder.encode("admin"));

        return entity;
    }

    @Transactional
    private AuthorityEntity createAuthority(String name) {
        AuthorityEntity authority = authorityRepository.findAuthorityEntityByName(name);
        if (authority == null) {
            authority = new AuthorityEntity();
            authority.setName(name);
            authorityRepository.save(authority);
        }

        return authority;
    }

    @Transactional
    private RoleEntity createRole(String name, Collection<AuthorityEntity> authorities) {
        RoleEntity role = roleRepository.findRoleEntityByName(name);
        if (role == null) {
            role = new RoleEntity();
            role.setName(name);
            role.setAuthorities(authorities);
            roleRepository.save(role);
        }

        return role;
    }
}
