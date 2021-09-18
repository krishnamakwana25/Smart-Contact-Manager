package com.smartcontactmanager.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@Configuration
@EnableWebSecurity
public class MyConfiguration extends WebSecurityConfigurerAdapter
{
	@Bean
	public UserDetailsService getUserDetailsService()
	{
		return new UserDetailServiceImplements();
	}
	
	@Bean
	public BCryptPasswordEncoder PasswordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider()
	{
		DaoAuthenticationProvider daoauthenticationprovider = new DaoAuthenticationProvider();
		
		daoauthenticationprovider.setUserDetailsService(this.getUserDetailsService());
		daoauthenticationprovider.setPasswordEncoder(PasswordEncoder());
		
		return daoauthenticationprovider;
	}

	//configure method..
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
		.antMatchers("/user/**").hasRole("USER")
		.antMatchers("/**").permitAll().and().formLogin()
		.loginPage("/signin")
		.defaultSuccessUrl("/user/index")
		/*.loginProcessingUrl("do_signup")
		.defaultSuccessUrl("/user/index")
				.failureUrl("/login-fail")*/
		.and().csrf().disable();
		
		//loginPage("/signin")-using this code after formlogin() we can direct user to custom login page
		
		/* by default spring boot provides its own security loginform*/
		/*
		 http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
		.antMatchers("/user/**").hasRole("USER")
		.antMatchers("/**").permitAll().and().formLogin().loginPage("/signin").and().csrf().disable();
		 */
	}	
	
}