package com.smartcontactmanager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smartcontactmanager.dao.ContactRepository;
import com.smartcontactmanager.dao.UserRepository;
import com.smartcontactmanager.entities.Contact;
import com.smartcontactmanager.entities.User;

@RestController
public class SeachController 
{
	@Autowired
	private UserRepository ur;
	@Autowired
	private ContactRepository cr;
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query, Principal p )
	{
		User usernm = this.ur.getUserByUsername(p.getName()); //je pn currently login hoy enu id, user,details aapse
		List<Contact> contacts = this.cr.findByNameContainingAndUser(query,usernm);
		return ResponseEntity.ok(contacts);
	}
}
