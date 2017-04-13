package com.xjtu.usermng.dao;


import java.util.List;

import com.xjtu.usermng.domain.User;

public interface UserDao {
 public User getUserByLoginName(String loginName);
 public List<User> getUserListByConditon(User user); 
 public void saveUser(User u);
 public User getUserById(String id);
 public void delete(String uid);
public void update(User user);
}
