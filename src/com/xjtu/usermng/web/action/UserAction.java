package com.xjtu.usermng.web.action;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.management.RuntimeErrorException;
import javax.servlet.Servlet;
import javax.sound.midi.VoiceStatus;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;
import com.xjtu.usermng.domain.User;
import com.xjtu.usermng.service.UserService;

public class UserAction  extends ActionSupport implements  ModelDriven<User>{
   
	private UserService userService = new UserService();
	User u = new User();
	@Override
	public User getModel() {
		// TODO Auto-generated method stub
		return u;
	}
	private File upload2;
	
	
    public File getUpload2() {
		return upload2;
	}

	public void setUpload2(File upload2) {
		this.upload2 = upload2;
	}
	private String upload2FileName;
    
	

	public String getUpload2FileName() {
		return upload2FileName;
	}

	public void setUpload2FileName(String upload2FileName) {
		this.upload2FileName = upload2FileName;
	}
	private String filePath ;
	private String fileName;
	
	public String getFileName(){
		try {
			System.out.println("fileName"+fileName);
			return URLEncoder.encode(fileName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("不可能");
		}
	}
	
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public InputStream getDownloadDoc(){
		return ServletActionContext.getServletContext().getResourceAsStream(filePath);
	}
	
	public String download(){
		//根据id查找用户
		User user = userService.view(u.getUid());
		//拿到用户的文件路径，文件名称
		filePath = user.getFilepath();
		fileName = user.getFilename();
		System.out.println("filePath:"+filePath);
		System.out.println("fileName:"+fileName);
		return "download";
		
	}
	private List<String> hobbyList;
	
	public List<String> getHobbyList() {
		return hobbyList;
	}

	public void setHobbyList(List<String> hobbyList) {
		this.hobbyList = hobbyList;
	}

	/**
	 * 删除用户
	 * @return
	 */
	public String  edit(){
		User user = userService.view(u.getUid());
		String hobby = user.getHobby();
		 hobbyList= new ArrayList<String>();  
		   String [] referealReasons =hobby.split(", ");  
		   for(String b :referealReasons){  
		    hobbyList.add(b);  
		   } 
		//System.out.println(hobby);
    	//标签回显是放入值栈，放入值栈栈顶
    	ValueStack vs = ActionContext.getContext().getValueStack();
    	vs.getRoot().push(user);
    	//vs.push();
    	return "edit";  
	}
	
	public String editUser(){
		 String uuid = UUID.randomUUID().toString();
			if(upload2!=null){
			//1.将上传的文件转存  
			    //>>找upload文件夹绝对路径
			     String dirPath = 	ServletActionContext.getServletContext().getRealPath("/upload");
			     //>>生成文件名称（uuid）
			   
			    //>>转存
			    File targetFile = new File(dirPath+"/"+uuid);
			    upload2.renameTo(targetFile);
			   //2.将文件的路径以及文件的原始名称封装到User对象
			    u.setFilepath("/upload/"+uuid);
			    u.setFilename(upload2FileName);   
			}    
			//根据id查找用户
			User user = userService.view(u.getUid());
			//拿到用户的文件路径，文件名称
			filePath = user.getFilepath();
			fileName = user.getFilename();
		userService.editUser(u);
		return "rlist";
	}
	/**
	 * 删除用户
	 * @return
	 */
	public String  delete(){
		
		userService.delete(u.getUid());	
		
		return "rlist";
	}
	/**
	 * 查看用户详情
	 * @return
	 */
    public String view(){
    	User user = userService.view(u.getUid());
    	//标签回显是放入值栈，放入值栈栈顶
    	ValueStack vs = ActionContext.getContext().getValueStack();
    	vs.getRoot().push(user);
    	//vs.push();
    	return "view";
    }
	/**
	 * 添加用户
	 * @return
	 */
	public String add(){
		 String uuid = UUID.randomUUID().toString();
		if(upload2!=null){
		//1.将上传的文件转存  
		    //>>找upload文件夹绝对路径
		     String dirPath = 	ServletActionContext.getServletContext().getRealPath("/upload");
		     //>>生成文件名称（uuid）
		   
		    //>>转存
		    File targetFile = new File(dirPath+"/"+uuid);
		    upload2.renameTo(targetFile);
		   //2.将文件的路径以及文件的原始名称封装到User对象
		    u.setFilepath("/upload/"+uuid);
		    u.setFilename(upload2FileName);   
		}    
		
		//3.使用随机字符串设置user的id
	      u.setUid(uuid);
	     //4.调用service，保存用户
          userService.add(u);
		
		return "rlist";
	}
	
	/**
	 * 查询用户列表
	 * @return
	 */
	public String list(){
	   List<User> userList = userService.list(u);
       Map<String,Object> requestScope = (Map<String, Object>) ActionContext.getContext().get("request");
	   requestScope.put("userList", userList);
		return "list";
	}
	
	
   /**
    * 处理用户登录
    * @return
    */
	public String  loginIn(){
		//1.验证参数,在配置中写
		
		//2.根据用户名查数据库
	    User user  = userService.loginIn(u.getLoginname());
	    if(user==null){
		 addActionError("用户名不存在！");
		 return "login";
	     }	
     	//验证密码
	    if(!u.getLoginpass().equals(user.getLoginpass())){
	    	
	    	 addActionError("密码不正确！");
			 return "login";
	    }
	    //将loginUser 放入session
	    Map<String, Object> sessionScope = (Map<String, Object>) ActionContext.getContext().getSession();
		sessionScope.put("user",user);
	   
		return "home";
		
	}
	public void validateLogin(){
		if(u.getLoginname()==null || u.getLoginname().trim().equals("")){
			addFieldError("loginname","用户名不能为空");
		}
		if(u.getLoginpass()==null || u.getLoginpass().trim().equals("")){
			addFieldError("loginpass","密码不能为空");
		}
	}
}
