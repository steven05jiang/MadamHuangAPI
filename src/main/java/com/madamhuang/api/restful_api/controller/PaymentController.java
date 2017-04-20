package com.madamhuang.api.restful_api.controller;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.madamhuang.api.restful_api.model.APIRequest;
import com.madamhuang.api.restful_api.model.APIResponse;
import com.madamhuang.api.restful_api.model.Activity;
import com.madamhuang.api.restful_api.model.ActivityDao;
import com.madamhuang.api.restful_api.model.DiscountProduct;
import com.madamhuang.api.restful_api.model.Invoice;
import com.madamhuang.api.restful_api.model.InvoiceDao;
import com.madamhuang.api.restful_api.model.Member;
import com.madamhuang.api.restful_api.model.MemberDao;
import com.madamhuang.api.restful_api.model.Product;
import com.madamhuang.api.restful_api.model.ProductDao;
import com.madamhuang.api.restful_api.model.User;
import com.madamhuang.api.restful_api.model.UserDao;
import com.madamhuang.api.restful_api.model2.*;
import com.madamhuang.api.restful_api.security.JWTHelper;
import com.madamhuang.api.restful_api.util.MemberHelper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class PaymentController {
	
	@Autowired
    private JWTHelper jwtHelper;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ActivityDao activityDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private MemberHelper memberHelper;
    private OkHttpClient client = new OkHttpClient();
    
    
    private static String TOKEN = "sandbox-sq0atb-fnwdsJ-bu92IeOIS1uipwg";
    private static String BASE_URL = "https://connect.squareup.com/v2/";
    private static String LOCATION_ID = "CBASEC0_sNJk2TkOB5VfkYouj_cgAQ";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    public APIResponse processOnePurchase(APIRequest request){
		APIResponse response = new APIResponse();
		
		//Verify Token
		String username = jwtHelper.getUsernameFromToken(request.getToken());
		if(request.getToken() == null || username == null){
			response.setCode("401");
			response.setMessage("Login required");
			return response;
		}
		logger.info("Payment request old token:{'username':{},'old token':{}}",username, request.getToken());
		String token = jwtHelper.refreshToken(request.getToken());
		response.setToken(token);
		logger.info("Payment request refreshed token:{'username':{},'new token':{}}", username, token);
		
		//Verify Membership and create attribute for error controller
		int productCategory = 0;
		int totalPrice = 0;
		boolean isMember = false;
		if(memberHelper.validateMemberByToken(token)){
			isMember = true;
			logger.info("Payment request as member:{'username':{},'token':{}}", username, token);
		}
		
		boolean isSuccessful = false;
		Invoice invoice = null;
		
		
		try{
			Gson gson = new Gson();
			JsonObject body = request.getBody();
			//System.out.println(gson.toJson(body));
			Square_Charge chargeRequest = gson.fromJson(body.get("charge"), Square_Charge.class);
			Invoice productInfo =  gson.fromJson(body.get("info"), Invoice.class);
			
			//Validate charge Request
			if(chargeRequest.getAmount_money() == null){
				response.setCode("400");
				response.setMessage("Money Amount requred.");
				return response;
			}
			if(chargeRequest.getAmount_money().getAmount() > 0 && chargeRequest.getCard_nonce() == null){
				response.setCode("402");
				response.setMessage("Payment Method requred.");
				return response;
			}
			if(!chargeRequest.getAmount_money().getCurrency().equalsIgnoreCase("USD")){
				response.setCode("403");
				response.setMessage("Only accept USD transaction");
				return response;
			}
			if(chargeRequest.getBilling_address().getCountry().equalsIgnoreCase("united states")){
				chargeRequest.getBilling_address().setCountry("US");
			}
			if(chargeRequest.getShipping_address().getCountry().equalsIgnoreCase("united states")){
				chargeRequest.getShipping_address().setCountry("US");
			}
			logger.info("Payment request charge info:{'username':{},'token':{},'money amount':{},'currency':{}, 'card nounce':{}, 'bill country':{}, 'ship country':{}}", username, token, chargeRequest.getAmount_money().getAmount() ,chargeRequest.getAmount_money().getCurrency(), chargeRequest.getCard_nonce(),chargeRequest.getBilling_address().getCountry(),chargeRequest.getShipping_address().getCountry());
			
			//Validate product information
			long productId = productInfo.getProductId();
			productCategory = productInfo.getProductCategory();
			int baseQuantity = productInfo.getBaseQuantity();
			int basePrice = 0;
			int memberQuantity = productInfo.getMemberQuantity();
			int memberPrice = 0;
			//Can only buy one member item
			if(productCategory <= 0 || (productCategory == 1 && baseQuantity <= 0) || baseQuantity < 0 || memberQuantity < 0 || memberQuantity > 1){
				response.setCode("400");
				response.setMessage("Error in product information. Incorrect product category or quantity.");
				return response;
			}
			if(productCategory == 1){
				Product product = productDao.findOne(productId);
				if(product == null){
					response.setCode("400");
					response.setMessage("Error in product information. Incorrect product id.");
					return response;
				}
				basePrice = product.getPrice();
				totalPrice = basePrice*baseQuantity;
				if(product.getDiscount() != null){
					DiscountProduct discount = product.getDiscount();
					if(discount.getIsEnable() && discount.getMinQuantity() <= baseQuantity){
						totalPrice = discount.getNewTotal()+discount.getDiscountPrice()*(baseQuantity-discount.getMinQuantity());
					}
				}
			}else if(productCategory == 2){
				Activity activity = activityDao.findOne(productId);
				if(activity == null){
					response.setCode("400");
					response.setMessage("Error in product information. Incorrect product id.");
					return response;
				}
				basePrice = activity.getPrice();
				memberPrice = activity.getMemberPrice();
				if(!isMember && memberQuantity > 0){
					response.setCode("403");
					response.setMessage("This transaction is required membership.");
					return response;
				}
				totalPrice = basePrice*baseQuantity+memberPrice*memberQuantity;
			}else{
				response.setCode("400");
				response.setMessage("Error in product information. Incorrect product category.");
				logger.error("Payment reques refused for incorrect product category id. User info: {'username':{},'token':{}}",username, token);
				return response;
			}
			
			logger.info("Payment request purchase info:{'username':{},'token':{}, 'type id':{}, 'product id':{}, 'base quantity':{}, 'memeber quantity':{}, 'original total price':{}}", username, token, productCategory, productId, baseQuantity, memberQuantity, totalPrice);
			//Add Tax
			totalPrice = (int) Math.round(1.0625*totalPrice);
			//Add Fee
			totalPrice = (int) Math.round((totalPrice+30)/0.971);
			
			productInfo.setBasePrice(basePrice);
			productInfo.setMemberPrice(memberPrice);
			productInfo.setTotalPrice(totalPrice);
				
			//Verify total and charge number
			if(productInfo.getTotalPrice() != chargeRequest.getAmount_money().getAmount()){
				response.setCode("409");
				response.setMessage("Charge amount not matched product information.");
				logger.error("Payment request refused for not match charge amount. User info:{'username':{},'token':{}}",username, token);
				return response;
			}
			//Assemble charge request
			chargeRequest.setIdempotency_key(UUID.randomUUID().toString());
			RequestBody requestBody = RequestBody.create(JSON, gson.toJson(chargeRequest));
			
			logger.info("Payment request ready to send for {'username':{},'token':{}}", username, token);
			String result = post("locations/"+LOCATION_ID+"/transactions", requestBody);
			if(result == null){
				response.setCode("503");
				response.setMessage("Service Unavailable now.");
				logger.error("Payment request fails for return null from transaction server. User info:{'username':{},'token':{}}", username, token);
				return response;
			}
			JsonParser jsonParser = new JsonParser();
			JsonObject responseBody = jsonParser.parse(result).getAsJsonObject();
			if(responseBody.get("errors") != null){
				response.setCode("500");
				response.setMessage("Service Error: "+ gson.toJson(responseBody.get("errors")));
				logger.error("Payment request fails for errors from transaction server: {'username':{},'token':{},'errors':{}}", username, token, gson.toJson(responseBody.get("errors")));
				return response;
			}
			isSuccessful = true;
			User user = userDao.findByUsername(username);
			productInfo.setUser(user);
			productInfo.setCreatedDate(new Date());
			Square_Address bSquareAddr = chargeRequest.getBilling_address();
			Square_Address sSquareAddr = chargeRequest.getShipping_address();
			String bAddr = null;
			String sAddr = null;
			if(bSquareAddr.getAddress_line_2() != null){
				bAddr = bSquareAddr.getAddress_line_1()+", "+bSquareAddr.getAddress_line_2()+", "+bSquareAddr.getLocality()+", "+bSquareAddr.getAdministrative_district_level_1()+" "+bSquareAddr.getPostal_code()+", "+bSquareAddr.getCountry();
			}else{
				bAddr = bSquareAddr.getAddress_line_1()+", "+bSquareAddr.getLocality()+", "+bSquareAddr.getAdministrative_district_level_1()+" "+bSquareAddr.getPostal_code()+", "+bSquareAddr.getCountry();
			}
			if(sSquareAddr.getAddress_line_2() != null){
				sAddr = sSquareAddr.getAddress_line_1()+", "+sSquareAddr.getAddress_line_2()+", "+sSquareAddr.getLocality()+", "+sSquareAddr.getAdministrative_district_level_1()+" "+sSquareAddr.getPostal_code()+", "+sSquareAddr.getCountry();
			}else{
				sAddr = sSquareAddr.getAddress_line_1()+", "+sSquareAddr.getLocality()+", "+sSquareAddr.getAdministrative_district_level_1()+" "+sSquareAddr.getPostal_code()+", "+sSquareAddr.getCountry();
			}
			
			productInfo.setBillingAddr(bAddr);
			productInfo.setShippingAddr(sAddr);
			productInfo.setTransactionId(responseBody.getAsJsonObject("transaction").get("id").getAsString());
			//System.out.println(productInfo.getTransactionId());
			logger.info("Payment success:{'username':{},'token':{},'transactionId':{}}", username, token, productInfo.getTransactionId());
			invoice = invoiceDao.save(productInfo);
			response.setCode("200");
			response.setBody(invoice);
			logger.info("Invoice is created successfully:{'username':{},'token':{},'transactionId':{}}", username, token, productInfo.getTransactionId());
			
			//Update member status.
			if(!isMember && productCategory == 1 && totalPrice > 10000){
				Member newMember = new Member();
				newMember.setCreatedDate(new Date());
				newMember.setUser(user);
				memberDao.save(newMember);
				isMember = true;
				logger.info("New Membership for {'username':{},'token':{}}", username, token);
			}
			
		}catch (JsonSyntaxException ex){
          	//ex.printStackTrace();
          	response.setCode("400");
          	response.setMessage("Bad request format.");
          	logger.error("Payment request refused for bad format form: {'username':{},'token':{},'cause':{},'message':{},'detail':{}}", username, token, ex.getCause(), ex.getMessage(), ex.getStackTrace());
          	
          }catch(IOException ex){
        	  response.setCode("500");
              response.setMessage("Payment Server Error. Please Try it later.");
              logger.error("Payment request fails cause by remote server connection failure: {'username':{},'token':{},'cause':{},'message':{},'detail':{}}", username, token, ex.getCause(), ex.getMessage(), ex.getStackTrace());
          }catch (Exception ex) {
              ex.printStackTrace();
              if(isSuccessful){
            	  response.setCode("510");
            	  if(invoice == null){
            		  response.setMessage("Payment Successul. But there is an error when creating invoice. Please contact us for more information.");
            		  logger.error("Invoice is created unsuccessfully:{'username':{},'token':{},'cause':{},'message':{},'detail':{}}", username, token, ex.getCause(), ex.getMessage(), ex.getStackTrace());
            	  }else if(productCategory == 1 && totalPrice > 10000 && !isMember){
            		  response.setMessage("Payment and Invoice create successfully. But there is an error when update membership information. Please contact us for more information.");
            		  logger.error("Membership is created unsuccessfully:{'username':{},'token':{},'cause':{},'message':{},'detail':{}}", username, token, ex.getCause(), ex.getMessage(), ex.getStackTrace());
            	  }
              }else{
	              response.setCode("500");
	              response.setMessage("Server Error. Please Try it later.");
	              logger.error("Payment request with error:{'username':{},'token':{},'cause':{},'message':{},'detail':{}}", username, token, ex.getCause(), ex.getMessage(), ex.getStackTrace());
              }
          }
		return response;
    }
    
    //public APIResponse payOneActivity(APIRequest request){}
    
    /*
    
	private Response get(String route){
    	OkHttpClient client = new OkHttpClient();
		
		Request request = new Request.Builder()
				.header("Authorization", "Bearer "+TOKEN)
				.header("Accept", "application/json")
				.header("Content-Type", "application/json")
                .url(BASE_URL+route)
                .get()
                .build();
		System.out.println(request.toString());
		Response response = null;
		try {
			response = client.newCall(request).execute();
			System.out.println(response.body().string());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
    }
    */
	
	private String post(String route, RequestBody body) throws IOException{
		Request request = new Request.Builder()
				.header("Authorization", "Bearer "+TOKEN)
				.header("Accept", "application/json")
				.header("Content-Type", "application/json")
                .url(BASE_URL+route)
                .post(body)
                .build();
	    try (Response response = client.newCall(request).execute()) {
	        return response.body().string();
	      }
    }

}
