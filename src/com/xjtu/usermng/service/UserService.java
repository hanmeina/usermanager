package com.xjtu.usermng.service;

import java.util.List;

import javax.annotation.Resource;

import com.xjtu.usermng.dao.UserDao;
import com.xjtu.usermng.dao.UserDaoImpl;
import com.xjtu.usermng.domain.User;

public class UserService {
 
  private UserDao userDao = new UserDaoImpl();
  public User loginIn(String loginName){
	  return userDao.getUserByLoginName(loginName);
  }
  public List<User> list(User user){
	  return userDao.getUserListByConditon(user);
  }
  public void  add(User user){
	  userDao.saveUser(user);
  }
  public User view(String id){
	  return userDao.getUserById(id);
	  
  }
   public void delete(String uid) {

    userDao.delete(uid);
	
   }
public void editUser(User user) {
	// TODO Auto-generated method stub
	userDao.update(user);
}
}
