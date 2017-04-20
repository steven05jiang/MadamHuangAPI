package com.madamhuang.api.restful_api.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.madamhuang.api.restful_api.model.AdminDao;
import com.madamhuang.api.restful_api.model.User;
import com.madamhuang.api.restful_api.model.UserDao;
import com.madamhuang.api.restful_api.security.JWTHelper;

@Component
public class AdminHelper {
    @Autowired
    private JWTHelper jwtHelper;
    @Autowired
    private UserDao userDao;
    @Autowired
    private AdminDao adminDao;
    
    public boolean validateAdminByToken(String token){
    	if(token == null) return false;
    	String username = jwtHelper.getUsernameFromToken(token);
    	User storedUser = userDao.findByUsername(username);
    	if(storedUser == null || adminDao.findOne(storedUser.getId()) == null) return false;
    	return true;
    }
}
