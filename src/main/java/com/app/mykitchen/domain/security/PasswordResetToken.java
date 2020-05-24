package com.app.mykitchen.domain.security;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.app.mykitchen.domain.User;

@Entity
public class PasswordResetToken {
	// 1440 minutes
	private static final int EXPIRATION = 60 * 24;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String token;
	
	@OneToOne(cascade=CascadeType.ALL, targetEntity=User.class)
	@JoinColumn(nullable=false, name="user_id")
	private User user;
	
	private Date expiryDate;
	
	public PasswordResetToken() {
		
	}
	
	public PasswordResetToken(final User user, final String token) {
		this.user = user;
		this.token = token;
		this.expiryDate = calculateExpiryDate();
	}
	
    private Date calculateExpiryDate() {
    	Calendar calender = Calendar.getInstance();
    	calender.setTimeInMillis(new Date().getTime());
    	calender.add(Calendar.MINUTE, EXPIRATION);
    	return new Date(calender.getTimeInMillis());
    }
    
    public void updateToken(String token) {
    	this.token = token;
		this.expiryDate = calculateExpiryDate();
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
    
}
