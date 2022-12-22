package com.posidex.lic.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.posidex.lic.filter.JwtRequestFilter;
import com.posidex.lic.impl.service.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class ConfigureSpringSecurity extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private MyUserDetailsService userdetailsservice;
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	@Autowired 
	private JWTAuthenticationEntryPoint entrypoint;
	
	/*@Bean
	public AuthenticationProvider authProvider()
	{
		DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
		provider.setUserDetailsService(userdetailsservice);
		provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		return provider;
	}*/
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth)throws Exception{
		
		auth.userDetailsService(userdetailsservice);
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
		
		http.csrf().disable().authorizeRequests().antMatchers("/psx/restservice/licms/generatetoken").permitAll().anyRequest().authenticated().and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling().authenticationEntryPoint(entrypoint);
		
		//http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilterAt(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		
	
		/*catch(Exception e)
		{
			Map<String,String> hmap=new HashMap<String,String>();
			hmap.put("message", "Access Denied");
			hmap.put("Error_code", "401");
			hmap.put("Error ","Unauthorized" );
			hmap.put("Status", "false");
		}*/
	}
	
    @Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		
		return super.authenticationManagerBean();
	}


	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return NoOpPasswordEncoder.getInstance();
	}
	
}
