package com.noware.lazyjdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implements this interface to inject the {@link java.sql.ResultSet} in your class. 
 * 
 * @author https://www.linkedin.com/in/ivandipaola/
 *
 */
public interface ResultSettable {

	/**
	 * Set the fields from ResultSet to pojo.
	 * 
	 * @param rs			ResultSet to copy to destination.
	 * 						<br>Eg: this.personName = rs.getString("NAME");
	 * @throws Exception
	 */
	public abstract <T> void setObjectFromResulSet(ResultSet rs) throws SQLException;
	
}
