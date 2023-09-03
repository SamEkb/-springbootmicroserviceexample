package com.kilanov.userswebservice.repository;

import com.kilanov.userswebservice.entity.AuthorityEntity;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Long> {

    AuthorityEntity findAuthorityEntityByName(String name);
}
