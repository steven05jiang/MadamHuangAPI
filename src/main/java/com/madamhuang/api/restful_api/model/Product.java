package com.madamhuang.api.restful_api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Product")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name = "Title")
	private String title;
	@Column(name = "Description")
	private String description;
	@Column(name = "ImageLink")
	private String imageLink;
	@Column(name = "Price")
	private int price;
	@Column(name = "Detail")
	private String detail;
	@OneToOne
	@JoinColumn(name = "ArticleId")
	@JsonIgnore
	private Article article;
	@Transient
	private String articleId;
	@OneToOne(mappedBy = "product", fetch=FetchType.EAGER)
	private DiscountProduct discount;
	
	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Product(long id, String title, String description, String imageLink, int price, Article article) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.imageLink = imageLink;
		this.price = price;
		this.article = article;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImageLink() {
		return imageLink;
	}
	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
	}
	public void setArticleId(String articleId){
		this.articleId = articleId;
	}	
	public String getArticleId(){
		if(this.articleId != null) return this.articleId;
		return this.article == null ? null : String.valueOf(this.article.getId());
	}
	public DiscountProduct getDiscount() {
		return discount;
	}
	public void setDiscount(DiscountProduct discount) {
		this.discount = discount;
	}
}
