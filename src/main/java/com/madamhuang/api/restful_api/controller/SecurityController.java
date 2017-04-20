package com.madamhuang.api.restful_api.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.madamhuang.api.restful_api.model.APIRequest;
import com.madamhuang.api.restful_api.model.APIResponse;
import com.madamhuang.api.restful_api.model.AdminDao;
import com.madamhuang.api.restful_api.model.MemberDao;
import com.madamhuang.api.restful_api.model.User;
import com.madamhuang.api.restful_api.model.UserDao;
import com.madamhuang.api.restful_api.security.JWTHelper;

@Component
public class SecurityController extends AbstractController  {

    // ------------------------
    @Autowired
    private JWTHelper jwtHelper;
    @Autowired
    private UserDao userDao;   
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private MemberDao memberDao;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     *  New User sign up - create new user record
     * @param json
     * @return
     */
    public APIResponse signUp(APIRequest request) {

        APIResponse response = new APIResponse();
        
        try {
            User user = (User) request.parseBody(User.class);
            //System.out.println("JSON: " + request.toString());

            // TODO: check if the user exists and data validation ..
            User storedUser = userDao.findByUsername(user.getUsername());
            logger.info("Signup with username:{}", user.getUsername());

            if(storedUser != null) {
                response.setCode("409");
                response.setMessage("User name exists ...");
                response.setBody(null);
                logger.error("Signup with duplicated username:{}", user.getUsername());
            } else {
                //System.out.println("TOKEN: " + token);
                //String userID = jwtHelper.getUsernameFromToken(token);
                //System.out.println("Get User ID from Token: " + userID);

                // Hash Password testing
                String hashedPassword = User.getSaltedHash(user.getPassword());
                //System.out.println("Hash: " + hashedPassword);
                user.setPassword(hashedPassword);
                userDao.save(user);
                logger.info("Create User.username:{} successfully", user.getUsername());
                
                String token = jwtHelper.generateToken(user.getUsername());
                logger.info("Token is created for User {'username':{},token:{}}", user.getUsername(),token);

                response.setCode("200");
                response.setMessage("Success");
                response.setBody(user);
                response.setToken(token);

            }
        } catch (JsonSyntaxException ex){
        	//ex.printStackTrace();
        	logger.error("Refuse request for bad format signup form: {'cause':{},'message':{},'detail':{}}", ex.getCause(), ex.getMessage(), ex.getStackTrace());
        	response.setCode("400");
        	response.setMessage("Bad signup request format.");
        }  catch (Exception ex) {
            //ex.printStackTrace();
        	logger.error("User signup with error: {'cause':{},'message':{},'detail':{}}", ex.getCause(), ex.getMessage(), ex.getStackTrace());
            response.setCode("500");
            response.setMessage("");
            response.setBody(null);
        }
        //System.out.println(response.getBody());
        

        return response;
    }

