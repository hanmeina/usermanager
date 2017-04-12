package com.xjtu.usermng.web.interceptor;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.xjtu.usermng.domain.User;

public class LoginInterceptor extends MethodFilterInterceptor{
    //凡是进入拦截器，都是有登录状态才能放行
	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		// TODO Auto-generated method stub
	    User user = (User) ActionContext.getContext().getSession().get("user");
	    if(user==null){
	    	return "login";
	    }
	    
	  return  invocation.invoke();
		
		
	}
	

	
}
