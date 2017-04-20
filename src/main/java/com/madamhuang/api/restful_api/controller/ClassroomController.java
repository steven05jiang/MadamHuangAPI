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
import com.madamhuang.api.restful_api.model.Article;
import com.madamhuang.api.restful_api.model.ArticleDao;
import com.madamhuang.api.restful_api.model.ClassroomItem;
import com.madamhuang.api.restful_api.model.ClassroomItemDao;
import com.madamhuang.api.restful_api.security.JWTHelper;
import com.madamhuang.api.restful_api.util.AdminHelper;

@Component
public class ClassroomController {
	@Autowired
	private ClassroomItemDao ciDao;
    @Autowired
    private JWTHelper jwtHelper;
    @Autowired
    private AdminHelper adminHelper;
	@Autowired
	private ArticleDao articleDao;
    
    public APIResponse getPaginatedItems(APIPaginatedRequest request) {

        APIPaginatedResponse response = new APIPaginatedResponse();
        
  		String token = request.getToken();
  		token = jwtHelper.refreshToken(token);
  		response.setToken(token);

        try {
            //System.out.println("Size: " + request.getSize());
            //System.out.println("Page: " + request.getPage());

            // testing count and pagination
            //System.out.println("Total Count: " + ciDao.count());
            Pageable pageable = new PageRequest(request.getPage(), request.getSize(), new Sort(Sort.Direction.DESC, "updatedDate"));

            Page<ClassroomItem> page = ciDao.findAll(pageable);
            
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

        } catch (SecurityException e) {
            e.printStackTrace();
        	response.setCode("300");
            response.setMessage(e.getMessage());
            response.setToken("");
            response.setBody(e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setCode("500");
            response.setMessage("Server Error. Please try later.");
        }

        //return allObjs;
        return response;
    }
    
  //Admin login required
  	public APIResponse updateClassroomItem(APIRequest request){
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
  			ClassroomItem item = (ClassroomItem) request.parseBody(ClassroomItem.class);
  			if(item == null){
  				response.setCode("400");
  	        	response.setMessage("Bad request format.");
  	        	return response;
  	        }
  			ClassroomItem storeditem = ciDao.findOne(item.getId());
  	        if(storeditem == null){
  	        	response.setCode("404");
  	        	response.setMessage("Classroom item not found");
  	        }else{
  	        	storeditem.setTitle(item.getTitle());
  	        	storeditem.setDescription(item.getDescription());
  	        	storeditem.setImageLink(item.getImageLink());
  		        Article article = null;
  		        if(item.getArticleId() != null){
  		        	article = articleDao.findById(Integer.parseInt(item.getArticleId()));
  		        }
  		        storeditem.setArticle(article);
  		        storeditem.setUpdatedDate(new Date());
  		        storeditem = ciDao.save(storeditem);
  	        	response.setCode("200");
  	        	response.setBody(storeditem);
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
  	
  	public APIResponse addClassroomItem(APIRequest request){
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
  			ClassroomItem item = (ClassroomItem) request.parseBody(ClassroomItem.class);
  	        if(item == null){
  	        	response.setCode("400");
  	        	response.setMessage("Bad request format.");
  	        }else{
  	        	item.setUpdatedDate(new Date());
  		        Article article = null;
  		        if(item.getArticleId() != null){
  		        	article = articleDao.findById(Integer.parseInt(item.getArticleId()));
  		        }
  		        item.setArticle(article);
  	        	item = ciDao.save(item);
  	        	response.setCode("200");
  	        	response.setBody(item);
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
  	
  	public APIResponse deleteClassroomItem(APIRequest request){
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
  			ClassroomItem item = (ClassroomItem) request.parseBody(ClassroomItem.class);
  			ClassroomItem storedItem = ciDao.findOne(item.getId());
  	        if(storedItem == null){
  	        	response.setCode("400");
  	        	response.setMessage("Bad request format.");
  	        }else{
  	        	ciDao.delete(storedItem);
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
