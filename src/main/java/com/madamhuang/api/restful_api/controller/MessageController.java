package com.madamhuang.api.restful_api.controller;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.google.gson.JsonSyntaxException;
import com.madamhuang.api.restful_api.model.APIPaginatedRequest;
import com.madamhuang.api.restful_api.model.APIPaginatedResponse;
import com.madamhuang.api.restful_api.model.APIRequest;
import com.madamhuang.api.restful_api.model.APIResponse;
import com.madamhuang.api.restful_api.model.Message;
import com.madamhuang.api.restful_api.model.MessageDao;
import com.madamhuang.api.restful_api.security.JWTHelper;
import com.madamhuang.api.restful_api.util.AdminHelper;

@Component
public class MessageController {
	
	@Autowired
	private MessageDao messageDao;
	@Autowired
    private JWTHelper jwtHelper;
    @Autowired
    private AdminHelper adminHelper;
    
    public APIResponse createMessage(APIRequest request){
    	APIResponse response = new APIResponse();
    	
    	try{
    		Message message = (Message) request.parseBody(Message.class);
    		if(message.getSenderName() == null || message.getSenderEmail() == null || message.getContent() == null){
    			response.setCode("400");
    			response.setMessage("Not sufficient information.");
    			return response;
    		}
    		message.setSentDate(new Date());
    		messageDao.save(message);
    		response.setCode("200");
    		response.setMessage("Send Message Successfully.");
    	}catch (JsonSyntaxException ex){
        	ex.printStackTrace();
        	response.setCode("400");
        	response.setMessage("Bad message request format.");
        }  catch (Exception ex) {
            ex.printStackTrace();
            response.setCode("500");
            response.setMessage("Server Error. Please try it later.");
            response.setBody(null);
        }
    	return response;
    }
    
    //Admin login required
  	public APIResponse getPaginatedMsgs(APIPaginatedRequest request){
  		APIPaginatedResponse response = new APIPaginatedResponse();
  		String token = request.getToken();
  		if(!adminHelper.validateAdminByToken(token)){
  			response.setCode("401");
  			response.setMessage("Please login as Admin");
  			return response;
  		}
  		token = jwtHelper.refreshToken(token);
  		response.setToken(token);
  		
  		try{
  			Pageable pageable = new PageRequest(request.getPage(), request.getSize(), new Sort(Sort.Direction.DESC, "id"));
  			
  			Page<Message> page = messageDao.findAll(pageable);
            
            //System.out.println("Total Pages:" + page.getTotalPages());

            response.setCode("200");
            response.setMessage("Success");
            //response.setBody(allObjs);
            response.setBody(page.getContent());

            //response.setPage(page.);
            response.setTotalPages(page.getTotalPages());
            response.setTotalElements(page.getTotalElements());
            response.setFirst(page.isFirst());
            response.setLast(page.isLast());
            response.setNumber(page.getNumber());
            response.setNumberOfElements(page.getNumberOfElements());
            response.setSort(page.getSort());
  		} catch (Exception ex) {
              ex.printStackTrace();
              response.setCode("500");
              response.setMessage("Server Error. Please try it later.");
              response.setBody(null);
          }
  		return response;
  	}
}
