package com.noware.lazyjdbc.parameter;


import java.sql.CallableStatement;
import java.sql.SQLException;

public class OutPlsqlParameter implements PlsqlParameter {

	ItemContainer<?> item;
	
	public OutPlsqlParameter(ItemContainer<?> item) {
		super();
		this.item = item;
	}

	@Override
	public void setParameter(int parameterIndex, CallableStatement cstm) throws SQLException {
		item.setOutParameter(parameterIndex, cstm);
	}
	
	@Override
	public ItemContainer<?> getItemFromStatement(int parameterIndex, CallableStatement cstm) throws Exception
	{
		return item.getObjectFromStatement(parameterIndex, cstm);
	}

	@Override
	public String toString() {
		return item!=null ? item.toString() : "";
	}

}
