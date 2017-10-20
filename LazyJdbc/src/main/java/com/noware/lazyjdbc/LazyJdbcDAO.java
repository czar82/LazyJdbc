package com.noware.lazyjdbc;


import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import oracle.jdbc.driver.OracleTypes;
import oracle.sql.ARRAY;

import org.apache.log4j.Logger;

import com.noware.lazyjdbc.parameter.PlsqlParameter;

/**
 * Extends this abstract class to from your DAO. 
 * 
 * @author https://www.linkedin.com/in/ivandipaola/
 *
 */
public abstract class LazyJdbcDAO
{
	public static Logger log = Logger.getLogger("it.sian.appsianagea.lazyjdbc.LazyJdbcDAO: ");
	private static HashMap<Class<?>, Integer> oracleMap;
	static {
		//Only the main OracleTypes are mapped, add below
		//others OracleTypes that you need.
		oracleMap = new HashMap<Class<?>, Integer>();
		oracleMap.put(String.class, OracleTypes.VARCHAR);
		oracleMap.put(Integer.class, OracleTypes.INTEGER);
		oracleMap.put(Clob.class, OracleTypes.CLOB);
		oracleMap.put(Blob.class, OracleTypes.BLOB);
		oracleMap.put(Boolean.class, OracleTypes.BOOLEAN);
		oracleMap.put(Date.class, OracleTypes.DATE);
		oracleMap.put(Double.class, OracleTypes.DOUBLE);
		oracleMap.put(Float.class, OracleTypes.FLOAT);		
	}

	/**
	 * Returns the {@link OracleTypes} matching to the input {@link Class}
	 * 
	 * @param t						Class type.
	 * @return						An int corresponding to the {@link OracleTypes}.
	 * @throws RuntimeException		If the Class type is not mapped an exception is thrown.
	 */
	public static <T> int getOracleTypes(Class<T> t) throws RuntimeException
	{
		Integer value = oracleMap.get(t);
		if (value==null)
		{
			throw new RuntimeException("The class " + t + " is not mapped. Clone the project on github and map it.");
		}
		return value;
	}
	
	/**
	 * Transforms a {@link java.sql.Array} to an {@link ArrayList} of T objects.
	 * 
	 * @param ar			Array to inject.
	 * @param t				Class type. Must implements {@link Arrayable}.
	 * @return				{@link ArrayList} of T objects.
	 * @throws Exception
	 */
	public static <T extends Arrayable> List<T> getListFromSqlArray(Array ar, Class<T> t) throws Exception
	{
		log.debug("in getListFromSqlArray ");
		T listElement = null;
		List<T> list = null;
		Object[] obj = (Object[]) ar.getArray();
		if (obj != null)
		{
			list = new ArrayList<T>();
			for (Object curr:obj)
			{
				if (curr != null)
				{
					listElement = t.newInstance();
					listElement.setObjectFromStruct((Struct) curr);
					list.add(listElement);
				}
			}
		}
		log.debug("out getListFromSqlArray ");
		return list;
	}

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
	public <T extends ResultSettable> List<T> genericFunction4refCursor(Connection conn, String functionName, Class<T> t, Object ... queryParams)  throws Exception
	{
		CallableStatement cstm = null;
		ResultSet rs = null;
		List<T> returnList = new ArrayList<T>();
		
		boolean paramEmpty = queryParams.length==1 && queryParams[0]==null;
		StringBuilder sb = new StringBuilder("{? = call " + functionName + "(");
		if (!paramEmpty)
		{
			for (int i=0; i<queryParams.length; i++)
			{
				sb.append("?,");
			}
			if (queryParams.length>0)
			{
				sb.deleteCharAt(sb.length()-1);
			}
		}
		sb.append(")}");

		try
		{
			cstm = conn.prepareCall(sb.toString());
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
			if (queryParams!=null)
			{
				log.error("queryParam: " + queryParams.toString() + " pl:" + sb.toString());
			}
			throw e;

		} finally
		{
			rs.close();
			cstm.close();
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

		boolean paramEmpty = queryParams.length==1 && queryParams[0]==null;
		StringBuilder sb = new StringBuilder("{? = call " + functionName + "(");
		if (!paramEmpty)
		{
			for (int i=0; i<queryParams.length; i++)
			{
				sb.append("?,");
			}
			if (queryParams.length>0)
			{
				sb.deleteCharAt(sb.length()-1);
			}
		}
		sb.append(")}");

		try
		{
			cstm = conn.prepareCall(sb.toString());
			int i=1;
			cstm.registerOutParameter(i++, getOracleTypes(t));
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
			if (queryParams!=null)
			{
				log.error("queryParam: " + queryParams.toString() + " pl:" + sb.toString());
			}
			throw e;

		} finally
		{
			cstm.close();
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
		
		boolean paramEmpty = queryParams.length==1 && queryParams[0]==null;
		StringBuilder sb = new StringBuilder("{? = call " + functionName + "(");
		if (!paramEmpty)
		{
			for (int i=0; i<queryParams.length; i++)
			{
				sb.append("?,");
			}
			if (queryParams.length>0)
			{
				sb.deleteCharAt(sb.length()-1);
			}
		}
		sb.append(")}");

		try
		{
			cstm = conn.prepareCall(sb.toString());
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
			returnList = getListFromSqlArray(cstm.getArray(1), t);
		} catch (Exception e)
		{
			if (queryParams!=null)
			{
				log.error("queryParam: " + queryParams.toString() + " pl:" + sb.toString());
			}
			throw e;

		} finally
		{
			cstm.close();
		}
		return returnList;		
	}


	/**
	 * Call a given pl/sql procedure 
	 * 
	 * @param conn				Database jdbc connection.
	 * @param functionName		Pl/sql funcion name (eg: name_schema.name_package.name_function).
	 * @param oracleTypeName	Custom {@link ARRAY} name to be mapped (eg: name_schema.name_custom_type).
	 * @param t					Class type element to be returned. Must implements {@link Arrayable}.
	 * @param queryParams		Varargs with the input and output parameters  {@link PlsqlParameter} for pl/sql (can be null if there are no parameters).
	 * @throws Exception
	 */	
	public void genericProcedure(Connection conn, String functionName, PlsqlParameter ... queryParams)  throws Exception
	{
		CallableStatement cstm = null;
		
		boolean paramEmpty = queryParams.length==1 && queryParams[0]==null;
		StringBuilder sb = new StringBuilder("{call " + functionName + "(");
		if (!paramEmpty)
		{
			for (int i=0; i<queryParams.length; i++)
			{
				sb.append("?,");
			}
			if (queryParams.length>0)
			{
				sb.deleteCharAt(sb.length()-1);
			}
		}
		sb.append(")}");

		try
		{
			cstm = conn.prepareCall(sb.toString());
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
			if (queryParams!=null)
			{
				log.error("queryParam: " + Arrays.toString(queryParams) + " pl:" + sb.toString());
			}
			throw e;

		} finally
		{
			cstm.close();
		}
	}	
	
	
}
