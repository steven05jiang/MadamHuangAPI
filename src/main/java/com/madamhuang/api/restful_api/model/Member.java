package com.madamhuang.api.restful_api.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "Member")
public class Member {

	@Id
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name="gen", strategy="foreign",
    	parameters = @Parameter(name = "property", value="user"))
	@Column(name = "Id")
	private long id;
	@Column(name = "SquareId")
	private String squareId;
	@Column(name = "CreatedDate")
	@Temporal(TemporalType.DATE)
	private Date createdDate;
	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private User user;
	
	public Member(long id, User user) {
		super();
		this.id = id;
		this.user = user;
	}
	public Member() {
		super();
		// TODO Auto-generated constructor stub
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getSquareId() {
		return squareId;
	}
	public void setSquareId(String squareId) {
		this.squareId = squareId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	
	
}
