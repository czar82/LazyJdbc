package com.noware.lazyjdbc.voexample;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Nation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<City> cities;

	public void addCity(City city) {
		if (cities==null)
		{
			cities = new ArrayList<City>();
		}
		cities.add(city);
	}
}
