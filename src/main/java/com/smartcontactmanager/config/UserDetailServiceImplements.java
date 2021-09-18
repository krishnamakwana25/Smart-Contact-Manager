package com.smartcontactmanager.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.smartcontactmanager.dao.UserRepository;
import com.smartcontactmanager.entities.User;
public class UserDetailServiceImplements implements UserDetailsService 
{
	@Autowired
	private UserRepository ur;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		User userByUsername = ur.getUserByUsername(username);
		
		if(userByUsername == null)
		{
			throw new UsernameNotFoundException("Couldn't Found User..!");
		}
		CustomUserDetails cud = new CustomUserDetails(userByUsername);
		return cud;
	}
}