package com.yibingding.haolaiwu.domian;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class Score {
	private String Guid;
	private String t_User_Guid;
	private String t_UserIntegral_Add;
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
	public String getT_UserIntegral_Add() {
		return t_UserIntegral_Add;
	}
	public void setT_UserIntegral_Add(String t_UserIntegral_Add) {
		this.t_UserIntegral_Add = t_UserIntegral_Add;
		if(t_UserIntegral_Add!=null&&(!t_UserIntegral_Add.equals(""))){
			setType(0);
		}
	}
	public String getT_UserIntegral_Reduce() {
		return t_UserIntegral_Reduce;
	}
	public void setT_UserIntegral_Reduce(String t_UserIntegral_Reduce) {
		this.t_UserIntegral_Reduce = t_UserIntegral_Reduce;
		if(t_UserIntegral_Reduce!=null&&(!t_UserIntegral_Reduce.equals(""))){
			setType(1);
		}
	}
	public String getT_UserIntegral_Reward() {
		return t_UserIntegral_Reward;
	}
	public void setT_UserIntegral_Reward(String t_UserIntegral_Reward) {
		this.t_UserIntegral_Reward = t_UserIntegral_Reward;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getAdddatestr() {
		return adddatestr;
	}
	public void setAdddatestr(String adddatestr) {
		this.adddatestr = adddatestr;
	}
	private String t_UserIntegral_Reduce;
	private String t_UserIntegral_Reward;
	private String t_AddDate;
	private String Reward;
}
