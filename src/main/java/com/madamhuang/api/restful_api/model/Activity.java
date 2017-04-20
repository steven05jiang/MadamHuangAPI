package com.madamhuang.api.restful_api.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Activity")
public class Activity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name = "Title")
	private String title;
	@Column(name = "Description")
	private String description;
	@Column(name = "ImageLink")
	private String imageLink;
	@Column(name = "CreatedDate")
	@Temporal(TemporalType.DATE)
	private Date createdDate;
	@Column(name = "StartDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	@Column(name = "EndDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	@Column(name = "Price")
	private int price;
	@Column(name = "MemberPrice")
	private int memberPrice;
	@OneToOne
	@JoinColumn(name = "ArticleId")
	@JsonIgnore
	private Article article;
	@Transient
	private String articleId;
	private boolean isEnable;
	
	public Activity() {
		super();
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

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
	public int getMemberPrice() {
		return memberPrice;
	}

	public void setMemberPrice(int memberPrice) {
		this.memberPrice = memberPrice;
	}

	public void setArticleId(String articleId){
		this.articleId = articleId;
	}
	
	public String getArticleId(){
		if(this.articleId != null) return this.articleId;
		return this.article == null ? null : String.valueOf(this.article.getId());
	}
	
	public void setIsEnable(boolean isEnable){
		this.isEnable = isEnable;
	}
	
	public boolean getIsEnable(){
		return this.isEnable;
	}
	
	
}
