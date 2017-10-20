package com.noware.lazyjdbc.parameter;


import java.sql.CallableStatement;
import java.sql.SQLException;

public interface PlsqlParameter {

	public void setParameter(int parameterIndex, CallableStatement cstm) throws SQLException;

	public ItemContainer<?> getItemFromStatement(int parameterIndex,
			CallableStatement cstm) throws Exception;
}