    /**
     * User sign in
     * @param json
     * @return
     */
    public APIResponse signIn(APIRequest request) {

        APIResponse response = new APIResponse();
        
        try {

            // Step 2. validate api key
            if(!validateAPIKey(request.getApiKey())) {
                throw new SecurityException("invalid API key");
            }

            //System.out.println("apiKey = " + request.getApiKey() );
            //System.out.println("operator = " + request.getOperator());
            //System.out.println("token = " + request.getToken());
            //System.out.println("body = " + request.getBody().toString());

            // TODO: Check API key and Token

            User user = (User) request.parseBody(User.class);

            //System.out.println("id  = " + user.getId());
            //System.out.println("username = " + user.getUsername());
            //System.out.println("password = " + user.getPassword());

            String username = user.getUsername();
            logger.info("Login try with username:{}", user.getUsername());
            String password =user.getPassword();

            //String hashedPassword =  User.getSaltedHash(params[1]);
            //User user = userDao.findByUsernameAndPassword(params[0], hashedPassword);

            User storedUser = userDao.findByUsername(username);
            if(storedUser != null && User.check(password, storedUser.getPassword())) {
                //System.out.println("password: " + storedUser.getPassword());
                String token = jwtHelper.generateToken(storedUser.getUsername());
                logger.info("Token is created for User {'username':{},'token':{}}", user.getUsername(),token);
                //System.out.println("TOKEN: " + token);
                if(adminDao.findOne(storedUser.getId()) == null){
                	storedUser.setIsAdmin(false);
                }else{
                	storedUser.setIsAdmin(true);
                	logger.info("User {'username':{},'token':{}} login as Admin", user.getUsername(),token);
                }
                if(memberDao.findOne(storedUser.getId()) == null){
                	storedUser.setIsMember(false);
                }else{
                	storedUser.setIsMember(true);
                	logger.info("User {'username':{},'token':{}} login as Member", user.getUsername(),token);
                }
                response.setCode("200");
                response.setMessage("Success");
                response.setToken(token);
                response.setBody(storedUser);
                logger.info("User {'username':{},'token':{}} login successfully", user.getUsername(),token);
            } else {
            	logger.error("Login fail caused by invalid username or password");
                response.setCode("401");
                response.setMessage("User name or password is invalid");
                response.setToken("");
                response.setBody("");
            }

        }catch (JsonSyntaxException ex){
        	//ex.printStackTrace();
        	logger.error("Refuse request for bad format login form: {'cause':{},'message':{},'detail':{}}", ex.getCause(), ex.getMessage(), ex.getStackTrace());
        	response.setCode("400");
        	response.setMessage("Bad signup request format.");
        }catch (Exception ex) {
        	//e.printStackTrace();
        	logger.error("User login with error: {'cause':{},'message':{},'detail':{}}", ex.getCause(), ex.getMessage(), ex.getStackTrace());
            response.setCode("500");
            response.setMessage("Unable to login. Please try later.");
            response.setToken("");
            response.setBody("");
        }

        //System.out.println(response.getBody());

        return response;
    }

    /**
     * Refresh Token
     * @param json
     * @return
     */
    public APIResponse refreshToken(APIRequest request) {

        APIResponse response = new APIResponse();
        try {
            if(!validateAPIKey(request.getApiKey())) throw new SecurityException("invalid API key");

            // TODO: Check API key
            // TODO: Token (Jason Web Token: who makes the request?


            String token = request.getToken();
            String username = jwtHelper.getUsernameFromToken(token);
            System.out.println("Get User ID from Token: " + username);
            System.out.println("Created on: " + jwtHelper.getCreatedDateFromToken(token));
            System.out.println("Expired on: " + jwtHelper.getExpirationDateFromToken(token));

            User storedUser = userDao.findByUsername(username);
            token = jwtHelper.refreshToken(token);
            
            if(adminDao.findOne(storedUser.getId()) == null){
            	storedUser.setIsAdmin(false);
            }else{
            	storedUser.setIsAdmin(true);
            }
            if(memberDao.findOne(storedUser.getId()) == null){
            	storedUser.setIsMember(false);
            }else{
            	storedUser.setIsMember(true);
            }
            response.setCode("200");
            response.setMessage("Success");
            response.setToken(token);
            response.setBody(storedUser);
        }
        catch (SecurityException e) {
            response.setCode("300");
            response.setMessage(e.getMessage());
            response.setToken("");
            response.setBody("");
        }
        System.out.println(response.getBody());

        return response;
    }
    
