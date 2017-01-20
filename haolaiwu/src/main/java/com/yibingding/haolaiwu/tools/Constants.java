package com.yibingding.haolaiwu.tools;

public class Constants {
	
	public static final String HOUSE_RENT = "HOUSE_RENT";//租房
	public static final String OLD_HOUSE = "OLD_HOUSE";//二手房
	public static final String BUILDING = "BUILDING";//楼盘
	
	public static final String RENT_RECOMMEND = "RENT_RECOMMEND";//房屋租赁 推荐租房
	public static final String RENT_WANT = "RENT_WANT";//房屋租赁 我要租房
	public static final String OLD_RECOMMEND = "OLD_RECOMMEND";//二手房 推荐购房
	public static final String OLD_WANT = "OLD_WANT";//二手房 我要买房
	public static final String BUILDING_RECOMMEND = "BUILDING_RECOMMEND";//楼盘 推荐购房
	public static final String BUILDING_WANT_LOOK = "BUILDING_WANT_LOOK";//楼盘 预约看房
	
	public static final String CONDITION_SELECTION_CLASSIFI_ID_TOTAL_MONEY = "9db25b43-7d25-4c1e-a9cc-fc9316e3e3aa";//总价id
	public static final String CONDITION_SELECTION_CLASSIFI_ID_HOUSE_TYPE = "8531c132-a555-466f-a490-949e3c5bb19a";//户型id
	public static final String CONDITION_SELECTION_CLASSIFI_ID_TYPE = "1458fc4e-91b4-4a44-91b9-415a06a5050e";//住宅类型id
	public static final String CONDITION_SELECTION_CLASSIFI_ID_DECORATE_TYPE = "a9357d99-165e-4b92-af69-b478b0efb1d0";//装修类型id
	public static final String CONDITION_SELECTION_CLASSIFI_ID_RENT_MONEY_TYPE = "2233943c-1140-40bf-bf26-a594e4da214b";//租金类型id

	// public static final String IP = "http://120.25.106.244:8010/";
	public static final String IP = "http://123.56.192.134:8001/";
	public static final String SERVER_IP = IP + "webservice/";
	public static final String SMS_URL="SMS_WebService.asmx/SendSMS";
	// 加入我们
	public static final String GET_JoinUS = SERVER_IP +"JoinUS_WebService.asmx/GetJoinUS";
	// 首页轮播图跳转地址
	public static final String GET_INDEX_image = IP +"/H5/AdView.aspx?Guid=";
	// 引导页图片
	public static final String GET_Splash_image = IP +"webservice/StartPage_WebService.asmx/GetData";
	// 获取房贷计算器
	public static final String GET_Counter = SERVER_IP +"OnOff_WebService.asmx/Counter";
	//获取省市区
	public static final String GET_PROVINCE_CITY= SERVER_IP +"China_WebService.asmx/getProvince";
	//获取条件筛选的类别
	public static final String GET_CONDITION_SELECTION_CLASSIFI= SERVER_IP +"Style_WebService.asmx/GetStyle";
	// 新闻信息搜索
	public static final String GET_SEL_NEWS= SERVER_IP +"News_WebService.asmx/SelNews";
	//获取楼盘列表
	public static final String GET_BUILDING_LIST= SERVER_IP +"Build_WebService.asmx/GetBuild";
	//获取二手房列表
	public static final String GET_OLD_HOUSE_LIST= SERVER_IP +"SecHouse_WebService.asmx/GetSecHouse";
	//获取房屋租赁列表
	public static final String GET_RENT_HOUSE_LIST= SERVER_IP +"RentalHouse_WebService.asmx/GetRental";
	// 获取租赁房开关
	public static final String GET_ON_OFF_RENTAL= SERVER_IP +"OnOff_WebService.asmx/OnOffRental?Token=";
	// 安卓分享下载地址
	public static final String GET_DOWN_LOAD_SRC= SERVER_IP +"OnOff_WebService.asmx/DownLoadSrc";
	// 判断是否有未读信息,userGuid:用户guid
	public static final String GET_IS_READ_SRC= SERVER_IP +"Messages_WebService.asmx/IsRead";
	//获取房屋租赁详情
	public static final String GET_RENT_HOUSE_DETAILS= SERVER_IP +"RentalHouse_WebService.asmx/GetRentalView";
	//获取二手房详情
	public static final String GET_OLD_HOUSE_DETAILS= SERVER_IP +"SecHouse_WebService.asmx/GetSecHouseView";
	// 获取二手房开关
	public static final String GET_ON_OFF_SEC= SERVER_IP +"OnOff_WebService.asmx/OnOffSec?Token=";
	//收藏
	public static final String COLLECT= SERVER_IP +"Collection_WebService.asmx/AddCollection";
	//取消收藏
	public static final String UNCOLLECT= SERVER_IP +"Collection_WebService.asmx/DelCollection";
	//是否收藏
	public static final String IS_COLLECTED= SERVER_IP +"Collection_WebService.asmx/GetCollection";
	//楼盘、房屋推荐登记
	public static final String RECOMMEND= SERVER_IP +"Client_WebService.asmx/AddOrModifyClient";
	
