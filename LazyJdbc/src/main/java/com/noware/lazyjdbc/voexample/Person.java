package com.noware.lazyjdbc.voexample;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.noware.lazyjdbc.ResultSettable;

/**
 * Class used to store a typical boring person.
 * 
 * @author Ivan Di Paola
 *
 */
public class Person implements ResultSettable, Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private Date birthDate;
	private String telephone;
	
	
	public void setObjectFromResulSet(ResultSet rs) throws SQLException {
		//get the column from database:
		setId(rs.getInt("ID_PERSON"));
		setName(rs.getString("NAME"));
		setBirthDate( rs.getDate("BIRTHDATE") );
		setTelephone( rs.getString("TELEPHONE") );
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getTelephone() {
		return telephone;
	}


	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Date getBirthDate() {
		return birthDate;
	}


	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}


}
