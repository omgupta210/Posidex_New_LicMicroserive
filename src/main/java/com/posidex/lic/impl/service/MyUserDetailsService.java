package com.posidex.lic.impl.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.posidex.lic.model.JwtUser;

 
@Service
public class MyUserDetailsService implements UserDetailsService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
   @Autowired
   private JdbcTemplate jdbctemplate;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		String q="select user_name,password from psx_user where user_name='"+username+"'";
	    JwtUser user=jdbctemplate.queryForObject(q, (rs,rowNum)-> new JwtUser(rs.getString("user_name"),rs.getString("password")));
	   logger.info("userdetails:"+ user);
	    if (user == null) {
            throw new UsernameNotFoundException(username);
        }
	    return new org.springframework.security.core.userdetails.User(user.getUser_name(), user.getPassword(),
				new ArrayList<>());
		
		
		
		
	}

}
