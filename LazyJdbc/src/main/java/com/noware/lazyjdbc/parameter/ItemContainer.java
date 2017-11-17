package com.noware.lazyjdbc.parameter;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class ItemContainer <T> {

    protected Class<T> inferedClass;

	public abstract int getOracleType();

	public abstract T getObject();

	public abstract void setOutParameter(int parameterIndex, CallableStatement cstm) throws SQLException;

	public abstract ItemContainer<T> getObjectFromStatement(int parameterIndex,
			CallableStatement cstm) throws Exception;
	
	protected Class<T> getGenericClass(){
        return inferedClass;
    }
	
}
