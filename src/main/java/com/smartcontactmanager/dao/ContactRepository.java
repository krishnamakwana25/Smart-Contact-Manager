package com.smartcontactmanager.dao;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.smartcontactmanager.entities.Contact;
import com.smartcontactmanager.entities.User;
public interface ContactRepository extends JpaRepository<Contact, Integer>
{
	//pagination :-A page is a sublist of a list of objects. It allows gain information about the position of it in the containing entire list.
	//Pageable :- Abstract interface for pagination information. 
	//pageable has two info. 1) currentpage = page 2) contact per page = 5
	//getting records from the database for view purpose
	
	@Query("from Contact as c where c.user.id=:userid")
	public Page<Contact> findContactsById(@Param("userid")int userid, Pageable pePageable);
	
	//search functionality
	public List<Contact> findByNameContainingAndUser(String name, User user);
}
