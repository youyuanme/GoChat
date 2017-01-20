package com.sibozn.gochat.utils;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	/*
	 * 定义数据库基本信息，数据库名称，版本，表名
	 */
	//数据库名称
	public final static String DATABASE_NAME = "goChat.db";
	//数据库版本
	private final static int DATABASE_VERSION = 1;
	//聊天内容表
	public final static String CHAT_CONTENT_TABLE = "chat_content_table";
	// 聊天记录
	public final static String CHAT_HISTORY_TABLE = "chat_history_table";


	private final static String CREATE_CHAT_HISTORY_SQL1 = "CREATE TABLE " + CHAT_CONTENT_TABLE//聊天记录表
													+ " (_id Integer primary key autoincrement,"
													+ " fromUserName text,"
													+ " fromUserAvatar text,"
													+ " fromDate text,"
													+ " fromContent text,"
													+ " toUserName text,"
													+ " toUserAvatar text,"
													+ " toDate text,"
													+ " toContent text);";
	private final static String CREATE_CHAT_CONTENT_SQL = "CREATE TABLE " + CHAT_CONTENT_TABLE//聊天内容表
													+ " (_id Integer primary key autoincrement,"
													+ " fromUserName text," // 发送者uid
													+ " toUserName text," // 接收者uid
													+ " fromUserEmail text,"// 发送者邮箱
													+ " toUserEmail text,"// 接收者邮箱
													+ " date text,"// 发送接收时间
													+ " content text,"// 消息内容
													+ " type text,"//判断内容类型，0是文字，1是图片
													+ " fromWho text,"//判断消息来自me 还是you
													+ " icon text);";// 接收者或发送者头像图片
	private final static String CREATE_CHAT_HISTORY_SQL = "CREATE TABLE " + CHAT_HISTORY_TABLE//用户聊天记录表
													+ " (_id Integer primary key autoincrement,"
													+ " id text,"
													+ " from_his text,"
													+ " to_his text,"
													+ " data_his text,"
													+ " type text,"
													+ " time text,"
													+ " uid text,"
													+ " photo text,"
													+ " country text,"
													+ " city text,"
													+ " sex text,"
													+ " age text);";

	public DBHelper(Context context) {
		this(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public DBHelper(Context context, String dbName, CursorFactory factory,
			int version) {
		super(context, dbName, factory, version);
	}

	/*
	 * 初次建表
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_CHAT_CONTENT_SQL);// 聊天内容
		db.execSQL(CREATE_CHAT_HISTORY_SQL);//用户聊天记录表
	}
	/*
	 * 升级数据库
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_CHAT_CONTENT_SQL);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_CHAT_HISTORY_SQL);
		onCreate(db);
	}
}

/***********************************************************************************************************/

/*


//	//浏览名企展示/出租商列表
//	public final static String BROWS_COMPANY_TABLE = "brows_company_table";
//	//浏览配件供应/配件求购
//	public final static String BROWS_PEIJIANQIUGOU_TABLE = "brows_peijianqiugou_table";
//	//浏览配件商列表
//	public final static String BROWS_PEIJIANSHANGLIEBIAO_TABLE = "brows_peijianshangliebiao_table";
//	//整机求购
//	public final static String BROWS_ZHENGJIQIUGOU_TABLE = "brows_zhengjiqiugou_table";
//	//招聘求职
//	public final static String BROWS_ZHAOPINGQIUZHI_TABLE = "brows_zhaopinqiuzhi_table";
//	//整机供应商
//	public final static String BROWS_ZHENGJIGONGYINGSHANG_TABLE = "brows_zhengjigongyingshang_table";
//	//浏览二手信息/二手交易
//	public final static String BROWS_HANDLE_TABLE = "brow_handle_table";
//	//浏览招聘信息
//	public final static String BROWS_ZHAOPINGXINXI_TABLE = "brow_zhaopinxinxi_table";
//	//省份表
//	public final static String USER_PROVINCES_TABLE = "user_provinces";
//	//城市表
//	public final static String UAER_CITYS_TABLE = "user_citys";



private final static String BROWS_INQUIRY_SQL = "CREATE TABLE " +BROWS_INQUIRY_TABLE// 求租历史记录
		+"(_id Integer primary key autoincrement,"
		+ " Guid text,"
		+ " t_Title text,"
		+ " t_Describe text,"
		+ " ProvinceName text,"
		+ " CityName text,"
		+ " styles text,"
		+ " t_User_Pic text,"
		+ " User_guid text,"
		+ " t_Date text);";
	private final static String BROWS_ZHAOPINGQIUZHI_SQL = "CREATE TABLE " +BROWS_ZHAOPINGQIUZHI_TABLE// 招聘求职
			+"(_id Integer primary key autoincrement,"
			+ " Guid text,"
			+ " t_UnitName text,"
			+ " t_Position text,"
			+ " ProvinceName text,"
			+ " CityName text,"
			+ " t_Salary text,"
			+ " t_Publish_Type text,"
			+ " t_Date text,"
			+ " t_LInkMan text,"
			+ " User_guid text);";
	private final static String BROWS_ZHENGJIGONGYINGSHANG_SQL = "CREATE TABLE " +BROWS_ZHENGJIGONGYINGSHANG_TABLE// 整机供应商
			+"(_id Integer primary key autoincrement,"
			+ " Guid text,"
			+ " t_Supplier_Name text,"
			+ " ProvinceName text,"
			+ " CityName text,"
			+ " t_Supplier_Remark text,"
			+ " t_Admit text,"
			+ " t_User_Pic text,"
			+ " t_Date text,"
			+ " User_guid text);";
	private final static String BROWS_ZHENGJIQIUGOU_SQL = "CREATE TABLE " +BROWS_ZHENGJIQIUGOU_TABLE//整机求购
			+"(_id Integer primary key autoincrement,"
			+ " Guid text,"
			+ " t_User_Pic text,"
			+ " StrType text,"
			+ " t_Remark text,"
			+ " t_Date text,"
			+ " User_guid text);";
	private final static String BROWS_COMPANY_SQL = "CREATE TABLE " +BROWS_COMPANY_TABLE// /出租商列表
			+"(_id Integer primary key autoincrement,"
			+ " Guid text,"
			+ " t_Rental_Name text,"
			+ " ProvinceName text,"
			+ " t_Rental_Admit text,"
			+ " CityName text,"
			+ " t_Rental_Remark text,"
			+ " t_User_Pic text,"
			+ " t_Date text,"
			+ " User_guid text);";
	private final static String BROWS_PEIJIANQIUGOU_SQL = "CREATE TABLE " +BROWS_PEIJIANQIUGOU_TABLE// 配件求购
			+"(_id Integer primary key autoincrement,"
			+ " Guid text,"
			+ " t_User_Pic text,"
			+ " StrType text,"
			+ " t_Remark text,"
			+ " t_Date text,"
			+ " User_guid text);";
	private final static String BROWS_PEIJIANSHANGLIEBIAO_SQL = "CREATE TABLE " +BROWS_PEIJIANSHANGLIEBIAO_TABLE// 配件商列表
			+"(_id Integer primary key autoincrement,"
			+ " Guid text,"
			+ " t_Supplier_Name text,"
			+ " ProvinceName text,"
			+ " CityName text,"
			+ " StrType text,"
			+ " t_Supplier_Remark text,"
			+ " t_User_Pic text,"
			+ " t_Date text,"
			+ " User_guid text);";
	private final static String BROWS_HANDLE_SQL = "CREATE TABLE " +BROWS_HANDLE_TABLE//浏览二手信息/二手交易
			+"(_id Integer primary key autoincrement,"
			+ " Guid text,"
			+ " t_Company_Name text,"
			+ " ProvinceName text,"
			+ " CityName text,"
			+ " t_Describe text,"
			+ " t_User_Pic text,"
			+ " t_Date text,"
			+ " t_Trade_Style text,"
			+ " t_Trade_Price text,"
			+ " User_guid text);";

	private final static String CREATE_WEATHER_PROVINCES_SQL = "CREATE TABLE " + USER_PROVINCES_TABLE//省份表
			+ " (_id Integer primary key autoincrement,"
			+ " ProvinceName text,"
			+ " ProvinceID integer);";
*/
