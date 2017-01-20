package com.yibingding.haolaiwu.domian;

import java.util.List;

public class City {

	String CityID;
	String CityName;
	List<District> districts;

	public City(String cityID, String cityName, List<District> districts) {
		super();
		CityID = cityID;
		CityName = cityName;
		this.districts = districts;
	}

	public City(String cityID, String cityName) {
		super();
		CityID = cityID;
		CityName = cityName;
	}

	public String getCityID() {
		return CityID;
	}

	public void setCityID(String cityID) {
		CityID = cityID;
	}

	public String getCityName() {
		return CityName;
	}

	public void setCityName(String cityName) {
		CityName = cityName;
	}

	public List<District> getDistricts() {
		return districts;
	}

	public void setDistricts(List<District> districts) {
		this.districts = districts;
	}

}
