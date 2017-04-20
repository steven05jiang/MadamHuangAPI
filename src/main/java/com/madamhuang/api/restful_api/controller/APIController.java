package com.madamhuang.api.restful_api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.madamhuang.api.restful_api.model.APIPaginatedRequest;
import com.madamhuang.api.restful_api.model.APIRequest;
import com.madamhuang.api.restful_api.model.APIResponse;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping(value = "api/v1")
public class APIController {

	private final APIResponse BAD_REQUEST = new APIResponse("400", "Bad Request Format.");
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SecurityController securityController;
	@Autowired
	private ActivityController activityController;
	@Autowired
	private ProductController productController;
	@Autowired
	private ArticleController articleController;
	@Autowired
	private MessageController messageController;
	@Autowired
	private ClassroomController classroomController;
	@Autowired
	private PaymentController paymentController;
	@Autowired
	private InvoiceController invoiceController;
	
	//*********** APIRequest Converter start**************//
	
	@SuppressWarnings("finally")
	private APIRequest requestConvert(String json){
		APIRequest request = null;
		try{
			request = APIRequest.parse(json);
		}catch(Exception ex){
			logger.error("Refuse request in bad format: {'cause':{},'message':{},'detail':{}}", ex.getCause(), ex.getMessage(), ex.getStackTrace());
		}finally{
			return request;
		}
	}
	
	@SuppressWarnings("finally")
	private APIPaginatedRequest paginatedRequestConvert(String json){
		APIPaginatedRequest request = null;
		try{
			request = APIPaginatedRequest.parse(json);
		}catch(Exception ex){
			logger.error("Refuse pagination request in bad format: {'cause':{},'message':{},'detail':{}}", ex.getCause(), ex.getMessage(), ex.getStackTrace());
		}finally{
			return request;
		}
	}
	
	//*********** APIRequest Converter start**************//
	
	
	
