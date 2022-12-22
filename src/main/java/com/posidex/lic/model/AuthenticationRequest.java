package com.posidex.lic.model;




public class AuthenticationRequest {
	
	
	private String usename;
	private String password;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsename() {
		return usename;
	}
	public void setUsename(String usename) {
		this.usename = usename;
	}
	public AuthenticationRequest(String password, String usename) {
		super();
		this.password = password;
		this.usename = usename;
	}
	public AuthenticationRequest() {
		
	}

}
