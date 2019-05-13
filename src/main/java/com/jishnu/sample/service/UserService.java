package com.jishnu.sample.service;

import com.jishnu.sample.model.User;
import com.jishnu.sample.model.UserInfo;

import java.util.List;

public interface UserService {

    public User findUserByEmail(String email);

    public User findUserByUserName(String userName);

    public void saveUser(User user,List<String> userRoles);

    public List<UserInfo> getUserList();
}