	//*********** Security related APIs start**************//
	
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    //@ResponseBody
    public APIResponse signUp(@RequestBody String json) {
    	APIRequest request = requestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return securityController.signUp(request);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    //@ResponseBody
    public APIResponse signIn(@RequestBody String json) {
    	APIRequest request = requestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return securityController.signIn(request);
    }

    @RequestMapping(value = "/refresh-token", method = RequestMethod.POST)
    //@ResponseBody
    public APIResponse refreshToken(@RequestBody String json) {
    	APIRequest request = requestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return securityController.refreshToken(request);
    }
    
    @RequestMapping(value = "/user-update", method = RequestMethod.POST)
    //@ResponseBody
    public APIResponse updateUser(@RequestBody String json) {
    	APIRequest request = requestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return securityController.updateUserProfile(request);
    }
    
    @RequestMapping(value = "/user-update-password", method = RequestMethod.POST)
    //@ResponseBody
    public APIResponse changePassword(@RequestBody String json) {
    	APIRequest request = requestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return securityController.changePassword(request);
    }
    
  //*********** Security related APIs end**************//

    //*********** Activity related APIs start**************//
    
    @RequestMapping(value = "/activity", method = RequestMethod.POST)
    //@ResponseBody
    public APIResponse getActivityById(@RequestBody String json) {
    	APIRequest request = requestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return activityController.getActivityById(request);
    }
    
    @RequestMapping(value = "/activities", method = RequestMethod.POST)
    public APIResponse getPaginatedActivities(@RequestBody String json) {
    	APIPaginatedRequest request = paginatedRequestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return activityController.getPaginatedActivities(request);
    }
    
    @RequestMapping(value = "/activity-update", method = RequestMethod.POST)
    public APIResponse updateActivity(@RequestBody String json) {
    	APIRequest request = requestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return activityController.updateActivity(request);
    }
    
    @RequestMapping(value = "/activity-add", method = RequestMethod.POST)
    public APIResponse addActivity(@RequestBody String json) {
    	APIRequest request = requestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return activityController.addActivity(request);
    }
    
    @RequestMapping(value = "/activity-delete", method = RequestMethod.POST)
    public APIResponse deleteActivity(@RequestBody String json) {
    	APIRequest request = requestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return activityController.deleteActivity(request);
    }
    
    
    
    //*********** Activity related APIs end**************//
    
    //*********** Classroom related APIs start**************//
    
    @RequestMapping(value = "/classroomitems", method = RequestMethod.POST)
    public APIResponse getPaginateClassroomItems(@RequestBody String json) {
    	APIPaginatedRequest request = paginatedRequestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return classroomController.getPaginatedItems(request);
    }
    
    @RequestMapping(value = "/classroom-item-update", method = RequestMethod.POST)
    public APIResponse updateClassroomItem(@RequestBody String json) {
    	APIRequest request = requestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return classroomController.updateClassroomItem(request);
    }
    
    @RequestMapping(value = "/classroom-item-add", method = RequestMethod.POST)
    public APIResponse addClassroomItem(@RequestBody String json) {
    	APIRequest request = requestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return classroomController.addClassroomItem(request);
    }
    
    @RequestMapping(value = "/classroom-item-delete", method = RequestMethod.POST)
    public APIResponse deleteClassroomItem(@RequestBody String json) {
    	APIRequest request = requestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return classroomController.deleteClassroomItem(request);
    }
    
    //*********** Classroom related APIs end**************//
    
    
    //*********** Product related APIs start**************//
    
    @RequestMapping(value = "/product", method = RequestMethod.POST)
    //@ResponseBody
    public APIResponse getProductById(@RequestBody String json) {
    	APIRequest request = requestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return productController.getProductById(request);
    }
    
    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public APIResponse getPaginatedProducts(@RequestBody String json) {
    	APIPaginatedRequest request = paginatedRequestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return productController.getPaginatedProducts(request);
    }
    
    @RequestMapping(value = "/product-update", method = RequestMethod.POST)
    public APIResponse updateProduct(@RequestBody String json) {
    	APIRequest request = requestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return productController.updateProduct(request);
    }
    
    @RequestMapping(value = "/product-add", method = RequestMethod.POST)
    public APIResponse addProduct(@RequestBody String json) {
    	APIRequest request = requestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return productController.addProduct(request);
    }
    
    @RequestMapping(value = "/product-delete", method = RequestMethod.POST)
    public APIResponse deleteProduct(@RequestBody String json) {
    	APIRequest request = requestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return productController.deleteProduct(request);
    }
    
    //*********** Product related APIs end**************//
    
    
    //*********** Article related APIs start**************//
    
    @RequestMapping(value = "/article", method = RequestMethod.POST)
    //@ResponseBody
    public APIResponse getArticleById(@RequestBody String json) {
    	APIRequest request = requestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return articleController.getArticleById(request);
    }
    
    /*
    
    @CrossOrigin
    @RequestMapping(value = "/articles", method = RequestMethod.POST)
    //@ResponseBody
    public APIResponse getArticles(@RequestBody String json) {
    	APIRequest request = requestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return articleController.getArticles(request);
    }
    */
    
    @RequestMapping(value = "/articles", method = RequestMethod.POST)
    //@ResponseBody
    public APIResponse getPaginateArticles(@RequestBody String json) {
    	APIPaginatedRequest request = paginatedRequestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return articleController.getPaginatedArticles(request);
    }
    
    @RequestMapping(value = "/article-update", method = RequestMethod.POST)
    //@ResponseBody
    public APIResponse updateArticle(@RequestBody String json) {
    	APIRequest request = requestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return articleController.updateArticle(request);
    }
    
    @RequestMapping(value = "/article-add", method = RequestMethod.POST)
    //@ResponseBody
    public APIResponse addArticle(@RequestBody String json) {
    	APIRequest request = requestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return articleController.addArticle(request);
    }
    
    @RequestMapping(value = "/article-delete", method = RequestMethod.POST)
    //@ResponseBody
    public APIResponse deleteArticle(@RequestBody String json) {
    	APIRequest request = requestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return articleController.deleteArticle(request);
    }
    
    
  //*********** Article related APIs end**************//
    
    
    //*********** Message related APIs start**************//
    
    @RequestMapping(value = "/send-message", method = RequestMethod.POST)
    //@ResponseBody
    public APIResponse createMessage(@RequestBody String json) {
    	APIRequest request = requestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return messageController.createMessage(request);
    }
    
    @RequestMapping(value = "/messages", method = RequestMethod.POST)
    //@ResponseBody
    public APIResponse getPaginatedMessages(@RequestBody String json) {
    	APIPaginatedRequest request = paginatedRequestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return messageController.getPaginatedMsgs(request);
    }
    
  //*********** Message related APIs end**************//
    
  //*********** Payment related APIs start**************//
    
    @RequestMapping(value = "/purchase", method = RequestMethod.POST)
    //@ResponseBody
    public APIResponse purchase(@RequestBody String json) {
    	APIRequest request = requestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return paymentController.processOnePurchase(request);
    }
    
    //*********** Payment related APIs end**************//
    
    //*********** Invoice related APIs start**************//
    
    @RequestMapping(value = "/invoices", method = RequestMethod.POST)
    public APIResponse getPaginatedInvoices(@RequestBody String json) {
    	APIPaginatedRequest request = paginatedRequestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return invoiceController.getPaginatedInvoice(request);
    }
    
    @RequestMapping(value = "/invoices-user", method = RequestMethod.POST)
    public APIResponse getPaginatedInvoicesByUser(@RequestBody String json) {
    	APIPaginatedRequest request = paginatedRequestConvert(json);
    	if(request == null){
    		return BAD_REQUEST;
    	}
        return invoiceController.getPaginatedInvoiceByUser(request);
    }
    
  //*********** Invoice related APIs end**************//
    
}
