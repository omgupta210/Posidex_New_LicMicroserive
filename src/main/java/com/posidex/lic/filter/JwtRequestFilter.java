package com.posidex.lic.filter;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.posidex.lic.impl.service.JWTUtils;
import com.posidex.lic.impl.service.MyUserDetailsService;



@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	@Autowired
	private MyUserDetailsService userdetailsservice;
	
	@Autowired
	private JWTUtils jwtUtil;


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String authorization=request.getHeader("Authorization");
		
		String username=null;
		String jwt=null;
		
		
		if(authorization !=null && authorization.startsWith("bearer ") )
		{
			jwt=authorization.substring(7);
			username=jwtUtil.extractUsername(jwt);
		}
		
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
		{
			UserDetails userdetails=userdetailsservice.loadUserByUsername(username);
			logger.info("userdetails atJWTfiltertable:"+userdetails);
			if(jwtUtil.validateToken(jwt, userdetails))
			{
				UsernamePasswordAuthenticationToken usernamepasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(
						userdetails,null,userdetails.getAuthorities());
				usernamepasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamepasswordAuthenticationToken);
			}
		}
		filterChain.doFilter(request, response);
		
		
	}
	
	
	

}
