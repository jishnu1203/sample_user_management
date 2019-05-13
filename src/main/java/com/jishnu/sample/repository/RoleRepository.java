package com.jishnu.sample.repository;

import com.jishnu.sample.model.Role;
import com.jishnu.sample.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(RoleType role);
}
