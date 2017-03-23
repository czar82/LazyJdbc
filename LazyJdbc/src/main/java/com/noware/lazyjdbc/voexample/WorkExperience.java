package com.noware.lazyjdbc.voexample;

import java.io.Serializable;
import java.sql.Struct;
import java.util.Date;

import com.noware.lazyjdbc.Arrayable;

/**
 * Class used to store a work experience.
 * 
 * @author Ivan Di Paola
 *
 */
public class WorkExperience implements Serializable, Arrayable {

	private static final long serialVersionUID = 1L;
	private Date from;
	private Date to;
	private String company;
	private String role;
	
	public void setObjectFromStruct(Struct st) throws Exception {
		//The fields in the array are returned in the same order of the
		//PL/SQL function
		Object[] ob = st.getAttributes();
		int i=0;
		this.setFrom((Date)ob[i++]);
		this.setTo((Date)ob[i++]);
		this.setCompany((String)ob[i++]);
		this.setRole((String)ob[i++]);
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	

}
