package com.smartcontactmanager.controller;
import com.smartcontactmanager.dao.UserRepository;
import com.smartcontactmanager.entities.User;
import com.smartcontactmanager.helper.Message;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder PasswordEncoder;
	@Autowired
	private UserRepository ur;
	@RequestMapping("/")
	public String Home(Model model)
	{		
		model.addAttribute("title","Home - Smart Contact Manager");
		return "Home";
	}
	
	@RequestMapping("/about")
	public String About(Model model)
	{		
		model.addAttribute("title","About - Smart Contact Manager");
		return "About";
	}
	
	@RequestMapping("/signup")
	public String SignUp(Model model)
	{		
		model.addAttribute("title","Register - Smart Contact Manager");
		model.addAttribute("user",new User());
		return "SignUp";
	}
	
	//user - register code 
	@RequestMapping(value="/do_signup", method=RequestMethod.POST)
	public String signupUser(@Valid @ModelAttribute("user") User user,BindingResult r1,
			@RequestParam(value = "agreement",defaultValue = "false") boolean agreement, 
			Model model, HttpSession session)
	{ 
		try 
		{
			if(!agreement)
			{				
				throw new Exception("You have not agreed the T&C");
			}
			if(r1.hasErrors())
			{
				model.addAttribute("user",user);
				return "SignUp";
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImgurl("default.png");
			user.setPwd(PasswordEncoder.encode(user.getPwd()));
			User result = this.ur.save(user);			
			model.addAttribute("user",new User());
			session.setAttribute("message", new Message("Successfully registered..!", "alert-success"));
			return "SignUp";
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something went wrong..!"+e.getMessage(), "alert-danger"));
			return "SignUp";
		}		
	}	
	
	//login code
	@RequestMapping("/signin")
	public String Login(Model model)
	{		
		model.addAttribute("title","Login page - Smart Contact Manager");
		return "Login";
	}
}
/*
 * @Autowired private UserRepository ur;
 * 
 * @GetMapping("/test")
 * 
 * @ResponseBody public String test() { User user = new User();
 * user.setName("Krishna"); user.setEmail("krishnarmakwana7312@gmail.com");
 * ur.save(user); return "working"; }
 */