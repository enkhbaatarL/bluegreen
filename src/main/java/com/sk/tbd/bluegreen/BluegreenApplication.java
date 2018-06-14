package com.sk.tbd.bluegreen;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sk.tbd.domain.User;

@RestController
@SpringBootApplication
public class BluegreenApplication {

	public static void main(String[] args) {
		SpringApplication.run(BluegreenApplication.class, args);
	}
	
	@GetMapping("/home") 
	public String home() {
		return "[{\"app\":\"Green\",\"data\":\"home\"}]";
	}
	
	ArrayList <User> users = new ArrayList<>();
	String userName = "";
	
	// 사용자 검색
	@RequestMapping(value = "/search", method= RequestMethod.GET)
	public User searchUser(HttpServletRequest request){
		//URL에서 파라미터 추출
	    Enumeration<String> enumeration = request.getParameterNames();		    
	    while(enumeration.hasMoreElements()){
	        String parameterName = enumeration.nextElement();
	        if (parameterName.equalsIgnoreCase("name")) {
	        	userName = request.getParameter(parameterName);
	        	//System.out.println("userName: "+userName);
	        }	
	    }
	    // 등록된 user list에서 input 파라미터에 맞는 user 검색
	    Iterator<User> it = users.iterator();
	    User returnUser = new User();
		while(it.hasNext()){
			User tempUser = new User();
			tempUser = it.next();
			if (tempUser.getUserName().equalsIgnoreCase(userName)) {				
			    returnUser = tempUser;
			    //System.out.println(tempUser.getUserName());
			}
		}
		return returnUser;
	}
	
	// 사용자 등록
	@RequestMapping(value = "/insert", method=RequestMethod.POST, consumes = "application/json")
	public String saveUser(@RequestBody Map<String, String> user){
		User newUser = new User();
		if (user.get("name").isEmpty()) {
			return "empty";
		}
		else {
			newUser.setUserName(user.get("name"));		
			newUser.setUserPass(user.get("pass"));
			newUser.setDate(new Date());
			users.add(newUser);
		    return "success";
		}
	}
	
	// 사용자 삭제
	@RequestMapping(value = "/delete", method= RequestMethod.DELETE, consumes = "application/json")
	public String deleteUser(@RequestBody Map<String, String> user){		
		// 등록된 user list중 input 파라미터에 맞는 user 검색 후 삭제
		boolean foundYN = false; 
		String deleteUserName = user.get("name");
		if (deleteUserName.isEmpty()) {
			return "empty";
		}
		else {
			Iterator<User> it = users.iterator();	    
			while(it.hasNext()){
				User tempUser = new User();
				tempUser = it.next();
				if (tempUser.getUserName().equalsIgnoreCase(deleteUserName)) {
					it.remove();
					foundYN = true;
				    //System.out.print("delete user: " + user.get("name"));
				}
			}
			if (foundYN) {
				return "success";
			}   
			else {
				return "notfound";
			}
		}
	}
	
	// 사용자 수정
	@RequestMapping(value = "/update", method= RequestMethod.PUT, consumes = "application/json")
	public String updateUser(@RequestBody Map<String, String> user){		
		// 등록된 user list중 input 파라미터에 맞는 user 검색 후 수정
		boolean foundYN = false; 
		String updateUserName = user.get("name");
		String updateUserPass = user.get("pass");
		if (updateUserName.isEmpty()) {
			return "empty";
		}
		else {    
			for(int i=0; i < users.size(); i++) {
				User updateUser = new User();				 
				if (users.get(i).getUserName().equalsIgnoreCase(updateUserName)) {
					updateUser.setUserName(updateUserName);
					updateUser.setUserPass(updateUserPass);
					updateUser.setDate(new Date());
					users.set(i, updateUser);
					foundYN = true;
				}
			}			
			if (foundYN) {
				return "success";
			}   
			else {
				return "notfound";
			}
		}
	}	
	
}
