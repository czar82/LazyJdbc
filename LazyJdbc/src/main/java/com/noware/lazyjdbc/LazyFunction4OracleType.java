package com.noware.lazyjdbc;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * Entry class to call plsql function that returns an Oracle Type. 
 * 
 * @author https://www.linkedin.com/in/ivandipaola/
 *
 */
@Getter
@SuperBuilder
public class LazyFunction4OracleType extends LazyFunction {
	/**
	 * Custom Oracle type {@link ARRAY} name to be mapped (eg: name_schema.name_custom_type).
	 */
	String oracleTypeName;
	
	/**
	 * Call a given pl/sql that returns an {@link ARRAY} and map it to a {@link ArrayList} 
	 * 
	 * @param t					Class type element to be returned. Must implements {@link Arrayable}.	
	 * @throws Exception
	 */
	public <T extends Arrayable> List<T> genericFunction4arrayType(Class<T> t)  throws Exception
	{
		CallableStatement cstm = null;
		List<T> returnList = new ArrayList<T>();
		
		String query = LazyJdbcUtility.getQueryFunction(this.getFunctionName(), queryParams).toString();		
		boolean paramEmpty = this.getQueryParams()==null || this.getQueryParams().isEmpty();

		try
		{
			cstm = this.getConnection().prepareCall(query);
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
		} catch (SQLException | InstantiationException | IllegalAccessException e)
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
	 * @param t					Class type element to be returned. Must implements {@link Arrayable}.
	 * @param outputCollection	Empty (not null!) output collection that will be filled with data from pl/sql.
	 * @return					{@link ArrayList} of T objects.
	 * @throws Exception
	 */
	public <T extends Arrayable> void genericFunction4abstractCollection(Class<T> t, AbstractCollection<T> outputCollection)  throws Exception
	{
		CallableStatement cstm = null;
		
		String query = LazyJdbcUtility.getQueryFunction(this.getFunctionName(), queryParams).toString();		
		boolean paramEmpty = this.getQueryParams()==null || this.getQueryParams().isEmpty();

		try
		{
			cstm = this.getConnection().prepareCall(query);
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
	 * @param t					Class type element to be returned. Must implements {@link Arrayable}.
	 * @return					An object T used to map {@link ARRAY} according to implementation of {@link Arrayable}.
	 * @throws Exception
	 */
	public <T extends Arrayable> T genericFunction4multiLevelObject(Class<T> t) throws Exception
	{
		CallableStatement cstm = null;
		T returnObject;
		
		String query = LazyJdbcUtility.getQueryFunction(this.getFunctionName(), queryParams).toString();		
		boolean paramEmpty = this.getQueryParams()==null || this.getQueryParams().isEmpty();

		try
		{
			cstm = this.getConnection().prepareCall(query);
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
		} catch (SQLException | InstantiationException | IllegalAccessException e)
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
	
}
