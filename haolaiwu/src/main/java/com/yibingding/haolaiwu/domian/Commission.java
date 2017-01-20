package com.yibingding.haolaiwu.domian;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class Commission {
private String Guid;
private String t_User_Guid;
private String t_UserAccount_Ratio;
private String t_UserAccount_Price;
private String t_UserAccount_AddReward;
private String t_UserAccount_ReduceReward;
private String t_UserAccount_Reward;
private String t_AddDate;
private String Reward;

//辅助属性
private String adddatestr;
private int type;
static SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd hh:mm:ss");
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
public String getT_UserAccount_Ratio() {
	return t_UserAccount_Ratio;
}
public void setT_UserAccount_Ratio(String t_UserAccount_Ratio) {
	this.t_UserAccount_Ratio = t_UserAccount_Ratio;
}
public String getT_UserAccount_Price() {
	return t_UserAccount_Price;
}
public void setT_UserAccount_Price(String t_UserAccount_Price) {
	this.t_UserAccount_Price = t_UserAccount_Price;
}
public String getT_UserAccount_AddReward() {
	return t_UserAccount_AddReward;
}
public void setT_UserAccount_AddReward(String t_UserAccount_AddReward) {
	if(t_UserAccount_AddReward!=null&&(!t_UserAccount_AddReward.equals(""))){
		setType(0);
	}
	this.t_UserAccount_AddReward = t_UserAccount_AddReward;
}
public String getT_UserAccount_ReduceReward() {
	return t_UserAccount_ReduceReward;
}
public void setT_UserAccount_ReduceReward(String t_UserAccount_ReduceReward) {
	if(t_UserAccount_ReduceReward!=null&&(!t_UserAccount_ReduceReward.equals(""))){
		setType(1);
	}
	this.t_UserAccount_ReduceReward = t_UserAccount_ReduceReward;
}
public String getT_UserAccount_Reward() {
	return t_UserAccount_Reward;
}
public void setT_UserAccount_Reward(String t_UserAccount_Reward) {
	this.t_UserAccount_Reward = t_UserAccount_Reward;
}
public String getT_AddDate() {
	return t_AddDate;
}
public void setT_AddDate(String t_AddDate) {
	this.t_AddDate = t_AddDate;
	if(adddatestr==null||adddatestr.equals("")){
		try {
			Date date = format.parse(t_AddDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			adddatestr = calendar.get(Calendar.MONTH)+1+"/"+calendar.get(Calendar.DAY_OF_MONTH)+" "+calendar.get(calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);
			Log.v("this", t_AddDate+",,"+adddatestr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
public String getReward() {
	return Reward;
}
public void setReward(String reward) {
	Reward = reward;
}
public String getAdddatestr() {
	return adddatestr;
}
public void setAdddatestr(String adddatestr) {
	this.adddatestr = adddatestr;
}
public int getType() {
	return type;
}
public void setType(int type) {
	this.type = type;
}
}
