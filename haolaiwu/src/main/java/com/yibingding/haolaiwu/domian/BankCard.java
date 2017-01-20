package com.yibingding.haolaiwu.domian;

public class BankCard {
	private String Guid;
	private String t_Bank_Name;
	private String t_Bank_NO;
	private String t_Bank_OpenAddress;
	private String t_Bank_OpenUser;
	private boolean selecked;

	public boolean isSelecked() {
		return selecked;
	}

	public void setSelecked(boolean selecked) {
		this.selecked = selecked;
	}

	private int banktype;

	public String getGuid() {
		return Guid;
	}

	public void setGuid(String guid) {
		Guid = guid;
	}

	public String getT_Bank_Name() {
		return t_Bank_Name;
	}

	public void setT_Bank_Name(String t_Bank_Name) {
		this.t_Bank_Name = t_Bank_Name;
		if (t_Bank_Name.contains("工商")) {
			banktype = 0;
		} else if (t_Bank_Name.contains("建设")) {
			banktype = 1;
		} else if (t_Bank_Name.contains("交通")) {
			banktype = 2;
		} else if (t_Bank_Name.contains("中国")) {
			banktype = 3;
		} else if (t_Bank_Name.contains("农业")) {
			banktype = 4;
		} else if (t_Bank_Name.contains("中信")) {
			banktype = 5;
		} else {
			banktype = 6;
		}
	}

	public String getT_Bank_NO() {
		return t_Bank_NO;
	}

	public void setT_Bank_NO(String t_Bank_NO) {
		this.t_Bank_NO = t_Bank_NO;
	}

	public String getT_Bank_OpenAddress() {
		return t_Bank_OpenAddress;
	}

	public void setT_Bank_OpenAddress(String t_Bank_OpenAddress) {
		this.t_Bank_OpenAddress = t_Bank_OpenAddress;
	}

	public String getT_Bank_OpenUser() {
		return t_Bank_OpenUser;
	}

	public void setT_Bank_OpenUser(String t_Bank_OpenUser) {
		this.t_Bank_OpenUser = t_Bank_OpenUser;
	}

	public int getBanktype() {
		return banktype;
	}

	public void setBanktype(int banktype) {
		this.banktype = banktype;
	}
}
