package com.noware.lazyjdbc.parameter;


import java.sql.CallableStatement;
import java.sql.SQLException;

public class InPlsqlParameter <T> implements PlsqlParameter {

	ItemContainer<T> item;

	public InPlsqlParameter() {
		super();
	}

	
	public InPlsqlParameter(T item) {
		super();
		this.item = new SingleItem<T>(item);
	}
	
	public InPlsqlParameter(ItemContainer<T> item) {
		super();
		this.item = item;
	}

	public void setParameter(int parameterIndex, CallableStatement cstm) throws SQLException {
		cstm.setObject(parameterIndex, item!=null ? item.getObject() : null);
	}

	public ItemContainer<?> getItemFromStatement(int parameterIndex,
			CallableStatement cstm) {
		return null;
	}

	@Override
	public String toString() {
		return item!=null ? item.toString() : "";
	}

}
