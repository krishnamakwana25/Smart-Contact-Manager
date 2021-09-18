package com.smartcontactmanager.controller;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcontactmanager.dao.UserRepository;
import com.smartcontactmanager.entities.User;
import com.smartcontactmanager.service.EmailService;
@Controller
public class ForgotPasswordController 
{
	Random randomOTP = new Random(1000);
	@Autowired
	private UserRepository ur ; 
	@Autowired
	private EmailService es;
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	@GetMapping("/forgotpassword")
	//open email form by clicking forgot password
	public String OpenEMailForm(Model m)
	{
		m.addAttribute("title","Forgot Password Form");
		return "ForgotEmailForm";		
	}
	
	//send-otp
	@PostMapping("/send-otp")
	public String SendOTP(@RequestParam("email")String email, Model m, HttpSession session)
	{
//		m.addAttribute("title","Forgot Password Form");
		
		//generarting otp 4 digit
		int otp = randomOTP.nextInt(999999);
		System.out.println("otp : "+otp);
		String subject = "OTP from Smart Contact Manager - 2021 ";
		String message = ""
				+ "<div style='border:1px solid gray; padding:20px;'>"
				+ "<h4>"
				+ "OTP is "
				+ "<b><u style='color:green;'>"+otp
				+ "<u></b></n>"				
				+ "</h4>"
				+ "</div>";
		
		String to = email;
		boolean result1 = this.es.sendEmail(subject,message,to);
		System.out.println(email);
		if(result1)
		{
			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);
			session.setAttribute("message","We have sent OTP to your Email");
			return "VerifyOTP";
		}
		else
		{
			session.setAttribute("message","Check Your E-Mail Id");
			return "ForgotEmailForm" ;
		}		
		//write code for send otp to email			
	}	
	
	//verify otp
	@PostMapping("/verify-otp")
	public String VerifyOTP(@RequestParam("otp") int otp,HttpSession session, Model m)
	{
		int myotp = (int)session.getAttribute("myotp");		
		String email = (String) session.getAttribute("email");
		User user = this.ur.getUserByUsername(email);				
		if(myotp==otp)
		{
			if(user==null)
			{
				//send err message
				session.setAttribute("message","User does not exist with this mail Id.");
				return "ForgotEmailForm" ;				
			}
			else
			{
				//send change password form
			}
			System.out.println("hello");
			m.addAttribute("title","Verify OTP");
			return "PasswordChangeForm";
		}
		//current user :- com.smartcontactmanager.entities.User@72ed58bf
		else
		{
			m.addAttribute("title","Forgot Password Form");
			session.setAttribute("message", "You have entered wrong OTP");
			return "VerifyOTP";
		}
	}
	
	//change-password
	@PostMapping("/change-password")
	public String ChangePassword(@RequestParam("newpassword") String newpwd, HttpSession session)
	{
		String email = (String) session.getAttribute("email");
		User user = this.ur.getUserByUsername(email);
		user.setPwd(this.bcrypt.encode(newpwd));
		this.ur.save(user);
		return "redirect:/signin?change=Password Change Successfully";
	}
}