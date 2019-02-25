package com.noware.lazyjdbc.parameter;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.noware.lazyjdbc.Arrayable;
import com.noware.lazyjdbc.LazyJdbcUtility;
import com.noware.lazyjdbc.OracleTypes;


public class ListItem <T extends Arrayable> extends ItemContainer<T> {

	private List<T> listItem;
	private String oracleTypeName;

	public ListItem(Class<T> inferedClass, String oracleTypeName) {
		super();
		super.inferedClass = inferedClass;
		this.oracleTypeName = oracleTypeName;
	}

	public ListItem(List<T> listItem, String oracleTypeName) {
		super();
		this.listItem = listItem;
		this.oracleTypeName = oracleTypeName;
	}

	@Override
	public int getOracleType() {
		return OracleTypes.ARRAY;
	}

	@Override
	public T getObject() {
		//TODO: An OracleTypes.ARRAY created by listItem should be returned.
		return null;
	}

	public List<T> getList() {
		return listItem;
	}

	@Override
	public void setOutParameter(int parameterIndex, CallableStatement cstm) throws SQLException {
		cstm.registerOutParameter(parameterIndex, getOracleType(), oracleTypeName);
		
	}

	@Override
	public ItemContainer<T> getObjectFromStatement(int parameterIndex,
			CallableStatement cstm) throws SQLException, InstantiationException, IllegalAccessException {
		listItem = LazyJdbcUtility.getListFromSqlArray(cstm.getArray(parameterIndex), getGenericClass());
		return this;
	}

	@Override
	public String toString() {
		return listItem!=null ? Arrays.toString(listItem.toArray()) : "";
	}
	
}
