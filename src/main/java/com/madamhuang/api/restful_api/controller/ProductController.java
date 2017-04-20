package com.madamhuang.api.restful_api.controller;

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
import com.madamhuang.api.restful_api.model.Product;
import com.madamhuang.api.restful_api.model.ProductDao;
import com.madamhuang.api.restful_api.security.JWTHelper;
import com.madamhuang.api.restful_api.util.AdminHelper;

@Component
public class ProductController {
	@Autowired
	private ProductDao productDao;
    @Autowired
    private JWTHelper jwtHelper;
    @Autowired
    private AdminHelper adminHelper;
	@Autowired
	private ArticleDao articleDao;
    
	public ProductController(){
		
	}
	
	public APIResponse getProductById(APIRequest request){
		APIResponse response = new APIResponse();
  		String token = request.getToken();
  		token = jwtHelper.refreshToken(token);
  		response.setToken(token);
  		
		try{
			Product product = (Product) request.parseBody(Product.class);
			Product storedProduct = productDao.findOne(product.getId());
			response.setCode("200");
			response.setBody(storedProduct);
		} catch(Exception ex){
			ex.printStackTrace();
			response.setCode("500");
		}
		return response;
	}
	
	public APIResponse getProducts(APIRequest request){
		APIResponse response = new APIResponse();
  		String token = request.getToken();
  		token = jwtHelper.refreshToken(token);
  		response.setToken(token);
		try{
			List<Product> products = (List<Product>) productDao.findAll();
			response.setCode("200");
			response.setBody(products);
		} catch(Exception ex){
			ex.printStackTrace();
			response.setCode("500");
		}
		return response;
	}
	
	public APIResponse getPaginatedProducts(APIPaginatedRequest request){
  		APIPaginatedResponse response = new APIPaginatedResponse();
  		String token = request.getToken();
  		token = jwtHelper.refreshToken(token);
  		response.setToken(token);
  		
  		try{
  			Pageable pageable = new PageRequest(request.getPage(), request.getSize(), new Sort(Sort.Direction.DESC, "id"));
  			
  			Page<Product> page = productDao.findAll(pageable);
            
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
  	public APIResponse updateProduct(APIRequest request){
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
  			Product product = (Product) request.parseBody(Product.class);
  			if(product == null){
  				response.setCode("400");
  	        	response.setMessage("Bad request format.");
  	        	return response;
  	        }
  			Product storedProduct = productDao.findOne(product.getId());
  	        if(storedProduct == null){
  	        	response.setCode("404");
  	        	response.setMessage("Activity not found");
  	        }else{
  	        	storedProduct.setTitle(product.getTitle());
  	        	storedProduct.setDescription(product.getDescription());
  	        	storedProduct.setImageLink(product.getImageLink());
  	        	storedProduct.setPrice(product.getPrice());
  	        	storedProduct.setDetail(product.getDetail());
  		        Article article = null;
  		        if(product.getArticleId() != null){
  		        	article = articleDao.findById(Integer.parseInt(product.getArticleId()));
  		        }
  		        storedProduct.setArticle(article);
  		        storedProduct = productDao.save(storedProduct);
  	        	response.setCode("200");
  	        	response.setBody(storedProduct);
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
  	
  	public APIResponse addProduct(APIRequest request){
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
  			Product product = (Product) request.parseBody(Product.class);
  	        if(product == null){
  	        	response.setCode("400");
  	        	response.setMessage("Bad request format.");
  	        }else{
  		        Article article = null;
  		        if(product.getArticleId() != null){
  		        	article = articleDao.findById(Integer.parseInt(product.getArticleId()));
  		        }
  		        product.setArticle(article);
  		        product = productDao.save(product);
  	        	response.setCode("200");
  	        	response.setBody(product);
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
  	
  	public APIResponse deleteProduct(APIRequest request){
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
  			Product product = (Product) request.parseBody(Product.class);
  			Product storedProduct = productDao.findOne(product.getId());
  	        if(storedProduct == null){
  	        	response.setCode("400");
  	        	response.setMessage("Bad request format.");
  	        }else{
  	        	productDao.delete(storedProduct);
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
