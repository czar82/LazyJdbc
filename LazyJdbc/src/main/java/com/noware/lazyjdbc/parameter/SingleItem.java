package com.noware.lazyjdbc.parameter;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;

import com.noware.lazyjdbc.LazyJdbcDAO;


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
		return LazyJdbcDAO.getOracleTypes(getGenericClass());
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
	@SuppressWarnings("unchecked")
	public ItemContainer<T> getObjectFromStatement(int parameterIndex, CallableStatement cstm) throws SQLException {
		item = (T)cstm.getObject(parameterIndex);
		//You can use this from JDK 1.7:
		//cstm.getObject(1, t);
		
		return this;
	}

	@Override
	public List<T> getList() {
		return null;
	}

	@Override
	public String toString() {
		return item!=null ? item.toString() : "";
	}

}
