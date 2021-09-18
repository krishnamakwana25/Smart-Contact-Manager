package com.smartcontactmanager.controller;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.smartcontactmanager.helper.Message;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.smartcontactmanager.dao.ContactRepository;
import com.smartcontactmanager.dao.MyOrderRepository;
import com.smartcontactmanager.dao.UserRepository;
import com.smartcontactmanager.entities.Contact;
import com.smartcontactmanager.entities.MyOrder;
import com.smartcontactmanager.entities.User;
import com.razorpay.*;
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private BCryptPasswordEncoder bpwd;
	@Autowired
	private UserRepository ur;
	@Autowired
	private ContactRepository cr;
	@Autowired
	private MyOrderRepository mor;
	// method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model m, Principal p) {
		String unm = p.getName();
		User user = ur.getUserByUsername(unm);
		m.addAttribute("user", user);
	}

	// home dash-board
	@RequestMapping("/index")
	public String dashboard(Model m, Principal p) {
		m.addAttribute("title", "User Dashboard");
		return "user/user_dashboard";
	}

	// add contact
	@GetMapping("/addcontact")
	public String openAddContact(Model m) {
		m.addAttribute("title", "Add Contact");
		m.addAttribute("contact", new Contact());
		return "user/AddContact";
	}

	// filling data innto database
