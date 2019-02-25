package com.noware.lazyjdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.noware.lazyjdbc.parameter.PlsqlParameter;

/**
 * Entry class to call plsql function and procedure from java. 
 * 
 * @author https://www.linkedin.com/in/ivandipaola/
 *
 * @deprecated Use subclass of {@link LazyJdbc} to build the function parameters and call it.
 * 
 */
public class LazyJdbcDAO
{
	public static Logger log = Logger.getLogger("LazyJdbcDAO: ");
	/**
	 * Call a given pl/sql that returns a ref cursor and map it to a {@link ArrayList}
	 *  
	 * @param conn				Database jdbc connection.
	 * @param functionName		Pl/sql funcion name (eg: name_schema.name_package.name_function).
	 * @param t					Class type element to be returned. Must implements {@link ResultSettable}.
	 * @param queryParams		Varargs with the input parameters for pl/sql (can be null if there are no parameters).
	 * @return					{@link ArrayList} of T objects.
	 * @throws Exception
	 */
	public static <T extends ResultSettable> List<T> genericFunction4refCursor(Connection conn, String functionName, Class<T> t, Object ... queryParams)  throws Exception
	{
		CallableStatement cstm = null;
		ResultSet rs = null;
		List<T> returnList = new ArrayList<T>();
		
		String query = LazyJdbcUtility.getQueryFunction(functionName, queryParams).toString();		
		boolean paramEmpty = queryParams.length==1 && queryParams[0]==null;

		try
		{
			cstm = conn.prepareCall(query);
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
		} catch (Exception e)
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
	 * @param conn				Database jdbc connection.
	 * @param functionName		Pl/sql funcion name (eg: name_schema.name_package.name_function).
	 * @param t					Class type to be returned.
	 * @param queryParams		Varargs with the input parameters for pl/sql (can be null if there are no parameters).
	 * @return					An object of the given class type.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T> T genericFunction4Object(Connection conn, String functionName, Class<T> t, Object ... queryParams)  throws Exception
	{
		CallableStatement cstm = null;

		String query = LazyJdbcUtility.getQueryFunction(functionName, queryParams).toString();		
		boolean paramEmpty = queryParams.length==1 && queryParams[0]==null;

		try
		{
			cstm = conn.prepareCall(query);
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
			return (T)cstm.getObject(1);
			
			//You can use this from JDK 1.7:
//			return cstm.getObject(1, t);
		} catch (Exception e)
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

	/**
	 * Call a given pl/sql that returns an {@link ARRAY} and map it to a {@link ArrayList} 
	 * 
	 * @param conn				Database jdbc connection.
	 * @param functionName		Pl/sql funcion name (eg: name_schema.name_package.name_function).
	 * @param oracleTypeName	Custom {@link ARRAY} name to be mapped (eg: name_schema.name_custom_type).
	 * @param t					Class type element to be returned. Must implements {@link Arrayable}.
	 * @param queryParams		Varargs with the input parameters for pl/sql (can be null if there are no parameters).
	 * @return					{@link ArrayList} of T objects.
	 * @throws Exception
	 */
	public <T extends Arrayable> List<T> genericFunction4arrayType(Connection conn, String functionName, String oracleTypeName, Class<T> t, Object ... queryParams)  throws Exception
	{
		CallableStatement cstm = null;
		List<T> returnList = new ArrayList<T>();
		
		String query = LazyJdbcUtility.getQueryFunction(functionName, queryParams).toString();		
		boolean paramEmpty = queryParams.length==1 && queryParams[0]==null;

		try
		{
			cstm = conn.prepareCall(query);
			int i=1;
			cstm.registerOutParameter(i++, OracleTypes.ARRAY, oracleTypeName);
			if (!paramEmpty)
			{
				for (Object ob:queryParams)
				{
					cstm.setObject(i++, ob);
				}
			}
			cstm.execute();
			returnList = LazyJdbcUtility.getListFromSqlArray(cstm.getArray(1), t);
		} catch (Exception e)
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
		return returnList;		
	}


	/**
	 * Call a given pl/sql that returns an {@link ARRAY} and map it to a {@link ArrayList} 
	 * 
	 * @param conn				Database jdbc connection.
	 * @param functionName		Pl/sql funcion name (eg: name_schema.name_package.name_function).
	 * @param oracleTypeName	Custom {@link ARRAY} name to be mapped (eg: name_schema.name_custom_type).
	 * @param t					Class type element to be returned. Must implements {@link Arrayable}.
	 * @param outputCollection	Empty (not null!) output collection that will be filled with data from pl/sql.
	 * @param queryParams		Varargs with the input parameters for pl/sql (can be null if there are no parameters).
	 * @return					{@link ArrayList} of T objects.
	 * @throws Exception
	 */
	public <T extends Arrayable> void genericFunction4abstractCollection(Connection conn, String functionName, String oracleTypeName, Class<T> t, AbstractCollection<T> outputCollection, Object ... queryParams)  throws Exception
	{
		CallableStatement cstm = null;
		
		String query = LazyJdbcUtility.getQueryFunction(functionName, queryParams).toString();		
		boolean paramEmpty = queryParams.length==1 && queryParams[0]==null;

		try
		{
			cstm = conn.prepareCall(query);
			int i=1;
			cstm.registerOutParameter(i++, OracleTypes.ARRAY, oracleTypeName);
			if (!paramEmpty)
			{
				for (Object ob:queryParams)
				{
					cstm.setObject(i++, ob);
				}
			}
			cstm.execute();
			LazyJdbcUtility.getAbstractCollectionFromSqlArray(cstm.getArray(1), t, outputCollection);
		} catch (Exception e)
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
	
	/**
	 * Call a given pl/sql that returns an {@link ARRAY} and map it to a multi level object
	 * (eg.: a T object with inside an {@link HashMap} with an inner  {@link ArrayList}).
	 * The logic mapping is specified implementing {@link Arrayable}.
	 * 
	 * @param conn				Database jdbc connection.
	 * @param functionName		Pl/sql funcion name (eg: name_schema.name_package.name_function).
	 * @param oracleTypeName	Custom {@link ARRAY} name to be mapped (eg: name_schema.name_custom_type).
	 * @param t					Class type element to be returned. Must implements {@link Arrayable}.
	 * @param queryParams		Varargs with the input parameters for pl/sql (can be null if there are no parameters).
	 * @return					An object T used to map {@link ARRAY} according to implementation of {@link Arrayable}.
	 * @throws Exception
	 */
	public <T extends Arrayable> T genericFunction4multiLevelObject(Connection conn, String functionName, String oracleTypeName, Class<T> t, Object ... queryParams)  throws Exception
	{
		CallableStatement cstm = null;
		T returnObject;
		
		String query = LazyJdbcUtility.getQueryFunction(functionName, queryParams).toString();		
		boolean paramEmpty = queryParams.length==1 && queryParams[0]==null;

		try
		{
			cstm = conn.prepareCall(query);
			int i=1;
			cstm.registerOutParameter(i++, OracleTypes.ARRAY, oracleTypeName);
			if (!paramEmpty)
			{
				for (Object ob:queryParams)
				{
					cstm.setObject(i++, ob);
				}
			}
			cstm.execute();
			returnObject = LazyJdbcUtility.getMultiLevelObjectFromSqlArray(cstm.getArray(1), t);
		} catch (Exception e)
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
		return returnObject;		
	}


	/**
	 * Call a given pl/sql procedure 
	 * 
	 * @param conn				Database jdbc connection.
	 * @param functionName		Pl/sql funcion name (eg: name_schema.name_package.name_function).
	 * @param oracleTypeName	Custom {@link ARRAY} name to be mapped (eg: name_schema.name_custom_type).
	 * @param t					Class type element to be returned. Must implements {@link Arrayable}.
	 * @param queryParams		Varargs with the input and output parameters  {@link PlsqlParameter} for pl/sql (can be null if there are no parameters).
	 * 							Only object are allowed as input parameter (no lists for now). 
	 * @throws Exception
	 */	
	public void genericProcedure(Connection conn, String functionName, PlsqlParameter ... queryParams)  throws Exception
	{
		CallableStatement cstm = null;
		
		String query = LazyJdbcUtility.getQueryProcedure(functionName, queryParams).toString();		
		boolean paramEmpty = queryParams.length==1 && queryParams[0]==null;

		try
		{
			cstm = conn.prepareCall(query);
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
		catch (Exception e)
		{
			LazyJdbcUtility.traceErrorLog(e, query, (Object[])queryParams);
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