package com.oracle.bugcamp.model;

import java.util.Date;

public class Bug {

	private Long rptno;
	private Date rptdate;
	private String programmer;
	private Long cs_priority;
	private Long status;
	private String confirm_flag;
	private Long product_id;
	private String category;
	private String utility_version;
	private String subject;

	public Long getRptno() {
		return rptno;
	}

	public void setRptno(Long rptno) {
		this.rptno = rptno;
	}

	public Date getRptdate() {
		return rptdate;
	}

	public void setRptdate(Date rptdate) {
		this.rptdate = rptdate;
	}

	public String getProgrammer() {
		return programmer;
	}

	public void setProgrammer(String programmer) {
		this.programmer = programmer;
	}

	public Long getCs_priority() {
		return cs_priority;
	}

	public void setCs_priority(Long cs_priority) {
		this.cs_priority = cs_priority;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getConfirm_flag() {
		return confirm_flag;
	}

	public void setConfirm_flag(String confirm_flag) {
		this.confirm_flag = confirm_flag;
	}

	public Long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Long product_id) {
		this.product_id = product_id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUtility_version() {
		return utility_version;
	}

	public void setUtility_version(String utility_version) {
		this.utility_version = utility_version;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

}
