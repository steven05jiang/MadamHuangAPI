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
import com.madamhuang.api.restful_api.model.Article;
import com.madamhuang.api.restful_api.model.ArticleDao;
import com.madamhuang.api.restful_api.security.JWTHelper;
import com.madamhuang.api.restful_api.util.AdminHelper;

@Component
public class ArticleController {

	@Autowired
	private ArticleDao articleDao;
    @Autowired
    private JWTHelper jwtHelper;
    @Autowired
    private AdminHelper adminHelper;
	
	public ArticleController(){}
	
	public APIResponse getArticleById(APIRequest request){
		APIResponse response = new APIResponse();
  		String token = request.getToken();
  		token = jwtHelper.refreshToken(token);
  		response.setToken(token);
		
		try{
			Article article = (Article) request.parseBody(Article.class);
	        System.out.println("JSON: " + request.toString());
	        Article storedArticle = articleDao.findById(article.getId());
	        if(storedArticle == null){
	        	response.setCode("404");
	        	response.setMessage("Article not found");
	        }else{
	        	response.setCode("200");
	        	response.setBody(storedArticle);
	        }
		} catch (JsonSyntaxException ex){
        	ex.printStackTrace();
        	response.setCode("400");
        	response.setMessage("Bad request format.");
        }  catch (Exception ex) {
            ex.printStackTrace();
            response.setCode("500");
            response.setMessage("");
            response.setBody(null);
        }
		return response;
	}
	
	public APIResponse getArticles(APIRequest request){
		APIResponse response = new APIResponse();
  		String token = request.getToken();
  		token = jwtHelper.refreshToken(token);
  		response.setToken(token);
		
		try{
			List<Article> articles = (List<Article>) articleDao.findAll();
	        response.setCode("200");
	        response.setBody(articles);
		}  catch (Exception ex) {
            ex.printStackTrace();
            response.setCode("500");
            response.setMessage("");
            response.setBody(null);
        }
		return response;
	}
	
    //Admin login required
  	public APIResponse getPaginatedArticles(APIPaginatedRequest request){
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
  			
  			Page<Article> page = articleDao.findAll(pageable);
            
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
	public APIResponse updateArticle(APIRequest request){
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
			Article article = (Article) request.parseBody(Article.class);
			if(article == null){
				response.setCode("400");
	        	response.setMessage("Bad request format.");
	        	return response;
	        }
	        Article storedArticle = articleDao.findById(article.getId());
	        if(storedArticle == null){
	        	response.setCode("404");
	        	response.setMessage("Article not found");
	        }else{
		        storedArticle.setTitle(article.getTitle());
		        storedArticle.setContent(article.getContent());
		        storedArticle.setUpdatedDate(new Date());
		        storedArticle = articleDao.save(storedArticle);
	        	response.setCode("200");
	        	response.setBody(storedArticle);
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
	
	public APIResponse addArticle(APIRequest request){
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
			Article article = (Article) request.parseBody(Article.class);
	        if(article == null){
	        	response.setCode("400");
	        	response.setMessage("Bad request format.");
	        }else{
	        	Date date = new Date();
	        	article.setCreatedDate(date);
	        	article.setUpdatedDate(date);
	        	article = articleDao.save(article);
	        	response.setCode("200");
	        	response.setBody(article);
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
	
	public APIResponse deleteArticle(APIRequest request){
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
			Article article = (Article) request.parseBody(Article.class);
	        Article storedArticle = articleDao.findById(article.getId());
	        if(storedArticle == null){
	        	response.setCode("400");
	        	response.setMessage("Bad request format.");
	        }else{
	        	articleDao.delete(storedArticle);
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
