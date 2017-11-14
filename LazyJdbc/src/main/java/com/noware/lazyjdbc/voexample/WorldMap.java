package com.noware.lazyjdbc.voexample;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Struct;
import java.util.HashMap;

import com.noware.lazyjdbc.Arrayable;

public class WorldMap implements Serializable, Arrayable {

	private static final long serialVersionUID = 1L;

	private HashMap<String, Nation> nations;
	
	public void addCity(City city)
	{
		if (nations==null)
		{
			nations = new HashMap<String, Nation>();
		}
		Nation curr = nations.get(city.getNation());
		if (curr==null)
		{
			curr = new Nation();
			nations.put(city.getNation(), curr);
		}
		curr.addCity(city);		
	}
	
	
	@Override
	public void setObjectFromStruct(Struct st) throws Exception {
		Object[] ob = st.getAttributes();		
		City city = new City();
		city.setCityName((String)ob[1]);
		city.setNation((String)ob[2]);
		city.setInhabitants((BigDecimal)ob[3]);

		addCity(city);
	}
	
}
