package com.jishnu.sample.repository;

import com.jishnu.sample.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserName(String username);

    User findByEmail(String email);

}
