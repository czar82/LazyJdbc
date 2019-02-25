package com.noware.lazyjdbc;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

/**
 * Entry class to call plsql function that returns a simple object or a cursor. 
 * 
 * @author https://www.linkedin.com/in/ivandipaola/
 *
 */
@Getter
@SuperBuilder
public class LazyFunction extends LazyJdbc {
	/**
	 * @param queryParams		Varargs with the input parameters for pl/sql (can be null if there are no parameters).
	 */
	@Singular
	List<Object> queryParams;
	
	/**
	 * Call a given pl/sql that returns a ref cursor and map it to a {@link ArrayList}
	 *  
	 * @param t					Class type element to be returned. Must implements {@link ResultSettable}.
	 * @throws Throwable
	 */
	public <T extends ResultSettable> List<T> genericFunction4refCursor(Class<T> t) throws Throwable {
		CallableStatement cstm = null;
		ResultSet rs = null;
		List<T> returnList = new ArrayList<T>();
		
		String query = LazyJdbcUtility.getQueryFunction(this.getFunctionName(), queryParams).toString();		
		boolean paramEmpty = this.getQueryParams()==null || this.getQueryParams().isEmpty();

		try
		{
			cstm = this.getConnection().prepareCall(query);
			int i=1;
			cstm.registerOutParameter(i++, OracleTypes.CURSOR);
			if (!paramEmpty)
			{
				for (Object ob:queryParams)
				{
					cstm.setObject(i++, ob);
				}
			}
			cstm.executeQuery();
			rs = (ResultSet) cstm.getObject(1);
			while (rs!=null && rs.next())
			{
				T curr = t.newInstance();
				curr.setObjectFromResulSet(rs);
				returnList.add(curr);
			}
		} catch (SQLException | InstantiationException | IllegalAccessException e)
		{
			LazyJdbcUtility.traceErrorLog(e, query, queryParams);
			throw e;

		} finally
		{
			if (cstm!=null)
			{
				if (rs!=null)
				{
					rs.close();
				}
				cstm.close();
			}
		}
		return returnList;
	}
	
	/**
	 * Call a given pl/sql and returns a generic object
	 *  
	 * @param t					Class type to be returned.
	 * @throws SQLException
	 */
	public <T> T genericFunction4Object(Class<T> t)  throws SQLException
	{
		CallableStatement cstm = null;

		String query = LazyJdbcUtility.getQueryFunction(this.getFunctionName(), queryParams).toString();		
		boolean paramEmpty = this.getQueryParams()==null || this.getQueryParams().isEmpty();

		try
		{
			cstm = this.getConnection().prepareCall(query);
			int i=1;
			cstm.registerOutParameter(i++, LazyJdbcUtility.getOracleTypes(t));
			if (!paramEmpty)
			{
				for (Object ob:queryParams)
				{
					cstm.setObject(i++, ob);
				}
			}
			cstm.executeQuery();
			return cstm.getObject(1, t);
		} catch (SQLException e)
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
