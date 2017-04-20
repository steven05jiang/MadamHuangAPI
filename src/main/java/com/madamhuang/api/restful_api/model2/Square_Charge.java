package com.madamhuang.api.restful_api.model2;

public class Square_Charge {
	private String idempotency_key;
	private Square_Money amount_money;
	private String card_nonce;
	private String customer_card_id;
	private boolean delay_capture;
	private String reference_id;
	private String note;
	private String customer_id;
	private Square_Address billing_address;
	private Square_Address shipping_address;
	private String buyer_email_address;
	public Square_Charge() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getIdempotency_key() {
		return idempotency_key;
	}
	public void setIdempotency_key(String idempotency_key) {
		this.idempotency_key = idempotency_key;
	}
	public Square_Money getAmount_money() {
		return amount_money;
	}
	public void setAmount_money(Square_Money amount_money) {
		this.amount_money = amount_money;
	}
	public String getCard_nonce() {
		return card_nonce;
	}
	public void setCard_nonce(String card_nonce) {
		this.card_nonce = card_nonce;
	}
	public String getCustomer_card_id() {
		return customer_card_id;
	}
	public void setCustomer_card_id(String customer_card_id) {
		this.customer_card_id = customer_card_id;
	}
	public boolean isDelay_capture() {
		return delay_capture;
	}
	public void setDelay_capture(boolean delay_capture) {
		this.delay_capture = delay_capture;
	}
	public String getReference_id() {
		return reference_id;
	}
	public void setReference_id(String reference_id) {
		this.reference_id = reference_id;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public Square_Address getBilling_address() {
		return billing_address;
	}
	public void setBilling_address(Square_Address billing_address) {
		this.billing_address = billing_address;
	}
	public Square_Address getShipping_address() {
		return shipping_address;
	}
	public void setShipping_address(Square_Address shipping_address) {
		this.shipping_address = shipping_address;
	}
	public String getBuyer_email_address() {
		return buyer_email_address;
	}
	public void setBuyer_email_address(String buyer_email_address) {
		this.buyer_email_address = buyer_email_address;
	}
	

}
