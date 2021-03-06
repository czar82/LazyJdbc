package com.noware.lazyjdbc.parameter;


import java.sql.CallableStatement;
import java.sql.SQLException;

public class OutPlsqlParameter implements PlsqlParameter {

	ItemContainer<?> item;
	
	public OutPlsqlParameter(ItemContainer<?> item) {
		super();
		this.item = item;
	}

	public void setParameter(int parameterIndex, CallableStatement cstm) throws SQLException {
		item.setOutParameter(parameterIndex, cstm);
	}
	
	public ItemContainer<?> getItemFromStatement(int parameterIndex, CallableStatement cstm) throws SQLException, InstantiationException, IllegalAccessException
	{
		return item.getObjectFromStatement(parameterIndex, cstm);
	}

	@Override
	public String toString() {
		return item!=null ? item.toString() : "";
	}

}
