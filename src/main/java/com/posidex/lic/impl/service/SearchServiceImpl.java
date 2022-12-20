package com.posidex.lic.impl.service;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.posidex.lic.api.service.MessageService;
import com.posidex.lic.api.service.SearchService;
import com.posidex.lic.entity.Psx_cluster_cross_ref_t;
import com.posidex.lic.entity.TokenRequest;
import com.posidex.lic.exception.CustomException;
import com.posidex.lic.model.CustomerRequest;
import com.posidex.lic.model.CustomerResponse;
import com.posidex.lic.model.ErrorCode;
import com.posidex.lic.model.LoginModel;
import com.posidex.lic.model.PSX_DG_BLK_TRG;
import com.posidex.lic.model.PolicyDetails;
import com.posidex.lic.model.PolicyResponseDetails;
import com.posidex.lic.model.ServiceRequest;
import com.posidex.lic.repository.impl.psx_cluster_cross_ref_t_repoimpl;
import com.posidex.lic.util.ResponseJson;

@Service
public class SearchServiceImpl implements SearchService {
	private final static Logger logger = LoggerFactory.getLogger(SearchService.class);

	

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private Environment env;
	@Autowired
	private MessageService messageService;

	@Autowired
	private psx_cluster_cross_ref_t_repoimpl psxrepo;

	

	@Override
	public ResponseJson<HttpStatus, ?> getDetails(ServiceRequest request)
			throws CustomException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		logger.info("entered into getDetails service");
		ResponseJson<HttpStatus, Object> responseJson = new ResponseJson<>();

		ResponseJson<HttpStatus, ?> res = sendDataToMatchingService(request);
//		responseJson.setData(processManager.getDetails());
//		responseJson.setMessage(messageService.getMessage("message.successful"));
		responseJson.setMessage(res.getMessage());
		responseJson.setData(res.getData());
		return responseJson;

	}

	@Override
	public ResponseJson<HttpStatus, ?> getToken()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ResponseJson<HttpStatus, Object> responseJson = new ResponseJson<>();
		TokenRequest reqEntity = new TokenRequest();
		reqEntity.setUserId(env.getProperty("TOKEN_USERNAME"));
		reqEntity.setPassword(env.getProperty("TOKEN_PWD"));
		reqEntity.setUserType("admin");
		reqEntity.setAuthType(null);
		reqEntity.setReLogin(true);
		reqEntity.setIsPlainPassword(true);

		HttpEntity<TokenRequest> httpEntity = new HttpEntity<TokenRequest>(reqEntity, headers);

		TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
		SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();

		BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(
				socketFactoryRegistry);

		logger.info("*** Calling closeable http client ***");
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf)
				.setConnectionManager(connectionManager).build();

		ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		RestTemplate restTemplate = new RestTemplate(requestFactory);

		LoginModel response = restTemplate.postForObject(env.getProperty("TOKEN_URL"), httpEntity, LoginModel.class);
		httpClient.getConnectionManager().shutdown();
		responseJson.setData(response.getToken());
		System.out.println(response.getToken());
		return responseJson;

	}

	public ResponseJson<HttpStatus, ?> sendDataToMatchingService(ServiceRequest request)
			throws CustomException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		logger.info("Calling matching service");
		HttpHeaders headers = new HttpHeaders();
		ResponseJson<HttpStatus, Object> responseJson = new ResponseJson<>();
		headers.set("Authorization", "Bearer " + getToken().getData()); // accessToken

		headers.setContentType(MediaType.APPLICATION_JSON);
		// String url =
		// "https://10.167.80.144:2626/requestPosting/submitMapForRestAPI/Rest API/4";
		CustomerRequest req = new CustomerRequest();
		ArrayList<PSX_DG_BLK_TRG> list = new ArrayList<>();
		PSX_DG_BLK_TRG dg = new PSX_DG_BLK_TRG();
		System.out.println("name" + request.getName());
		dg.setPolicyHolderName(request.getName());
		dg.setDateOfBirth(request.getDob());
		//logger.info(request.getPhoneNumber() + "ZXc");
		if (!(request.getPhoneNumber() == null)) {
			if (!(request.getPhoneNumber().isEmpty())) {
				dg.setPhone1(request.getPhoneNumber());
			}
		}
		if (!(request.getAddress() == null)) {
			if (!(request.getAddress().isEmpty())) {
				dg.setAddress1(request.getAddress());
			}
		}
		if (!(request.getPincode() == null)) {
			if (!(request.getPincode().isEmpty())) {
				dg.setPincode1(request.getPincode());
			}
		}
		if (!(request.getEmail() == null)) {
			if (!(request.getEmail().isEmpty())) {
				dg.setEmailID1(request.getEmail());
			}
		}
		if (!(request.getPan() == null)) {
			if (!(request.getPan().isEmpty())) {
				dg.setPan(request.getPan());
			}
		}
		if (!(request.getGender() == null)) {
			if (!(request.getGender().isEmpty())) {
				dg.setGender(request.getGender());
			}
		}
		if (!(request.getBankAccountNo() == null)) {
			if (!(request.getBankAccountNo().isEmpty())) {
				dg.setVoterID(request.getBankAccountNo());
			}
		}

