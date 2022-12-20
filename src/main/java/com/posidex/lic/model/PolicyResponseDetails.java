package com.posidex.lic.model;

public class PolicyResponseDetails {
	
	private String customer_id;
	private String policy_no;
	private String plan_cd;
	private String policy_term;
	private String premium_paying_term;
	private String sum_assured;
	private String commencement_dt;
	private String premium_mode_desc;
	private String stus_cd;
	private String medical_flag;
	private String inst_premium;
	private String fup_dt;
	private String name;
	private String gender;
	private String address;
	private String pincode;
	private String dob;
	private String mobile;
	private String email;
	private String Pan_no;
	private String bank_acc_no;
	private String agency_code;
	
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getPolicy_no() {
		return policy_no;
	}
	public void setPolicy_no(String policy_no) {
		this.policy_no = policy_no;
	}
	public String getPlan_cd() {
		return plan_cd;
	}
	public void setPlan_cd(String plan_cd) {
		this.plan_cd = plan_cd;
	}
	public String getPolicy_term() {
		return policy_term;
	}
	public void setPolicy_term(String policy_term) {
		this.policy_term = policy_term;
	}
	public String getPremium_paying_term() {
		return premium_paying_term;
	}
	public void setPremium_paying_term(String premium_paying_term) {
		this.premium_paying_term = premium_paying_term;
	}
	public String getSum_assured() {
		return sum_assured;
	}
	public void setSum_assured(String sum_assured) {
		this.sum_assured = sum_assured;
	}
	public String getCommencement_dt() {
		return commencement_dt;
	}
	public void setCommencement_dt(String commencement_dt) {
		this.commencement_dt = commencement_dt;
	}
	public String getPremium_mode_desc() {
		return premium_mode_desc;
	}
	public void setPremium_mode_desc(String premium_mode_desc) {
		this.premium_mode_desc = premium_mode_desc;
	}
	public String getStus_cd() {
		return stus_cd;
	}
	public void setStus_cd(String stus_cd) {
		this.stus_cd = stus_cd;
	}
	public String getMedical_flag() {
		return medical_flag;
	}
	public void setMedical_flag(String medical_flag) {
		this.medical_flag = medical_flag;
	}
	public String getInst_premium() {
		return inst_premium;
	}
	public void setInst_premium(String inst_premium) {
		this.inst_premium = inst_premium;
	}
	public String getFup_dt() {
		return fup_dt;
	}
	public void setFup_dt(String fup_dt) {
		this.fup_dt = fup_dt;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPan_no() {
		return Pan_no;
	}
	public void setPan_no(String pan_no) {
		Pan_no = pan_no;
	}
	public String getBank_acc_no() {
		return bank_acc_no;
	}
	public void setBank_acc_no(String bank_acc_no) {
		this.bank_acc_no = bank_acc_no;
	}
	public String getAgency_code() {
		return agency_code;
	}
	public void setAgency_code(String agency_code) {
		this.agency_code = agency_code;
	}
	
	@Override
	public String toString() {
		return "PolicyResponseDetails [customer_id=" + customer_id + ", policy_no=" + policy_no + ", plan_cd=" + plan_cd
				+ ", policy_term=" + policy_term + ", premium_paying_term=" + premium_paying_term + ", sum_assured="
				+ sum_assured + ", commencement_dt=" + commencement_dt + ", premium_mode_desc=" + premium_mode_desc
				+ ", stus_cd=" + stus_cd + ", medical_flag=" + medical_flag + ", inst_premium=" + inst_premium
				+ ", fup_dt=" + fup_dt + ", name=" + name + ", gender=" + gender + ", address=" + address + ", pincode="
				+ pincode + ", dob=" + dob + ", mobile=" + mobile + ", email=" + email + ", Pan_no=" + Pan_no
				+ ", bank_acc_no=" + bank_acc_no + ", agency_code=" + agency_code + "]";
	}
	
	public PolicyResponseDetails(String plan_cd, String policy_term, String premium_paying_term, String sum_assured,
			String commencement_dt, String premium_mode_desc, String stus_cd, String medical_flag, String inst_premium,
			String fup_dt) {
		this.plan_cd = plan_cd;
		this.policy_term = policy_term;
		this.premium_paying_term = premium_paying_term;
		this.sum_assured = sum_assured;
		this.commencement_dt = commencement_dt;
		this.premium_mode_desc = premium_mode_desc;
		this.stus_cd = stus_cd;
		this.medical_flag = medical_flag;
		this.inst_premium = inst_premium;
		this.fup_dt = fup_dt;
	}
	public PolicyResponseDetails( String name,String dob,String policy_no, String gender, String address, String pincode
			, String mobile, String email, String pan_no, String bank_acc_no, String agency_code) {
		
		this.policy_no = policy_no;
		this.name = name;
		this.gender = gender;
		this.address = address;
		this.pincode = pincode;
		this.dob = dob;
		this.mobile = mobile;
		this.email = email;
		Pan_no = pan_no;
		this.bank_acc_no = bank_acc_no;
		this.agency_code = agency_code;
	}
	public PolicyResponseDetails(String plan_cd, String policy_term, String premium_paying_term, String sum_assured,
			String commencement_dt, String premium_mode_desc, String stus_cd, String medical_flag, String inst_premium,
			String fup_dt,String name,String dob,String policy_no, String gender, String address, String pincode
			, String mobile, String email, String pan_no, String bank_acc_no, String agency_code) {
		this.policy_no = policy_no;
		this.plan_cd = plan_cd;
		this.policy_term = policy_term;
		this.premium_paying_term = premium_paying_term;
		this.sum_assured = sum_assured;
		this.commencement_dt = commencement_dt;
		this.premium_mode_desc = premium_mode_desc;
		this.stus_cd = stus_cd;
		this.medical_flag = medical_flag;
		this.inst_premium = inst_premium;
		this.fup_dt = fup_dt;
		this.name = name;
		this.gender = gender;
		this.address = address;
		this.pincode = pincode;
		this.dob = dob;
		this.mobile = mobile;
		this.email = email;
		Pan_no = pan_no;
		this.bank_acc_no = bank_acc_no;
		this.agency_code = agency_code;
	}
	public PolicyResponseDetails() {
		
	}

	
	

}
