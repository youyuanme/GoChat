package com.yibingding.haolaiwu.domian;

import java.util.List;

public class Province {

	String ProvinceID;
	String ProvinceName;
	List<City> cities;

	public Province(String provinceID, String provinceName, List<City> cities) {
		super();
		ProvinceID = provinceID;
		ProvinceName = provinceName;
		this.cities = cities;
	}
	public Province(String provinceID, String provinceName) {
		super();
		ProvinceID = provinceID;
		ProvinceName = provinceName;
	}

	public String getProvinceID() {
		return ProvinceID;
	}

	public void setProvinceID(String provinceID) {
		ProvinceID = provinceID;
	}

	public String getProvinceName() {
		return ProvinceName;
	}

	public void setProvinceName(String provinceName) {
		ProvinceName = provinceName;
	}

	public List<City> getCities() {
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}

}