//		if (request.getPan() != null || !(request.getPan().isEmpty())) {
//			dg.setPan(request.getPan());
//		}

		list.add(dg);

		req.setPSX_DG_BLK_TRG(list);
		Gson gson = new Gson();
		String entityJsonString = gson.toJson(req);

		HttpEntity<String> requestEntity = new HttpEntity<>(entityJsonString, headers);
		TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
		SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();

		BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(
				socketFactoryRegistry);

		logger.info("*** Calling closeable http client ***");
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf)
				.setConnectionManager(connectionManager).build();

		ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		RestTemplate restTemplate = new RestTemplate(requestFactory);

		// logger.info("Sending request body to matching service: " + requestEntity);

		logger.info("Sending request body as json to matching service: " + entityJsonString);

		CustomerResponse responseEntity = restTemplate.postForObject(env.getProperty("MATCHING_SERVICE_URL"),
				requestEntity, CustomerResponse.class);

		String resJsonString = gson.toJson(responseEntity);
		logger.info("Getting response body as json  from matching service: " + resJsonString);
		// logger.info("Message from
		// service"+messageService.getMessage("REQUEST.ERROR"));
		if (responseEntity.getStatusMessage().equals("1055")||(responseEntity.getStatusMessage().equals("105"))) {
			logger.info("Message Code from matching service:" + responseEntity.getStatusMessage());
			throw new CustomException(messageService.getMessage("message.validation"));
		} else if (responseEntity.getStatusMessage().equals("1030")) {
			logger.info("Message Code from matching service:" + responseEntity.getStatusMessage());
			throw new CustomException(messageService.getMessage("REQUEST_ERROR"));
		} else if (responseEntity.getStatusMessage().equals("1010") || responseEntity.getStatusMessage().equals("1015")
				|| responseEntity.getStatusMessage().equals("1021") || responseEntity.getStatusMessage().equals("1035")
				|| responseEntity.getStatusMessage().equals("1045")
				|| responseEntity.getStatusMessage().equals("1050")) {
			logger.info("Message Code from matching service:" + responseEntity.getStatusMessage());
			throw new CustomException(messageService.getMessage("message.error"));
		}

		// String resJson = gson.toJson(resJsonString);
		ArrayList<List<String>> result = new ArrayList<>();

		ArrayList<String> apiHeaders = new ArrayList<>();
		ArrayList<List<String>> apiResults = new ArrayList<>();
		int[] values = { 0, 21, 22, 31, 33, 48, 53, 69, 77, 85 };
		for (int val : values) {
			apiHeaders.add(responseEntity.getApiHeader().get(val));

		}
		apiHeaders.add("CustomerId");
		logger.info("matchCount" + responseEntity.getRetValue().getMatchCount());
		if(responseEntity.getRetValue().getMatchCount() ==0) {
			logger.info("Message Code from matching service:" + responseEntity.getStatusMessage());
			throw new CustomException(messageService.getMessage("message.Invalid"));
		}
		for (int i = 1; i < responseEntity.getApiResults().size(); i++) {
			ArrayList<String> apiResultList = new ArrayList<>();
			StringBuffer sb = new StringBuffer();
			for (int j : values) {
				apiResultList.add(responseEntity.getApiResults().get(i).get(j));

			}
			String s = "SELECT CUSTOMER_ID FROM `psx_cluster_cross_ref_t`  WHERE cust_unq_id IN ('"
					+ responseEntity.getApiResults().get(i).get(0) + "')";
			System.out.println(s);
			List<Object> CusId = jdbcTemplate.query(s, (rs, rowNum) -> new String(rs.getString("CUSTOMER_ID")));
			String customerId = CusId.toString().replace("[", "");
			apiResultList.add(customerId.replace("]", ""));

			apiResults.add(apiResultList);
		}
		result.add(apiHeaders);
		result.addAll(apiResults);
		List<Map<String, String>> finalList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < responseEntity.getRetValue().getMatchCount(); i++) {
			Map<String, String> hash1 = new HashMap<String, String>();

			logger.info("apiResults Size" + apiResults.get(i).size());
//			for(int j = 0;j<apiResults.get(i).size();j++) {
//				ServiceResponse res = new ServiceResponse();
			hash1.put("Policy Number", apiResults.get(i).get(0));
			hash1.put("Date of Birth", apiResults.get(i).get(1));
			hash1.put("Name", apiResults.get(i).get(2));
			hash1.put("Pan", apiResults.get(i).get(3));
			hash1.put("Bank Account No", apiResults.get(i).get(4));
			hash1.put("Gender", apiResults.get(i).get(5));
			hash1.put("Address", apiResults.get(i).get(6));
			hash1.put("Pincode", apiResults.get(i).get(7));
			hash1.put("Phone Number", apiResults.get(i).get(8));
			hash1.put("Email Id", apiResults.get(i).get(9));

			hash1.put("Customer Id", apiResults.get(i).get(10));

			finalList.add(hash1);
		}

		responseJson.setMessage(responseEntity.getStatusMessage());
		responseJson.setData(finalList);
		return responseJson;
	}

	public ResponseJson<HttpStatus, ?> getcustomerDetails(String custid) throws CustomException, Exception{
		ResponseJson<HttpStatus, ?> responseJson = new ResponseJson<>();

		try
		{
		List<Psx_cluster_cross_ref_t> reqponserefdatat = psxrepo.findByCustomerid(custid);
		
		if(reqponserefdatat.isEmpty()||reqponserefdatat==null )
		{
			throw new CustomException(messageService.getMessage(ErrorCode.RNF.getMessageKey()));
		}

		responseJson.setData1(reqponserefdatat);
		responseJson.setMessage("message.successful");
		responseJson.setStatus(HttpStatus.OK);
		responseJson.setStatusflag(true);
		}
		catch(CustomException ce)
		{
			responseJson.setMessage(ce.getMessage());
			responseJson.setData1(null);
			responseJson.setStatusflag(false);

		}
		catch(Exception ce)
		{
			responseJson.setMessage(ce.getMessage());
			
			responseJson.setData1(null);
			responseJson.setStatusflag(false);
			
		}
		return responseJson;
	}



	public ResponseJson<HttpStatus, ?> getcustomerdetailswithpno(String pno)throws CustomException,NullPointerException,Exception{
		ResponseJson<HttpStatus, ?> responseJson = new ResponseJson<>();
		try {
		Psx_cluster_cross_ref_t p = psxrepo.findByCustunqid(pno);
		
		logger.info("record of psx_cluster_table with policy no: " + psxrepo.findByCustunqid(pno));

		if(p==null)
		{
			throw new CustomException(messageService.getMessage(ErrorCode.RNF.getMessageKey()));
		}
		// Psx_cluster_cross_ref_t p1=(Psx_cluster_cross_ref_t)p.get();
		
		responseJson.setData1(p);
		responseJson.setMessage("psx cluster details for the given pno");
		responseJson.setStatus(HttpStatus.OK);
		responseJson.setStatusflag(true);
		
		}
		catch(CustomException ce)
		{
			responseJson.setMessage(ce.getMessage());
			responseJson.setData1((Psx_cluster_cross_ref_t)psxrepo.findByCustunqid(pno));
			
			responseJson.setStatusflag(false);
		}
		catch(Exception ce)
		{
			responseJson.setMessage(ce.getMessage());
			
			responseJson.setData1((Psx_cluster_cross_ref_t)psxrepo.findByCustunqid(pno));
			
			responseJson.setStatusflag(false);
			
		}
		return responseJson;
	}

	@Override
	public ResponseJson<HttpStatus, ?> getPolicydetails(String partionNo,String pno) throws CustomException,NullPointerException, Exception {
		ResponseJson<HttpStatus,?> res=new ResponseJson<>();
		String q="select NAME,DOB_DOI,RELATION_NAME,PARTITION_NUMBER,CUST_UNQ_ID,gender,Address1,pincode1,phone1,Email_id1,pan,Voter_id,passport from "+partionNo+"_psx_dg_blk_trg where CUST_UNQ_ID='"+pno+"'";
		
		logger.info("query details to fetch details  from _psx_dg_blk_trg " +q);
		try {
				/*PolicyDetails pld=jdbcTemplate.queryForObject(q, (rs, rowNum) -> new PolicyDetails(
					rs.getString("NAME"),rs.getString("DOB_DOI"),rs.getString("RELATION_NAME"),rs.getString("PARTITION_NUMBER"),rs.getString("CUST_UNQ_ID")) );*/
			  
				
		PolicyResponseDetails polres=jdbcTemplate.queryForObject(q, (rs,num)-> new PolicyResponseDetails(rs.getString("NAME"),rs.getString("DOB_DOI"),rs.getString("CUST_UNQ_ID"),
						 rs.getString("gender"),rs.getString("Address1"),rs.getString("pincode1"),rs.getString("phone1"),rs.getString("Email_id1"),
						 rs.getString("pan"),rs.getString("Voter_id"),rs.getString("passport")));
			logger.info("policyresposnedetails :"+polres);
			
		String q1="SELECT plan_cd,policy_term,premium_paying_term,sum_assured,commencement_dt,premium_mode_desc,stus_cd,medical_flag,inst_premium,fup_dt FROM  psx_lic_additionl_parameters where policy_no ='"+pno+"'";
		
		logger.info("query details to fetch details  from psx_lic_additionl_parameters: "+q1);
		PolicyResponseDetails polres_t2=jdbcTemplate.queryForObject(q1, (rs,num)-> new PolicyResponseDetails(rs.getString("plan_cd"),rs.getString("policy_term"),rs.getString("premium_paying_term"),
				 rs.getString("sum_assured"),rs.getString("commencement_dt"),rs.getString("premium_mode_desc"),rs.getString("stus_cd"),rs.getString("medical_flag"),
				 rs.getString("inst_premium"),rs.getString("fup_dt")));
				
		PolicyResponseDetails final_polres=new PolicyResponseDetails(polres_t2.getPlan_cd(),polres_t2.getPolicy_term(),polres_t2.getPremium_paying_term(),polres_t2.getSum_assured(),
				polres_t2.getCommencement_dt(),polres_t2.getPremium_mode_desc(),polres_t2.getStus_cd(),polres_t2.getMedical_flag(),polres_t2.getInst_premium(),polres_t2.getFup_dt(),
				polres.getName(),polres.getDob(),polres.getPolicy_no(),polres.getGender(),polres.getAddress(),polres.getPincode(),polres.getMobile(),polres.getEmail(),
				polres.getPan_no(),polres.getBank_acc_no(),polres.getAgency_code());
		
		
		       res.setData1(final_polres);
			   res.setMessage("successful");
			   res.setStatus(HttpStatus.OK);
			   res.setStatusflag(true);
	     	}
		catch(Exception e)
		{
			   res.setData1(null);
			   res.setMessage(e.getMessage());
			   res.setStatus(HttpStatus.PRECONDITION_FAILED);
			   res.setStatusflag(false);
		}
		
          
		return res;
	}

}
