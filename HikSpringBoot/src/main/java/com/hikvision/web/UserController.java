package com.hikvision.web;

import java.util.List;
import java.util.Random;


import javax.servlet.http.HttpServletRequest;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import com.hikvision.entity.User;
import com.hikvision.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller

@RequestMapping("/user")

public class UserController {

	private static Logger log = LoggerFactory.getLogger(UserController.class);
	@Autowired
    private UserService userService;

    @RequestMapping("/list")
    @ResponseBody
    public List<User> toIndex(HttpServletRequest request, Model model){
    	 List<User> user=null;
        try {
             user = this.userService.list();
             log.debug("查询了一次数据写下日志");
		} catch (Exception e) {
			 System.out.print(e);
			 log.error("List<User> toIndex Error. "+e+"");
		}
 
        return user;
    }
    
    @RequestMapping("/add")
    @ResponseBody
    public  String add(HttpServletRequest request, Model model){
        String str="";
        User user =new User();
        Random rand =new Random(25);
        int i;
        i=rand.nextInt(100);
        user.setUsername("zhang"+i);
        user.setPassword("12345");
        user.setAge(15);
        try {
        	   int isok=userService.insert(user);
        	   log.error("添加成功一条"+user.toString());
        	     if(isok>0)
        	        {
        	        	str="添加成功！";
        	        }else
        	        {
        	        	str="添加失败";
        	        }
		} catch (Exception e) {
			 System.out.print(e);
			 log.debug("String add debug. "+e+"");
		}
     
   
        return str;
    }
    
    @RequestMapping("/delete")
    @ResponseBody
    public String  delete(HttpServletRequest request, Model model){
    	int isok=0;
    	 String str="";
        try {
             isok = this.userService.delete();
             log.info("全部删除了留下的日志");
             if (isok>0) {
            	 str="全部删除成功！";
			}else {
				str="全部删除失败";
			}
		} catch (Exception e) {
			 System.out.print(e);
		}
 
        return str;
    }
    
    @RequestMapping("/getJvmInfo")
    @ResponseBody
    public String  getJvmInfo(HttpServletRequest request, Model model){
    	 String str="";
        try {
        	long totalMemory = Runtime.getRuntime().totalMemory(); //JVM中的初始内存总量
            long maxMemory = Runtime.getRuntime().maxMemory(); //JVM试图使用的最大内存

            str+="totalMemory = " + totalMemory + "Byte 、 " +
                    (totalMemory / (double) 1024 / 1024) + " MB\n";
            str+="MaxMemory = " + maxMemory + " Byte 、 " +
                    (maxMemory / (double) 1024 / 1024) + " MB\n";
        
		} catch (Exception e) {
			 System.out.print(e);
		}
 
        return str;
    }
    


}
