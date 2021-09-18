package com.smartcontactmanager.entities;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.*;

@Entity
@Table(name="USER")
public class User 
{
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private int id;
	
	@NotBlank(message="name cannot be null")
	@Size(min=3, max=20, message = "name must be between 3-20 charactrers")
	private String name;
	@Column(unique = true)
	
	@Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$" ,message="invalid Email")
	private String email;
	private String pwd;
	private String role;
	private boolean enabled;
	private String imgurl;
	@Column(length=500)
	private String about;
		
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")  //,orphanRemoval = true
	private List<Contact> contacts = new ArrayList<>();				
	
	public User() {
		super();	
	}
	public User(int id, String name, String email, String pwd, String role, boolean enabled, String imgurl,
			String about) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.pwd = pwd;
		this.role = role;
		this.enabled = enabled;
		this.imgurl = imgurl;
		this.about = about;
	}
	
	public List<Contact> getC() {
		return contacts;
	}
	public void setC(List<Contact> contacts) {
		this.contacts = contacts;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
//	@Override
//	public String toString() {
//		return "User [id=" + id + ", name=" + name + ", email=" + email + ", pwd=" + pwd + ", role=" + role
//				+ ", enabled=" + enabled + ", imgurl=" + imgurl + ", about=" + about + ", contacts=" + contacts + "]";
//	}
}