package com.yibingding.haolaiwu.domian;

public class District {

	String DistrictID;
	String DistrictName;

	public District(String districtID, String districtName) {
		super();
		DistrictID = districtID;
		DistrictName = districtName;
	}

	public String getDistrictID() {
		return DistrictID;
	}

	public void setDistrictID(String districtID) {
		DistrictID = districtID;
	}

	public String getDistrictName() {
		return DistrictName;
	}

	public void setDistrictName(String districtName) {
		DistrictName = districtName;
	}

}
