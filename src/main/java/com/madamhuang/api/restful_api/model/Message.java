package com.madamhuang.api.restful_api.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Message")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name="SenderName")
	private String senderName;
	@Column(name="SenderEmail")
	private String senderEmail;
	@Column(name="SenderTel")
	private String senderTel;
	@Column(name="Content")
	private String content;
	@Column(name="SentDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date sentDate;
	
	public Message() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Message(long id, String senderName, String senderEmail, String senderTel, String content, Date sentDate) {
		super();
		this.id = id;
		this.senderName = senderName;
		this.senderEmail = senderEmail;
		this.senderTel = senderTel;
		this.content = content;
		this.sentDate = sentDate;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getSenderEmail() {
		return senderEmail;
	}
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}
	public String getSenderTel() {
		return senderTel;
	}
	public void setSenderTel(String senderTel) {
		this.senderTel = senderTel;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getSentDate() {
		return sentDate;
	}
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}
	
}