    public APIResponse changePassword(APIRequest request){
    	APIResponse response = new APIResponse();
    	
    	try{
    		JsonObject object =  request.getBody();
    		//System.out.println(object);
    		String oldPassword = object.get("oldPassword").getAsString();
    		//System.out.println(oldPassword);
    		String newPassword = object.get("newPassword").getAsString();
    		//System.out.println(newPassword);
    		String username = object.get("username").getAsString();
    		//System.out.println(username);
    		String token = request.getToken();
    		logger.info("Change password request from: {'username':{},'old token':{}}", username, token);
    		if(!validateChangePassword(token, username, oldPassword) || newPassword == null){
    			response.setCode("403");
    			response.setMessage("Forbidden. Please login or check necessary information.");
    			logger.error("Change password request refused: {'username':{}, 'token':{}, 'cause':{}}", username, token, "error in request information");
    			return response;
    		}
    		User storedUser = userDao.findByUsername(username);
            // Hash Password testing
            String hashedPassword = User.getSaltedHash(newPassword);
            //System.out.println("Hash: " + hashedPassword);
            storedUser.setPassword(hashedPassword);
            userDao.save(storedUser);
            logger.info("Change password successfully:{'username':{},'token':{}}", username, token);
            token = jwtHelper.refreshToken(token);
            logger.info("Token is refreshed successfully after change password:{'username':{},'token':{}}", username, token);
            if(adminDao.findOne(storedUser.getId()) == null){
            	storedUser.setIsAdmin(false);
            }else{
            	storedUser.setIsAdmin(true);
            	logger.info("Admin status validates successfully after change password:{'username':{},'token':{}}", username, token);
            }
            if(memberDao.findOne(storedUser.getId()) == null){
            	storedUser.setIsMember(false);
            }else{
            	storedUser.setIsMember(true);
            	logger.info("Member status validates successfully after change password:{'username':{},'token':{}}", username, token);
            }
            response.setCode("200");
            response.setToken(token);
            response.setBody(storedUser);
    	}catch (JsonSyntaxException ex){
        	//ex.printStackTrace();
        	response.setCode("400");
        	response.setMessage("Bad signup request format.");
        	logger.error("Change password request refused for bad format form: {'cause':{},'message':{},'detail':{}}", ex.getCause(), ex.getMessage(), ex.getStackTrace());
        }catch (Exception ex) {
        	//e.printStackTrace();
            response.setCode("500");
            response.setMessage("Unable to update. Please try later.");
            response.setToken("");
            response.setBody("");
            logger.error("Change password request refused for server errors: {'cause':{},'message':{},'detail':{}}", ex.getCause(), ex.getMessage(), ex.getStackTrace());
        }
    	return response;
    }
    
    private boolean validateChangePassword(String token, String username, String password) throws Exception{
    	if(token == null || username == null || password == null){
    		return false;
    	}
    	User storedUser = userDao.findByUsername(jwtHelper.getUsernameFromToken(token));
    	logger.info("Ready to validate user information for password change: {'username':{},'token':{}}", username, token);
    	if(storedUser == null || !storedUser.getUsername().equals(username) || !User.check(password, storedUser.getPassword())){
    		return false;
    	}
    	return true;
    }
    
    public APIResponse updateUserProfile(APIRequest request){
    	APIResponse response = new APIResponse();
    	
    	try{
    		User user = (User) request.parseBody(User.class);
    		System.out.println(request.getBody());
    		String token = request.getToken();
    		if(!validateUpdateProfile(token, user.getUsername(), user.getEmail())){
    			response.setCode("403");
    			response.setMessage("Forbidden. Please login or check necessary information.");
    			return response;
    		}
    		User storedUser = userDao.findByUsername(user.getUsername());
    		storedUser.setAddressLine1(user.getAddressLine1());
    		storedUser.setAddressLine2(user.getAddressLine2());
    		storedUser.setCity(user.getCity());
    		storedUser.setCountry(user.getCountry());
    		storedUser.setZipCode(user.getZipCode());
    		storedUser.setEmail(user.getEmail());
    		storedUser.setTel(user.getTel());
    		storedUser.setFirstName(user.getFirstName());
    		storedUser.setLastName(user.getLastName());
    		storedUser.setImageLink(user.getImageLink());
    		userDao.save(storedUser);
    		token = jwtHelper.refreshToken(token);
            if(adminDao.findOne(storedUser.getId()) == null){
            	storedUser.setIsAdmin(false);
            }else{
            	storedUser.setIsAdmin(true);
            }
            if(memberDao.findOne(storedUser.getId()) == null){
            	storedUser.setIsMember(false);
            }else{
            	storedUser.setIsMember(true);
            }
    		response.setCode("200");
    		response.setToken(token);
    		response.setBody(storedUser);
    	} catch (JsonSyntaxException ex){
        	ex.printStackTrace();
        	response.setCode("400");
        	response.setMessage("Bad signup request format.");
        }catch (Exception e) {
        	e.printStackTrace();
            response.setCode("500");
            response.setMessage("Unable to update. Please try later.");
            response.setToken("");
            response.setBody("");
        }
    	return response;
    }
    
    //Return true if allow to update
    private boolean validateUpdateProfile(String token, String username, String email){
		if(token == null || username == null || email == null || !jwtHelper.getUsernameFromToken(token).equals(username)){
			return false;
		}
		return true;
    }

}
