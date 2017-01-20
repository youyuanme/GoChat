package com.yibingding.haolaiwu.domian;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Customer implements Parcelable {
	private String Guid;
	private String t_User_Guid;
	private String t_Assicoate_Guid;
	private String t_Client_Name;
	private String t_Client_CardID;
	private String t_Client_Phone;
	private String t_Client_QQ;
	private String t_Client_Email;
	private String t_Client_Remark;
	private String t_Client_Consultant;
	private String ConsultantLoginId;
	private String ConsultantRealName;
	private String t_Client_dState;
	private String t_Client_dDate;
	private String t_Client_sState;
	private String t_Client_sDate;
	private String t_Client_mState;
	private String t_Client_mDate;
	private String t_Client_eState;
	private String t_Client_eDate;
	private String names;
	private String style;
	private String pic;
	private String ProVinceName;
	private String CityName;
	private String DistrictName;
	private String Street;
	private String AveragePrice;

	@Override
	public String toString() {
		return "Customer [Guid=" + Guid + ", t_User_Guid=" + t_User_Guid
				+ ", t_Assicoate_Guid=" + t_Assicoate_Guid + ", t_Client_Name="
				+ t_Client_Name + ", t_Client_CardID=" + t_Client_CardID
				+ ", t_Client_Phone=" + t_Client_Phone + ", t_Client_QQ="
				+ t_Client_QQ + ", t_Client_Email=" + t_Client_Email
				+ ", t_Client_Remark=" + t_Client_Remark
				+ ", t_Client_Consultant=" + t_Client_Consultant
				+ ", ConsultantLoginId=" + ConsultantLoginId
				+ ", ConsultantRealName=" + ConsultantRealName
				+ ", t_Client_dState=" + t_Client_dState + ", t_Client_dDate="
				+ t_Client_dDate + ", t_Client_sState=" + t_Client_sState
				+ ", t_Client_sDate=" + t_Client_sDate + ", t_Client_mState="
				+ t_Client_mState + ", t_Client_mDate=" + t_Client_mDate
				+ ", t_Client_eState=" + t_Client_eState + ", t_Client_eDate="
				+ t_Client_eDate + ", names=" + names + ", style=" + style
				+ ", pic=" + pic + ", ProVinceName=" + ProVinceName
				+ ", CityName=" + CityName + ", DistrictName=" + DistrictName
				+ ", Street=" + Street + ", AveragePrice=" + AveragePrice
				+ ", time=" + time + "]";
	}

	// 辅助属性
	private String time;

	public String getGuid() {
		return Guid;
	}

	public void setGuid(String guid) {
		Guid = guid;
	}

	public String getT_User_Guid() {
		return t_User_Guid;
	}

	public void setT_User_Guid(String t_User_Guid) {
		this.t_User_Guid = t_User_Guid;
	}

	public String getT_Assicoate_Guid() {
		return t_Assicoate_Guid;
	}

	public void setT_Assicoate_Guid(String t_Assicoate_Guid) {
		this.t_Assicoate_Guid = t_Assicoate_Guid;
	}

	public String getT_Client_Name() {
		return t_Client_Name;
	}

	public void setT_Client_Name(String t_Client_Name) {
		this.t_Client_Name = t_Client_Name;
	}

	public String getT_Client_CardID() {
		return t_Client_CardID;
	}

	public void setT_Client_CardID(String t_Client_CardID) {
		this.t_Client_CardID = t_Client_CardID;
	}

	public String getT_Client_Phone() {
		return t_Client_Phone;
	}

	public void setT_Client_Phone(String t_Client_Phone) {
		this.t_Client_Phone = t_Client_Phone;
	}

	public String getT_Client_QQ() {
		return t_Client_QQ;
	}

	public void setT_Client_QQ(String t_Client_QQ) {
		this.t_Client_QQ = t_Client_QQ;
	}

	public String getT_Client_Email() {
		return t_Client_Email;
	}

	public void setT_Client_Email(String t_Client_Email) {
		this.t_Client_Email = t_Client_Email;
	}

	public String getT_Client_Remark() {
		return t_Client_Remark;
	}

	public void setT_Client_Remark(String t_Client_Remark) {
		this.t_Client_Remark = t_Client_Remark;
	}

	public String getT_Client_Consultant() {
		return t_Client_Consultant;
	}

	public void setT_Client_Consultant(String t_Client_Consultant) {
		this.t_Client_Consultant = t_Client_Consultant;
	}

	public String getConsultantLoginId() {
		return ConsultantLoginId;
	}

	public void setConsultantLoginId(String consultantLoginId) {
		ConsultantLoginId = consultantLoginId;
	}

	public String getConsultantRealName() {
		return ConsultantRealName;
	}

	public void setConsultantRealName(String consultantRealName) {
		ConsultantRealName = consultantRealName;
	}

	public String getT_Client_dState() {
		return t_Client_dState;
	}

	public void setT_Client_dState(String t_Client_dState) {
		this.t_Client_dState = t_Client_dState;
	}

	public String getT_Client_dDate() {
		return t_Client_dDate;
	}

	public void setT_Client_dDate(String t_Client_dDate) {
		this.t_Client_dDate = t_Client_dDate;
	}

	public String getT_Client_sState() {
		return t_Client_sState;
	}

	public void setT_Client_sState(String t_Client_sState) {
		this.t_Client_sState = t_Client_sState;
	}

	public String getT_Client_sDate() {
		return t_Client_sDate;
	}

	public void setT_Client_sDate(String t_Client_sDate) {
		this.t_Client_sDate = t_Client_sDate;
	}

	public String getT_Client_mState() {
		return t_Client_mState;
	}

	public void setT_Client_mState(String t_Client_mState) {
		this.t_Client_mState = t_Client_mState;
	}

	public String getT_Client_mDate() {
		return t_Client_mDate;
	}

	public void setT_Client_mDate(String t_Client_mDate) {
		this.t_Client_mDate = t_Client_mDate;
	}

	public String getT_Client_eState() {
		return t_Client_eState;
	}

	public void setT_Client_eState(String t_Client_eState) {
		this.t_Client_eState = t_Client_eState;
	}

	public String getT_Client_eDate() {
		return t_Client_eDate;
	}

	public void setT_Client_eDate(String t_Client_eDate) {
		this.t_Client_eDate = t_Client_eDate;
	}

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getProVinceName() {
		return ProVinceName;
	}

	public void setProVinceName(String proVinceName) {
		ProVinceName = proVinceName;
	}

	public String getCityName() {
		return CityName;
	}

	public void setCityName(String cityName) {
		CityName = cityName;
	}

	public String getDistrictName() {
		return DistrictName;
	}

	public void setDistrictName(String districtName) {
		DistrictName = districtName;
	}

	public String getStreet() {
		return Street;
	}

	public void setStreet(String street) {
		Street = street;
	}

	public String getAveragePrice() {
		return AveragePrice;
	}

	public void setAveragePrice(String averagePrice) {
		AveragePrice = averagePrice;
	}

	public String getTime() {
		String value ="";
		if(t_Client_dState.equals("0")||t_Client_sState.equals("0")){
			return value;
		}else if(t_Client_sState.equals("1")){
			value +="到访时间："+t_Client_sDate;
		}else if(t_Client_mState.equals("1")){
			value +="\n大定时间："+t_Client_mDate;
		}else if(t_Client_eState.equals("1")){
			value +="\n签约时间："+t_Client_eDate;
		}
		
		return value;
		// return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public static final Parcelable.Creator<Customer> CREATOR = new Creator<Customer>() {
		public Customer createFromParcel(Parcel source) {
			Customer adInfo = new Customer();
			adInfo.Guid = source.readString();
			adInfo.t_User_Guid = source.readString();
			adInfo.t_Assicoate_Guid = source.readString();
			adInfo.t_Client_Name = source.readString();
			adInfo.t_Client_CardID = source.readString();
			adInfo.t_Client_Phone = source.readString();
			adInfo.t_Client_QQ = source.readString();
			adInfo.t_Client_Email = source.readString();
			adInfo.t_Client_Remark = source.readString();
			adInfo.t_Client_Consultant = source.readString();
			adInfo.ConsultantLoginId = source.readString();
			adInfo.ConsultantRealName = source.readString();
			adInfo.t_Client_dState = source.readString();
			adInfo.t_Client_dDate = source.readString();
			adInfo.t_Client_sState = source.readString();
			adInfo.t_Client_sDate = source.readString();
			adInfo.t_Client_mState = source.readString();
			adInfo.t_Client_mDate = source.readString();
			adInfo.t_Client_eDate = source.readString();
			adInfo.t_Client_eState = source.readString();
			adInfo.names = source.readString();
			adInfo.pic = source.readString();
			adInfo.style = source.readString();
			adInfo.ProVinceName = source.readString();
			adInfo.CityName = source.readString();
			adInfo.DistrictName = source.readString();
			adInfo.Street = source.readString();
			adInfo.AveragePrice = source.readString();// 27
			return adInfo;
		}

		public Customer[] newArray(int size) {
			return new Customer[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(Guid);
		dest.writeString(t_User_Guid);
		dest.writeString(t_Assicoate_Guid);
		dest.writeString(t_Client_Name);
		dest.writeString(t_Client_CardID);
		dest.writeString(t_Client_Phone);
		dest.writeString(t_Client_QQ);
		dest.writeString(t_Client_Email);
		dest.writeString(t_Client_Remark);
		dest.writeString(t_Client_Consultant);
		dest.writeString(ConsultantLoginId);
		dest.writeString(ConsultantRealName);
		dest.writeString(t_Client_dState);
		dest.writeString(t_Client_dDate);
		dest.writeString(t_Client_sState);
		dest.writeString(t_Client_sDate);
		dest.writeString(t_Client_mState);
		dest.writeString(t_Client_mDate);
		dest.writeString(t_Client_eDate);
		dest.writeString(t_Client_eState);
		dest.writeString(names);
		dest.writeString(pic);
		dest.writeString(style);
		dest.writeString(ProVinceName);
		dest.writeString(CityName);
		dest.writeString(DistrictName);
		dest.writeString(Street);
		dest.writeString(AveragePrice);
	}
}
