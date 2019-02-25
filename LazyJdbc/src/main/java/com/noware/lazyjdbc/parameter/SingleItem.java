package com.noware.lazyjdbc.parameter;

import java.sql.CallableStatement;
import java.sql.SQLException;

import com.noware.lazyjdbc.LazyJdbcUtility;


public class SingleItem <T> extends ItemContainer<T> {

	private T item;

	public SingleItem(Class<T> inferedClass) {
		super();
		super.inferedClass = inferedClass;
	}
	
	public SingleItem(T item) {
		super();
		this.item = item;
	}

	@Override
	public int getOracleType() {
		return LazyJdbcUtility.getOracleTypes(getGenericClass());
	}

	@Override
	public T getObject() {
		return item;
	}

	@Override
	public void setOutParameter(int parameterIndex, CallableStatement cstm) throws SQLException {
		cstm.registerOutParameter(parameterIndex, this.getOracleType());
		
	}
	
	@Override
	public ItemContainer<T> getObjectFromStatement(int parameterIndex, CallableStatement cstm) throws SQLException {
//		item = (T)cstm.getObject(parameterIndex);
		//You can use this from JDK 1.7:
		item = cstm.getObject(parameterIndex, inferedClass);
		
		return this;
	}

	@Override
	public String toString() {
		return item!=null ? item.toString() : "";
	}

}
