package com.smartcontactmanager.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name="CONTACT")
public class Contact 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cid;
	private String name;
	private String secondname;
	private String work;
	private String email;
	private String phone;
	private String img;
	@Column(length=6000)
	private String description;
	
	@ManyToOne()
	@JsonIgnore   //user ne serialize nhi kre only contact ne j krse 
	private User user ; 
	
	public Contact() {
		super();
	}
	public Contact(int cid, String name, String secondname, String work, String email, String phone, String img,
			String description) {
		super();
		this.cid = cid;
		this.name = name;
		this.secondname = secondname;
		this.work = work;
		this.email = email;
		this.phone = phone;
		this.img = img;
		this.description = description;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSecondname() {
		return secondname;
	}
	public void setSecondname(String secondname) {
		this.secondname = secondname;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "Contact [cid=" + cid + ", name=" + name + ", secondname=" + secondname + ", work=" + work + ", email="
				+ email + ", phone=" + phone + ", img=" + img + ", description=" + description + ", user=" + user + "]";
	}

}
