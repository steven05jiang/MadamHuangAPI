package com.madamhuang.api.restful_api.controller;

import java.util.Date;
import java.util.List;

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
import com.madamhuang.api.restful_api.model.Activity;
import com.madamhuang.api.restful_api.model.ActivityDao;
import com.madamhuang.api.restful_api.model.Article;
import com.madamhuang.api.restful_api.model.ArticleDao;
import com.madamhuang.api.restful_api.security.JWTHelper;
import com.madamhuang.api.restful_api.util.AdminHelper;

@Component
public class ActivityController {

	@Autowired
	private ActivityDao activityDao;
    @Autowired
    private JWTHelper jwtHelper;
    @Autowired
    private AdminHelper adminHelper;
	@Autowired
	private ArticleDao articleDao;
    
	public ActivityController(){
		
	}
	
	public APIResponse getActivityById(APIRequest request){
		APIResponse response = new APIResponse();
  		String token = request.getToken();
  		token = jwtHelper.refreshToken(token);
  		response.setToken(token);
  		
		try{
			Activity activity = (Activity) request.parseBody(Activity.class);
			Activity storedActivity = activityDao.findOne(activity.getId());
			response.setCode("200");
			response.setBody(storedActivity);
		} catch(Exception ex){
			ex.printStackTrace();
			response.setCode("500");
		}
		return response;
	}
	
	public APIResponse getActivities(APIRequest request){
		APIResponse response = new APIResponse();
  		String token = request.getToken();
  		token = jwtHelper.refreshToken(token);
  		response.setToken(token);
		
		try{
			List<Activity> activities = (List<Activity>) activityDao.findAll();
			response.setCode("200");
			response.setBody(activities);
		} catch(Exception ex){
			ex.printStackTrace();
			response.setCode("500");
		}
		return response;
	}
	
  	public APIResponse getPaginatedActivities(APIPaginatedRequest request){
  		APIPaginatedResponse response = new APIPaginatedResponse();
  		String token = request.getToken();
  		token = jwtHelper.refreshToken(token);
  		response.setToken(token);
  		
  		try{
  			Pageable pageable = new PageRequest(request.getPage(), request.getSize(), new Sort(Sort.Direction.DESC, "id"));
  			
  			Page<Activity> page = activityDao.findAll(pageable);
            
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
  	
  //Admin login required
  	public APIResponse updateActivity(APIRequest request){
  		APIResponse response = new APIResponse();
  		String token = request.getToken();
  		if(!adminHelper.validateAdminByToken(token)){
  			response.setCode("401");
  			response.setMessage("Please login as Admin");
  			return response;
  		}
  		token = jwtHelper.refreshToken(token);
  		response.setToken(token);
  		
  		try{
  			Activity activity = (Activity) request.parseBody(Activity.class);
  			if(activity == null){
  				response.setCode("400");
  	        	response.setMessage("Bad request format.");
  	        	return response;
  	        }
  			Activity storedActivity = activityDao.findOne(activity.getId());
  	        if(storedActivity == null){
  	        	response.setCode("404");
  	        	response.setMessage("Activity not found");
  	        }else{
  		        storedActivity.setTitle(activity.getTitle());
  		        storedActivity.setDescription(activity.getDescription());
  		        storedActivity.setImageLink(activity.getImageLink());
  		        storedActivity.setPrice(activity.getPrice());
  		        storedActivity.setStartDate(activity.getStartDate());
  		        storedActivity.setEndDate(activity.getEndDate());
  		        storedActivity.setMemberPrice(activity.getMemberPrice());
  		        storedActivity.setIsEnable(activity.getIsEnable());
  		        Article article = null;
  		        if(activity.getArticleId() != null){
  		        	article = articleDao.findById(Integer.parseInt(activity.getArticleId()));
  		        }
  		        storedActivity.setArticle(article);
  		        storedActivity = activityDao.save(storedActivity);
  	        	response.setCode("200");
  	        	response.setBody(storedActivity);
  	        }
  		} catch (JsonSyntaxException ex){
          	ex.printStackTrace();
          	response.setCode("400");
          	response.setMessage("Bad request format.");
          }  catch (Exception ex) {
              ex.printStackTrace();
              response.setCode("500");
              response.setMessage("Server Error. Please try it later.");
              response.setBody(null);
          }
  		return response;
  	}
  	
  	public APIResponse addActivity(APIRequest request){
  		APIResponse response = new APIResponse();
  		String token = request.getToken();
  		if(!adminHelper.validateAdminByToken(token)){
  			response.setCode("401");
  			response.setMessage("Please login as Admin");
  			return response;
  		}
  		token = jwtHelper.refreshToken(token);
  		response.setToken(token);
  		
  		try{
  			Activity activity = (Activity) request.parseBody(Activity.class);
  	        if(activity == null){
  	        	response.setCode("400");
  	        	response.setMessage("Bad request format.");
  	        }else{
  	        	activity.setCreatedDate(new Date());
  		        Article article = null;
  		        if(activity.getArticleId() != null){
  		        	article = articleDao.findById(Integer.parseInt(activity.getArticleId()));
  		        }
  		        activity.setArticle(article);
  	        	activity = activityDao.save(activity);
  	        	response.setCode("200");
  	        	response.setBody(activity);
  	        }
  		} catch (JsonSyntaxException ex){
          	ex.printStackTrace();
          	response.setCode("400");
          	response.setMessage("Bad request format.");
          }  catch (Exception ex) {
              ex.printStackTrace();
              response.setCode("500");
              response.setMessage("Server Error. Please try it later.");
              response.setBody(null);
          }
  		return response;
  	}
  	
  	public APIResponse deleteActivity(APIRequest request){
  		APIResponse response = new APIResponse();
  		String token = request.getToken();
  		if(!adminHelper.validateAdminByToken(token)){
  			response.setCode("401");
  			response.setMessage("Please login as Admin");
  			return response;
  		}
  		token = jwtHelper.refreshToken(token);
  		response.setToken(token);
  		
  		try{
  			Activity activity = (Activity) request.parseBody(Activity.class);
  			Activity storedActivity = activityDao.findOne(activity.getId());
  	        if(storedActivity == null){
  	        	response.setCode("400");
  	        	response.setMessage("Bad request format.");
  	        }else{
  	        	activityDao.delete(storedActivity);
  	        	response.setCode("200");
  	        }
  		} catch (JsonSyntaxException ex){
          	ex.printStackTrace();
          	response.setCode("400");
          	response.setMessage("Bad request format.");
          }  catch (Exception ex) {
              ex.printStackTrace();
              response.setCode("500");
              response.setMessage("Server Error. Please try it later.");
              response.setBody(null);
          }
  		return response;
  	}
}
