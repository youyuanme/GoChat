package com.yibingding.haolaiwu.domian;

import java.util.List;

import com.alibaba.fastjson.JSONArray;

public class Appraisal {
	private String Guid;
	private String t_Associate_Guid;
	private String newsGuid;
	private String t_Talk_FromUserGuid;
	private String t_Talk_FromContent;
	private String t_Talk_FromDate;
	private String t_Talk_Good;
	private String t_Talk_Bad;
	private String t_Talk_Audit;
	private String t_DelState;
	private String fromLoginId;
	private String fromRealName;
	private String fromNickName;
	private String fromPic;
	private String fromUserStyle;
	private String newsTitle;
	private String newsPic;
	private String newsDate;
	private String replyCount;

	public String getNewsGuid() {
		return newsGuid;
	}

	public void setNewsGuid(String newsGuid) {
		this.newsGuid = newsGuid;
	}

	public String getGuid() {
		return Guid;
	}

	public void setGuid(String guid) {
		Guid = guid;
	}

	public String getT_Associate_Guid() {
		return t_Associate_Guid;
	}

	public void setT_Associate_Guid(String t_Associate_Guid) {
		this.t_Associate_Guid = t_Associate_Guid;
	}

	public String getT_Talk_FromUserGuid() {
		return t_Talk_FromUserGuid;
	}

	public void setT_Talk_FromUserGuid(String t_Talk_FromUserGuid) {
		this.t_Talk_FromUserGuid = t_Talk_FromUserGuid;
	}

	public String getT_Talk_FromContent() {
		return t_Talk_FromContent;
	}

	public void setT_Talk_FromContent(String t_Talk_FromContent) {
		this.t_Talk_FromContent = t_Talk_FromContent;
	}

	public String getT_Talk_FromDate() {
		return t_Talk_FromDate;
	}

	public void setT_Talk_FromDate(String t_Talk_FromDate) {
		this.t_Talk_FromDate = t_Talk_FromDate;
	}

	public String getT_Talk_Good() {
		return t_Talk_Good;
	}

	public void setT_Talk_Good(String t_Talk_Good) {
		this.t_Talk_Good = t_Talk_Good;
	}

	public String getT_Talk_Bad() {
		return t_Talk_Bad;
	}

	public void setT_Talk_Bad(String t_Talk_Bad) {
		this.t_Talk_Bad = t_Talk_Bad;
	}

	public String getT_Talk_Audit() {
		return t_Talk_Audit;
	}

	public void setT_Talk_Audit(String t_Talk_Audit) {
		this.t_Talk_Audit = t_Talk_Audit;
	}

	public String getT_DelState() {
		return t_DelState;
	}

	public void setT_DelState(String t_DelState) {
		this.t_DelState = t_DelState;
	}

	public String getFromLoginId() {
		return fromLoginId;
	}

	public void setFromLoginId(String fromLoginId) {
		this.fromLoginId = fromLoginId;
	}

	public String getFromRealName() {
		return fromRealName;
	}

	public void setFromRealName(String fromRealName) {
		this.fromRealName = fromRealName;
	}

	public String getFromNickName() {
		return fromNickName;
	}

	public void setFromNickName(String fromNickName) {
		this.fromNickName = fromNickName;
	}

	public String getFromPic() {
		return fromPic;
	}

	public void setFromPic(String fromPic) {
		this.fromPic = fromPic;
	}

	public String getFromUserStyle() {
		return fromUserStyle;
	}

	public void setFromUserStyle(String fromUserStyle) {
		this.fromUserStyle = fromUserStyle;
	}

	public String getNewsTitle() {
		return newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	public String getNewsPic() {
		return newsPic;
	}

	public void setNewsPic(String newsPic) {
		this.newsPic = newsPic;
	}

	public String getNewsDate() {
		return newsDate;
	}

	public void setNewsDate(String newsDate) {
		this.newsDate = newsDate;
	}

	public String getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(String replyCount) {
		this.replyCount = replyCount;
	}

	public JSONArray getStrReply() {
		return strReply;
	}

	public void setStrReply(JSONArray strReply) {
		this.strReply = strReply;
	}

	private JSONArray strReply;
}
