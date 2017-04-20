package com.madamhuang.api.restful_api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "DiscountProduct")
public class DiscountProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "ProductId")
	private Product product;
	@Column(name = "MinQuantity")
	private int minQuantity;
	@Column(name = "DiscountPrice")
	private int discountPrice;
	@Column(name = "NewTotal")
	private int newTotal;
	@Column(name = "IsEnable")
	private boolean isEnable;
	public DiscountProduct() {
		super();
		// TODO Auto-generated constructor stub
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getMinQuantity() {
		return minQuantity;
	}
	public void setMinQuantity(int minQuantity) {
		this.minQuantity = minQuantity;
	}
	public int getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(int discountPrice) {
		this.discountPrice = discountPrice;
	}
	public int getNewTotal() {
		return newTotal;
	}
	public void setNewTotal(int newTotal) {
		this.newTotal = newTotal;
	}
	public boolean getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}
	
}
