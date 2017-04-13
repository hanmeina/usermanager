package com.xjtu.usermng.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.xjtu.usermng.domain.User;
import com.xjtu.usermng.util.JdbcUtil;

public class UserDaoImpl implements UserDao{
    private static QueryRunner qr  = new QueryRunner(JdbcUtil.getDataSource());
	/**
	 * 根据用户名查询User
	 * @param loginName
	 * @return User
	 */
	public User getUserByLoginName(String loginName) {
		String sql = "select * from tbl_user where loginname = ?";
		try {
		User user = qr.query(sql,  new BeanHandler<User>(User.class),loginName);
		 return user;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询用户失败");
		}
		
	}
	
	/**
	 * 条件组合查询用户列表
	 */
	@Override
	public List<User> getUserListByConditon(User user) {
	    String sql = "select * from tbl_user where 1=1";
	    List<Object> params = new ArrayList<>();
	    //判断拼装语句和添加参数
	    if(user.getUsername()!=null && !"".equals(user.getUsername().trim())){
	    	  sql = sql + " and username=? ";
	    	  params.add(user.getUsername());
	    }
	    if(user.getEducation()!=null && !"".equals(user.getEducation().trim())){
	    	  sql = sql + " and education=? ";
	    	  params.add(user.getEducation());
	    }
	    if(user.getGender()!=null && !"".equals(user.getGender().trim())){
	    	  sql = sql + " and gender=? ";
	    	  params.add(user.getGender());
	    }
	    if(user.getUpload()!=null && !"".equals(user.getUpload().trim())){
	    	if(user.getUpload().equals("1")){//有简历的
	    		 
	    		sql = sql + " and filepath is not null ";
	    		 
	    	 }
             if(user.getUpload().equals("2")){//无简历的
            	 
            	 sql = sql + " and filepath is null ";
	    		 
	    	 }
	    }
	    
	    try {
		List<User> userList =	qr.query(sql, new BeanListHandler<User>(User.class),params.toArray());
		 return userList;
	    } catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询用户列表失败");
		}
	    
	}
	/**
	 * 保存用户
	 */
   @Override
  public void saveUser(User u) {
	   String sql =	
			"INSERT INTO `tbl_user`                 "+
			" VALUES                                 "+
			"   (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
		
		try {
			int result = qr.update(sql, u.getUid(),u.getUsername(),
					u.getLoginname(),u.getLoginpass(),
					u.getGender(),u.getBirthday(),u.getEducation(),
					u.getCellphone(),u.getHobby(),u.getFilepath(),
					u.getFilename(),u.getRemark());
			if(result!=1){
				throw new RuntimeException("保存用户失败!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("保存用户失败!");
		}
		 
	
    }
   /**
    * 根据id查询用户
    * @param id
    * @return
    */
   @Override
   public User getUserById(String id) {
		
		String sql = " select * from tbl_user where uid = ? ";
		
		try {
			User u  = qr.query(sql, new  BeanHandler<User>(User.class), id);
			//3 杩斿洖
			return u;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询用户失败!");
		}
	}

 /**
  * 根据id删除用户
  */
@Override
public void delete(String uid) {
	// TODO Auto-generated method stub
	String sql= "delete from tbl_user where uid=?";
	try {
		int result = 	qr.update(sql, uid);
		if(result!=1){
			
			throw new RuntimeException("删除用户失败!");
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw new RuntimeException("删除用户失败!");
	}
	
	
}

		@Override
		public void update(User u) {
			String sql =	
					"update tbl_user set username=?,loginname=?, loginpass=?,filepath=?,filename=? where uid= ?";

				try {
					int result = qr.update(sql,u.getUsername(),u.getLoginname(),u.getLoginpass(),u.getFilepath(),u.getFilename(),u.getUid());
					System.out.println(result);
					System.out.println(sql);
					if(result!=1){
						throw new RuntimeException("修改用户失败!");
					}
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException("修改用户失败!");
				}
				 
		}

}
