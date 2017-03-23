package com.noware.lazyjdbc;

import java.sql.Struct;

/**
 * Implements this interface to inject the {@link java.sql.Array} in your class. 
 * 
 * @author https://www.linkedin.com/in/ivandipaola/
 *
 */
public interface Arrayable {

	/**
	 * Set the fields from Struct to pojo.
	 * 
	 * @param st			Struct containg objects' array to copy to the destination.
	 * 						<br>Eg: Object[] ob = st.getAttributes();
	 * @throws Exception
	 */
	public abstract void setObjectFromStruct(Struct st) throws Exception;

}
