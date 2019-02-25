package com.noware.lazyjdbc;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;

import com.noware.lazyjdbc.parameter.PlsqlParameter;

import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

/**
 * Entry class to call plsql procedure. 
 * 
 * @author https://www.linkedin.com/in/ivandipaola/
 *
 */
@Getter
@SuperBuilder
public class LazyProcedure extends LazyJdbc {
	/**
	 * List with the input and output parameters  {@link PlsqlParameter} for pl/sql (can be null if there are no parameters).
	 */
	@Singular
	List<PlsqlParameter> queryParams;
	
	/**
	 * Call a given pl/sql procedure 
	 * @throws Exception 
	 * 
	 * @throws Throwable 
	 */	
	public void genericProcedure() throws Exception
	{
		CallableStatement cstm = null;
		
		String query = LazyJdbcUtility.getQueryProcedure(this.getFunctionName(), queryParams).toString();		
		boolean paramEmpty = this.getQueryParams()==null || this.getQueryParams().isEmpty();

		try
		{
			cstm = this.getConnection().prepareCall(query);
			int i=1;
			if (!paramEmpty)
			{
				for (PlsqlParameter ob:queryParams)
				{
					ob.setParameter(i++, cstm);
				}
			}
			
			cstm.execute();
			if (!paramEmpty)
			{
				i=1;
				for (PlsqlParameter ob:queryParams)
				{
					ob.getItemFromStatement(i++, cstm);
				}
			}
		} 
		catch (SQLException | InstantiationException | IllegalAccessException e)
		{
			LazyJdbcUtility.traceErrorLog(e, query, queryParams);
			throw e;

		} finally
		{
			if (cstm!=null)
			{
				cstm.close();
			}
		}
	}
	
}