//	@RequestMapping(value = { "/process-contact" }, method = RequestMethod.POST)
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile fileobj,
			Principal p, HttpSession session) {
		try {
			String name = p.getName();
			User user = this.ur.getUserByUsername(name);
			// processing & uploading file
			if (fileobj.isEmpty()) {
//				System.out.println("File is Empty");
				contact.setImg("contact.png");
			} else {
				contact.setImg(fileobj.getOriginalFilename());
				File savefile = new ClassPathResource("static/images").getFile();
				Path path = Paths.get(savefile.getAbsolutePath() + File.separator + fileobj.getOriginalFilename());
				Files.copy(fileobj.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//				System.out.println("image uploaded");
			}
			// over
			contact.setUser(user); // contact ne user aapvu
			user.getC().add(contact); // user ne contact aapva db ma foreign key type work kkrse aa code
			this.ur.save(user);			
			session.setAttribute("message", new Message("Your Contact added successfully..!", "success"));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong, Try Again..!", "danger"));
		}
		return "user/AddContact";
	}

	// view contact
	// pagination per page = 10
	// current page = 0 page
	@GetMapping("/showcontact/{page}")
	public String ShowContct(@PathVariable("page") Integer page, Model m, Principal p) {
		m.addAttribute("title", "Show User Contacts");
//		--------------------One method to get data from db----------------------------
		// fetch contacts data from db
//		String name = p.getName();
//		User userByUsername = this.ur.getUserByUsername(name);
//		List<Contact> contacts = userByUsername.getC();
//		System.out.println(contacts);
		// ------------------------------------------------
//		--------------------Second method to get data from db----------------------------
		String name = p.getName();
		User userByUsername = this.ur.getUserByUsername(name);
		// pageable has two info. 1) currentpage = page 2) contact per page = 10
		Pageable pageable = PageRequest.of(page, 7);
		Page<Contact> contacts = this.cr.findContactsById(userByUsername.getId(), pageable);
		m.addAttribute("contacts", contacts);
		m.addAttribute("CurrentPage", page);
		m.addAttribute("TotalPages", contacts.getTotalPages());		
		return "user/ShowContact";
	}

	// showing particular contact details
	@GetMapping("/{cid}/particularcontact")
	public String showContactDetails(@PathVariable("cid") Integer cid, Model m, Principal p) {
		try {
			Optional<Contact> onecontact = this.cr.findById(cid);
			Contact contact = onecontact.get();
			String username = p.getName();
			User user = this.ur.getUserByUsername(username);
			if (user.getId() == contact.getUser().getId()) {
				m.addAttribute("contact", contact);
				m.addAttribute("title", contact.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "user/ParticularContact";
	}

	// delete contact
	@GetMapping("/deletecontact/{cid}")
	public String deleteContactById(@PathVariable("cid") Integer cid, Model m, Principal p, HttpSession session) {
		try {
			Optional<Contact> findcontact = this.cr.findById(cid);
			Contact contact = findcontact.get();
			String username = p.getName();
			User user = this.ur.getUserByUsername(username);
			if (user.getId() == contact.getUser().getId()) {
				contact.setUser(null);
				this.cr.delete(contact);
			}
			m.addAttribute("contact", contact);
			session.setAttribute("message", new Message("Contact deleted successfully..!", "success"));
			return "redirect:/user/showcontact/0";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "user/ShowContact";
	}

	// open update form
	@PostMapping("/updatecontact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid, Model m) {
		m.addAttribute("title", "Update Contact Form");
		Contact contact = this.cr.findById(cid).get();
		m.addAttribute("contact", contact);
		return "user/UpdateForm";
	}

	// to add updated value into db
	@RequestMapping(value = "/process-updatedata", method = RequestMethod.POST)
	public String UpdateContactData(@ModelAttribute Contact contact1, @RequestParam("profileImage") MultipartFile file, Model m, HttpSession session, Principal p) 
	{
		try 
		{
			Contact oldcontactdetails = this.cr.findById(contact1.getCid()).get();			
			if(!file.isEmpty())
			{
				/*DELETE OLD PHOTO*/
				File deletefile = new ClassPathResource("static/images").getFile();
				File file1 = new File(deletefile,oldcontactdetails.getImg());
				//contact1.setImg("contact.png");
				file1.delete();
				/*OVER*/
				
				/*UPDATE NEW PHOTO*/
				File savefile = new ClassPathResource("static/images").getFile();
				Path path = Paths.get(savefile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact1.setImg(file.getOriginalFilename());
				/*OVER*/
			}	
			else
			{
				contact1.setImg(oldcontactdetails.getImg());
			}
			User user = this.ur.getUserByUsername(p.getName()); 
			contact1.setUser(user);
			this.cr.save(contact1);
			session.setAttribute("message", new Message("Your contact details are updated..!", "success"));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return "redirect:/user/"+contact1.getCid()+"/particularcontact";
	}
	
	//user profile
	@GetMapping("/userprofile")
	public String UserProfile(Model m, Principal p )
	{
		String unm = p.getName();
		m.addAttribute("title","Profile Page");
		User user = ur.getUserByUsername(unm);		
		m.addAttribute("user", user);
		return "user/UserProfile";
	}
	
	//open setting
	@GetMapping("/settings")
	public String OpenSetting(Model m)
	{
		m.addAttribute("title","Settings Page");
		return "user/Settings";
	}
	
	//change password - old to new
	@PostMapping("/changepassword")
	public String ChangePassword(@RequestParam("oldpwd") String oldpwd1,@RequestParam("newpwd") String newpwd1, Principal p ,HttpSession session)
	{
		String name = p.getName();
		User currentuser = this.ur.getUserByUsername(name);
		/* System.out.println("user:- "+currentuser.getPwd()); */
		if(this.bpwd.matches(oldpwd1, currentuser.getPwd()))
		{
			//change the password
			currentuser.setPwd(this.bpwd.encode(newpwd1));
			this.ur.save(currentuser);
			session.setAttribute("message", new Message("Your password is successfully changed.", "success"));
		}
		else
		{
			//error..
			session.setAttribute("message", new Message("Please enter correct old password.", "danger"));
			return "redirect:/user/settings";
		}		
		return "redirect:/user/index";
	}
	
	//creating order for payment
	@PostMapping("/create_order")
	@ResponseBody
	public String createOrder(@RequestBody Map<String, Object> data, Principal p) throws Exception
	{
		System.out.println(data);		
		
		int amt = Integer.parseInt(data.get("amount").toString());
		
		var client = new RazorpayClient("rzp_test_ZzOd8m4fdvzVcr", "aPTRBCe9Gs5iVdhsOq5bShgy");
		
		JSONObject ob = new JSONObject();
		ob.put("amount", amt*100);
		//paisa pass krvana so *100 kryu che
		ob.put("currency", "INR");
		ob.put("receipt", "txn_123456");
		
		//creating new order
		Order order = client.Orders.create(ob);
		System.out.println(order);
		
		//if you want you can save this to your data..
		MyOrder myOrder = new MyOrder();
		myOrder.setAmount(order.get("amount")+"");
		myOrder.setOrderid(order.get("id"));
		myOrder.setPaymentid(null);
		myOrder.setStatus("created");
		myOrder.setUser(this.ur.getUserByUsername(p.getName()));
		myOrder.setReceipt(order.get("receipt"));
		
		this.mor.save(myOrder);
		
		return order.toString();
	}
	
//	update_order
	@PostMapping("/update_order")
	public ResponseEntity<?> updateOrder(@RequestBody Map<String, Object> data)
	{
		MyOrder myorder = this.mor.findByOrderid(data.get("order_id").toString());
		myorder.setPaymentid(data.get("payment_id").toString());
		myorder.setStatus(data.get("status").toString());
		this.mor.save(myorder);
		
		System.out.println(data);
		return ResponseEntity.ok(Map.of("msg","updated"));
	}
}