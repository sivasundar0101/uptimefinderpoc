package com.poc.uptimefinder.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "urlstatus")
public class Urlstatus {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	String url;
	String status;
	
	@CreationTimestamp
    @Column(updatable = false)
    Timestamp statusDateTime;
	
	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getStatusDateTime() {
		return statusDateTime;
	}

	public void setStatusDateTime(Timestamp statusDateTime) {
		this.statusDateTime = statusDateTime;
	}

	
	
}
