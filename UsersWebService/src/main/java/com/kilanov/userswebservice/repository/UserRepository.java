package com.kilanov.userswebservice.repository;

import com.kilanov.userswebservice.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    UserEntity getUserEntityByEmail(String email);
}
