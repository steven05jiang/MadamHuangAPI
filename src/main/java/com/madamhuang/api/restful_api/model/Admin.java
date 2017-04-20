package com.madamhuang.api.restful_api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "Admin")
public class Admin {

	@Id
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name="gen", strategy="foreign",
    	parameters = @Parameter(name = "property", value="user"))
	@Column(name = "Id")
	private long id;
	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private User user;
	
	public Admin() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Admin(int id, User user) {
		super();
		this.id = id;
		this.user = user;
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
}
