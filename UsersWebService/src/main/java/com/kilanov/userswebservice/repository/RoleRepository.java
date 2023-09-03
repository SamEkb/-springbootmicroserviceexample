package com.kilanov.userswebservice.repository;

import com.kilanov.userswebservice.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

    RoleEntity findRoleEntityByName(String name);
}