	//用户模块API
	public static final String User_Login_URL="User_WebService.asmx/Login";          //用户登录
	public static final String User_Regist_URL="User_WebService.asmx/AddUsers";          //注册会员
	public static final String User_ForgetPwd_URL="User_WebService.asmx/ForgetPwd";          //忘记会员密码
	public static final String User_AlertPwd_URL="User_WebService.asmx/EditPwdbyGuid";          //更改会员密码
	public static final String User_CompleteUser_URL="User_WebService.asmx/CompleteUser";          //完善客户信息
	public static final String User_Commission_URL="UserAccount_WebService.asmx/GetUserAccount";          //用户酬金
	public static final String User_Score_URL="UserIntegral_WebService.asmx/GetUserIntegral";          //用户积分
	public static final String User_GetUserListByStyle_URL="User_WebService.asmx/GetUserByStyle";          //获取用户列表
	public static final String User_GetUserBank_URL="Bank_WebService.asmx/GetUserBank";          //获取个人银行卡信息
	public static final String User_AddOREditBank_URL="Bank_WebService.asmx/AddOrModifyBank";          //添加或编辑银行卡
	public static final String User_UnBandBank_URL="Bank_WebService.asmx/DelBank";          //删除银行卡
	public static final String User_TakeMonyRequst_URL="Withdrawal_WebService.asmx/AddOrModifyWithdrawal";          //提现申请
	public static final String User_AddFeedBack_URL="FeedBack_WebService.asmx/AddFeedBack";          //添加意见
	public static final String User_GetAppraisal_URL="UserTalk_WebService.asmx/MyTalk";          //获取我的评论
	public static final String User_GetTeamUser_URL="User_WebService.asmx/GetUserByLeader";          //获取我的团队成员
	public static final String User_GetClientList_URL="Client_WebService.asmx/GetClient";          //获取我的客户列表
	public static final String User_GetClientListByPage_URL="Client_WebService.asmx/GetClientByPage"; //获取我的客户列表分页
	public static final String User_EditClientState_URL="Client_WebService.asmx/EditState";          //更改客户状态
	public static final String User_EditClientConsultant_URL="Client_WebService.asmx/EditConsultant";          //对客户进行顾问分配
	public static final String UploadImg_URL="Client_WebService.asmx/UploadPic";          //合同上传图片
	public static final String PAYMSG_URL="ALiPay_WebService.asmx/GetALiPay";          //获取商户支付宝信息
	// 获取新闻列表信息
	public static final String GET_NEW = SERVER_IP + "News_WebService.asmx/GetNewsList";
	// 获取推荐新闻信息
	public static final String GET_RECOMMAND_NEW = SERVER_IP + "News_WebService.asmx/GetRecommandNews";
	// 图片地址
	public static final String IMAGE_URL = IP + "Attach/";
	// 新闻详情
	public static final String NEW_DETAILS_URL = IP + "H5/NewsView.aspx?Guid=";
	// 新闻分享
	public static final String NEW_SHARD = "http://app.fmmvip.com:8001/H5/NewsView.aspx?Guid=";
	// 新闻评论
	public static final String NEW_COMMENT_URL = SERVER_IP + "UserTalk_WebService.asmx/GetTalk";
	// 获取用户是否收藏
	public static final String NEW_ISCOLLECTION_URL = SERVER_IP + "Collection_WebService.asmx/GetCollection";
	// 添加收藏关注
	public static final String NEW_ADDISCOLLECTION_URL = SERVER_IP + "Collection_WebService.asmx/AddCollection";
	// 取消关注收藏
	public static final String NEW_DELISCOLLECTION_URL = SERVER_IP + "Collection_WebService.asmx/DelCollection";
	// 添加评论
	public static final String NEW_COMMENTADD_URL = SERVER_IP + "UserTalk_WebService.asmx/AddTalk";
	// 相关评论
	public static final String NEW_ABOUTCOMMENT_URL = SERVER_IP + "UserTalk_WebService.asmx/GetTalk";
	// 取消关注收藏，
	public static final String MY_ATTENTION_DELCOLLECION_URL = SERVER_IP + "Collection_WebService.asmx/DelCollection";
	// 获取个人关注收藏列表
	public static final String MY_ATTENTION_GETCOLLECIONLIST_URL = SERVER_IP + "Collection_WebService.asmx/GetCollectionList";
	// 获取个人系统信息,
	public static final String MY_GETMESSAGE_URL = SERVER_IP + "Messages_WebService.asmx/GetMessage";
	// 删除系统信息
	public static final String MY_DELMESSAGE_URL = SERVER_IP + "Messages_WebService.asmx/Edit";
	// 获取系统信息明细,
	public static final String MY_GETMESSAGEDETAILS_URL = SERVER_IP + "Messages_WebService.asmx/GetView";
	
	public static final boolean isdebug = false;
	//城市列表
	public static final String GETCITY_URL=SERVER_IP+"China_WebService.asmx/GetCity";
	//首页广告图
	public static final String HOMEADS_URL=SERVER_IP+"Ads_WebService.asmx/GetData?Token=";
	//推荐楼盘
	public static final String COMMANDBUILDING_URL=SERVER_IP+"Build_WebService.asmx/GetRecommandBuild";
	//推荐新闻
	public static final String COMMANDNEWS_URL=SERVER_IP+"News_WebService.asmx/GetRecommandNews";
	//楼盘明细
	public static final String HOUSEVIEW_URL=SERVER_IP+"Build_WebService.asmx/GetBuildView";
	
	public static final String GetUserByManager_URL=SERVER_IP+"User_WebService.asmx/GetUserByManager";
	
}
