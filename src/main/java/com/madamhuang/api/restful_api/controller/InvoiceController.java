package com.madamhuang.api.restful_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.madamhuang.api.restful_api.model.APIPaginatedRequest;
import com.madamhuang.api.restful_api.model.APIPaginatedResponse;
import com.madamhuang.api.restful_api.model.APIResponse;
import com.madamhuang.api.restful_api.model.Invoice;
import com.madamhuang.api.restful_api.model.InvoiceDao;
import com.madamhuang.api.restful_api.model.User;
import com.madamhuang.api.restful_api.model.UserDao;
import com.madamhuang.api.restful_api.security.JWTHelper;
import com.madamhuang.api.restful_api.util.AdminHelper;

@Component
public class InvoiceController {

	@Autowired
	private InvoiceDao invoiceDao;
	@Autowired
	private UserDao userDao;
	@Autowired
    private JWTHelper jwtHelper;
    @Autowired
    private AdminHelper adminHelper;
    
  	public APIResponse getPaginatedInvoiceByUser(APIPaginatedRequest request){
  		APIPaginatedResponse response = new APIPaginatedResponse();
  		String token = request.getToken();
  		String username = jwtHelper.getUsernameFromToken(token);
  		if(token == null || username  == null){
  			response.setCode("401");
  			response.setMessage("Login required.");
  			return response;
  		}
  		token = jwtHelper.refreshToken(token);
  		response.setToken(token);
  		
  		try{
  			User user = userDao.findByUsername(username);
  			Pageable pageable = new PageRequest(request.getPage(), request.getSize(), new Sort(Sort.Direction.DESC, "id"));
  			
  			//Page<Invoice> page = invoiceDao.findAll(pageable);
  			Page<Invoice> page = invoiceDao.findByUser(user, pageable);
            
            //System.out.println("Total Pages:" + page.getTotalPages());

            response.setCode("200");
            response.setMessage("Success");
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
  	public APIResponse getPaginatedInvoice(APIPaginatedRequest request){
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
  			
  			Page<Invoice> page = invoiceDao.findAll(pageable);
            
            //System.out.println("Total Pages:" + page.getTotalPages());

            response.setCode("200");
            response.setMessage("Success");
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
