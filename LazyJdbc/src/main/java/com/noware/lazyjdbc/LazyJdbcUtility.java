package com.noware.lazyjdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class. 
 * 
 * @author https://www.linkedin.com/in/ivandipaola/
 *
 */
public class LazyJdbcUtility
{
    private static final Logger log = LogManager.getLogger(LazyJdbcUtility.class);

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
	 * @throws SQLException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static <T extends Arrayable> List<T> getListFromSqlArray(Array ar, Class<T> t) throws SQLException, InstantiationException, IllegalAccessException
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
	 * Insert a {@link java.sql.Array} to an {@link AbstractCollection} of T objects.
	 * 
	 * @param ar				Array to inject.
	 * @param t					Class type. Must implements {@link Arrayable}.
	 * @param outputCollection	{@link AbstractCollection} of T objects, must be not null.
	 * 							The array will be inject here.
	 * @throws SQLException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	protected static <T extends Arrayable> void getAbstractCollectionFromSqlArray(Array ar, Class<T> t, AbstractCollection<T> outputCollection) throws SQLException, InstantiationException, IllegalAccessException
	{
		log.debug("in getAbstractCollectionFromSqlArray ");
		T listElement = null;
		Object[] obj = (Object[]) ar.getArray();
		if (obj != null)
		{
			for (Object curr:obj)
			{
				if (curr != null)
				{
					listElement = t.newInstance();
					listElement.setObjectFromStruct((Struct) curr);
					outputCollection.add(listElement);
				}
			}
		}
		log.debug("out getAbstractCollectionFromSqlArray ");
	}
	
	/**
	 * Transforms a {@link java.sql.Array} to a multi level object.
	 * 
	 * @param ar			Array to inject.
	 * @param t				Class type. Must implements {@link Arrayable}.
	 * @return				Multi level object of type T.
	 * @throws SQLException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static <T extends Arrayable> T getMultiLevelObjectFromSqlArray(Array ar, Class<T> t) throws SQLException, InstantiationException, IllegalAccessException
	{
		log.debug("in getMultiLevelObjectFromSqlArray ");
		T multiLevelObject = null;
		Object[] obj = (Object[]) ar.getArray();
		if (obj != null)
		{
			multiLevelObject = t.newInstance();
			for (Object curr:obj)
			{
				if (curr != null)
				{
					multiLevelObject.setObjectFromStruct((Struct) curr);
				}
			}
		}
		log.debug("out getMultiLevelObjectFromSqlArray ");
		return multiLevelObject;
	}

	/**
	 * Create a query string for a plsql function.
	 * 
	 * @param functionName		Function name.
	 * @param queryParams		Query parameters.
	 * @return					{@link StringBuilder} query from given inputs.
	 * 
	 * @deprecated
	 */
	protected static StringBuilder getQueryFunction(String functionName, Object ... queryParams) {
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
		return sb;
	}

	/**
	 * Create a query string for a plsql function.
	 * 
	 * @param functionName		Function name.
	 * @param queryParams		Query parameters.
	 * @return					{@link StringBuilder} query from given inputs.
	 */
	protected static StringBuilder getQueryFunction(String functionName, List<Object> queryParams) {
		StringBuilder sb = new StringBuilder("{? = call " + functionName + "(");
		if (queryParams!=null && !queryParams.isEmpty())
		{
			for (int i=0; i<queryParams.size(); i++)
			{
				sb.append("?,");
			}
			if (queryParams.size()>0)
			{
				sb.deleteCharAt(sb.length()-1);
			}
		}
		sb.append(")}");
		return sb;
	}

	/**
	 * Create a query string for a plsql procedure.
	 * 
	 * @param functionName		Procedure name.
	 * @param queryParams		Query parameters.
	 * @return					{@link StringBuilder} query from given inputs.
	 * 
	 * @deprecated
	 */
	protected static <T> StringBuilder getQueryProcedure(String functionName, T ... queryParams) {
		return getQueryFunction(functionName, queryParams).delete(1, 5);
	}

	/**
	 * Create a query string for a plsql procedure.
	 * 
	 * @param functionName		Procedure name.
	 * @param queryParams		Query parameters.
	 * @return					{@link StringBuilder} query from given inputs.
	 */
	protected static <T> StringBuilder getQueryProcedure(String functionName, List<T> queryParams) {
		return getQueryFunction(functionName, queryParams).delete(1, 5);
	}
	
	protected static void traceErrorLog(Throwable e, String functionName, Object ... queryParams)
	{
		log.error("ERROR: " + e.getMessage());
		log.error(" plsql: " + functionName);			
		if (queryParams!=null)
		{
			log.error("queryParam: " + Arrays.toString(queryParams));
		}
	}
	
}