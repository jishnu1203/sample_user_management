package com.jishnu.sample.service;

import com.jishnu.sample.model.Role;
import com.jishnu.sample.model.RoleType;
import com.jishnu.sample.model.User;
import com.jishnu.sample.model.UserInfo;
import com.jishnu.sample.repository.RoleRepository;
import com.jishnu.sample.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRespository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUser(User user, List<String> userRoles) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setFirstName(user.getFirstName());
        user.setUserName(user.getUserName());
        user.setEmail(user.getEmail());
        HashSet<Role> roles = new HashSet<>();
        for(String role: userRoles) {
            Role userRole = roleRespository.findByName(RoleType.valueOf(role));
            roles.add(userRole);
        }
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public List<UserInfo> getUserList() {
        List<User> users = userRepository.findAll();
        List<UserInfo> userList = new ArrayList<>();
        for (User user1 : users) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user1.getId());
            userInfo.setUserName(user1.getUserName());
            userInfo.setEmail(user1.getEmail());
            userInfo.setFirstName(user1.getFirstName());
            List<String> userRoles = user1.getRoles().stream().map(role1 -> role1.getName().name()).collect(Collectors.toList());
            userInfo.setRoles(String.join(", ", userRoles));
            userList.add(userInfo);
        }
        return userList;
    }

    public User findUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
