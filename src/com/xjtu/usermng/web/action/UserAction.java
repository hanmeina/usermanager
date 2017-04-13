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
			throw new RuntimeException("������");
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
		//����id�����û�
		User user = userService.view(u.getUid());
		//�õ��û����ļ�·�����ļ�����
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
	 * ɾ���û�
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
    	//��ǩ�����Ƿ���ֵջ������ֵջջ��
    	ValueStack vs = ActionContext.getContext().getValueStack();
    	vs.getRoot().push(user);
    	//vs.push();
    	return "edit";  
	}
	
	public String editUser(){
		 String uuid = UUID.randomUUID().toString();
			if(upload2!=null){
			//1.���ϴ����ļ�ת��  
			    //>>��upload�ļ��о���·��
			     String dirPath = 	ServletActionContext.getServletContext().getRealPath("/upload");
			     //>>�����ļ����ƣ�uuid��
			   
			    //>>ת��
			    File targetFile = new File(dirPath+"/"+uuid);
			    upload2.renameTo(targetFile);
			   //2.���ļ���·���Լ��ļ���ԭʼ���Ʒ�װ��User����
			    u.setFilepath("/upload/"+uuid);
			    u.setFilename(upload2FileName);   
			}    
			//����id�����û�
			User user = userService.view(u.getUid());
			//�õ��û����ļ�·�����ļ�����
			filePath = user.getFilepath();
			fileName = user.getFilename();
		userService.editUser(u);
		return "rlist";
	}
	/**
	 * ɾ���û�
	 * @return
	 */
	public String  delete(){
		
		userService.delete(u.getUid());	
		
		return "rlist";
	}
	/**
	 * �鿴�û�����
	 * @return
	 */
    public String view(){
    	User user = userService.view(u.getUid());
    	//��ǩ�����Ƿ���ֵջ������ֵջջ��
    	ValueStack vs = ActionContext.getContext().getValueStack();
    	vs.getRoot().push(user);
    	//vs.push();
    	return "view";
    }
	/**
	 * ����û�
	 * @return
	 */
	public String add(){
		 String uuid = UUID.randomUUID().toString();
		if(upload2!=null){
		//1.���ϴ����ļ�ת��  
		    //>>��upload�ļ��о���·��
		     String dirPath = 	ServletActionContext.getServletContext().getRealPath("/upload");
		     //>>�����ļ����ƣ�uuid��
		   
		    //>>ת��
		    File targetFile = new File(dirPath+"/"+uuid);
		    upload2.renameTo(targetFile);
		   //2.���ļ���·���Լ��ļ���ԭʼ���Ʒ�װ��User����
		    u.setFilepath("/upload/"+uuid);
		    u.setFilename(upload2FileName);   
		}    
		
		//3.ʹ������ַ�������user��id
	      u.setUid(uuid);
	     //4.����service�������û�
          userService.add(u);
		
		return "rlist";
	}
	
	/**
	 * ��ѯ�û��б�
	 * @return
	 */
	public String list(){
	   List<User> userList = userService.list(u);
       Map<String,Object> requestScope = (Map<String, Object>) ActionContext.getContext().get("request");
	   requestScope.put("userList", userList);
		return "list";
	}
	
	
   /**
    * �����û���¼
    * @return
    */
	public String  loginIn(){
		//1.��֤����,��������д
		
		//2.�����û��������ݿ�
	    User user  = userService.loginIn(u.getLoginname());
	    if(user==null){
		 addActionError("�û��������ڣ�");
		 return "login";
	     }	
     	//��֤����
	    if(!u.getLoginpass().equals(user.getLoginpass())){
	    	
	    	 addActionError("���벻��ȷ��");
			 return "login";
	    }
	    //��loginUser ����session
	    Map<String, Object> sessionScope = (Map<String, Object>) ActionContext.getContext().getSession();
		sessionScope.put("user",user);
	   
		return "home";
		
	}
	public void validateLogin(){
		if(u.getLoginname()==null || u.getLoginname().trim().equals("")){
			addFieldError("loginname","�û�������Ϊ��");
		}
		if(u.getLoginpass()==null || u.getLoginpass().trim().equals("")){
			addFieldError("loginpass","���벻��Ϊ��");
		}
	}
}
