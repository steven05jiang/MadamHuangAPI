package com.madamhuang.api.restful_api.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Invoice")
public class Invoice {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name = "Note")
	private String note;
	@Column(name = "TotalPrice")
	private int totalPrice;
	@Column(name = "BasePrice")
	private int basePrice;
	@Column(name = "BaseQuantity")
	private int baseQuantity;
	@Column(name = "MemberPrice")
	private int memberPrice;
	@Column(name = "MemberQuantity")
	private int memberQuantity;
	@Column(name = "Comment")
	private String comment;
	@Column(name = "CreatedDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	// 0 for unknown, 1 for product, and 2 for activity
	@Column(name = "ProductCategory")
	private int productCategory;
	// Refer to the id of specific category. 0 for unknown.
	@Column(name = "ProductId")
	private long productId;
	@Column(name = "Receiver")
	private String receiver;
	@Column(name = "Email")
	private String email;
	@Column(name = "ShippingAddr")
	private String shippingAddr;
	@Column(name = "BillingAddr")
	private String billingAddr;
	@Column(name = "TransactionId")
	private String transactionId;
	@ManyToOne
	@JoinColumn(name = "UserId")
	@JsonIgnore
	private User user;
	public Invoice() {
		super();
		// TODO Auto-generated constructor stub
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	public int getBasePrice() {
		return basePrice;
	}
	public void setBasePrice(int basePrice) {
		this.basePrice = basePrice;
	}
	public int getBaseQuantity() {
		return baseQuantity;
	}
	public void setBaseQuantity(int baseQuantity) {
		this.baseQuantity = baseQuantity;
	}
	public int getMemberPrice() {
		return memberPrice;
	}
	public void setMemberPrice(int memberPrice) {
		this.memberPrice = memberPrice;
	}
	public int getMemberQuantity() {
		return memberQuantity;
	}
	public void setMemberQuantity(int memberQuantity) {
		this.memberQuantity = memberQuantity;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(int productCategory) {
		this.productCategory = productCategory;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getShippingAddr() {
		return shippingAddr;
	}
	public void setShippingAddr(String shippingAddr) {
		this.shippingAddr = shippingAddr;
	}
	public String getBillingAddr() {
		return billingAddr;
	}
	public void setBillingAddr(String billingAddr) {
		this.billingAddr = billingAddr;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}
