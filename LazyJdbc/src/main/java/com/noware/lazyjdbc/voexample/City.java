package com.noware.lazyjdbc.voexample;

import java.io.Serializable;
import java.math.BigDecimal;

public class City implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String Nation;
	private String CityName;
	private BigDecimal inhabitants;
	
	public String getNation() {
		return Nation;
	}
	public void setNation(String nation) {
		Nation = nation;
	}
	public String getCityName() {
		return CityName;
	}
	public void setCityName(String cityName) {
		CityName = cityName;
	}
	public BigDecimal getInhabitants() {
		return inhabitants;
	}
	public void setInhabitants(BigDecimal inhabitants) {
		this.inhabitants = inhabitants;
	}

}
