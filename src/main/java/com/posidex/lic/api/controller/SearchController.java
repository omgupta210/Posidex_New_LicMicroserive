package com.posidex.lic.api.controller;

import org.springframework.http.ResponseEntity;


import com.posidex.lic.model.CustRequestId;
import com.posidex.lic.model.PolicyNoRequest;
import com.posidex.lic.model.ServiceRequest;

public interface SearchController {

	public ResponseEntity<?> realTimeSearch(ServiceRequest request) throws Exception;

	public ResponseEntity<?> getToken() throws Exception;
	
	public ResponseEntity<?> CustomerIdSerch(CustRequestId custmerid) throws Exception;
	
	public ResponseEntity<?> PloicySearch(PolicyNoRequest policyno) throws Exception;
	
}
