package com.sibozn.gochat.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteStatement;

/**
 * 
 * 功能描述: 数据库管理类，具备增删改查操作。
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class DataBaseManager<T> {

	private DBHelper dbHelper;
	public static DataBaseManager instance = null;
	private SQLiteDatabase sqliteDatabase;
	
	/**
	 * 构造函数
	 * @param context	上下文对象
	 */
	private DataBaseManager(Context context) {
		dbHelper = new DBHelper(context);
		sqliteDatabase = dbHelper.getWritableDatabase();	
	}
	
	/***
	 * 获取本类对象实例
	 * @param context	上下文对象
	 * @return
	 */
	public static final DataBaseManager getInstance(Context context) {
		if (instance == null) {
			if(context == null){
				throw new RuntimeException("context is null");
			}
			instance = new DataBaseManager(context);
		}
		return instance;
	}
	
	/**
	 * 关闭数据库
	 */
	public void close() {
		if(sqliteDatabase.isOpen()){
			sqliteDatabase.close();
			sqliteDatabase = null;
		} 
		
		if(dbHelper != null){
			dbHelper.close(); 
			dbHelper = null;
		}
		
		if(instance != null) {
			instance = null;
		}
	}
	
	/**
	 * 执行一个不带占位符参数的sql语句
	 * @param sql
	 */
	public void execSql(String sql){
		if(sqliteDatabase.isOpen()){
			sqliteDatabase.execSQL(sql);
		}else{
			throw new RuntimeException("数据库已关闭");
		}
	}
	
	/**
	 * 查询表中多少条数据
	 * @param table	表名
	 * @return
	 * @throws Exception
	 */
	public int getDataCounts(String table)throws Exception{
		Cursor cursor = null;
		int counts = 0;
		if(sqliteDatabase.isOpen()){
			cursor = queryData2Cursor("select * from " + table , null);
			if(cursor != null && cursor.moveToFirst()){
				counts = cursor.getCount();
			}
		}else{
			throw new RuntimeException("数据库已关闭");
		}
		return counts;
	}
	
	/**
	 * 清空表中所有数据
	 * @param table 表名
	 * @throws Exception
	 */
	public void clearAllData(String table)throws Exception{
		if(sqliteDatabase.isOpen()){
			sqliteDatabase.execSQL("DELETE FROM " + table);
		}else{
			throw new RuntimeException("数据库已关闭");
		}
	}

	/**
	 * 插入数据
	 * @param sql  		执行更新操作的sql语句
	 * @param bindArgs	sql语句中的参数,参数的顺序对应占位符顺序
	 * @return	id		返回插入对应的id
	 * 						0 --> 插入失败	
	 */
	public Long insertDataBySql(String sql, String[] bindArgs) throws Exception{
		long result = 0;
		if(sqliteDatabase.isOpen()){
			SQLiteStatement statement = sqliteDatabase.compileStatement(sql);
			if(bindArgs != null){
				int size = bindArgs.length;
				for(int i = 0; i < size; i++){
					//将参数和占位符绑定，对应
					statement.bindString(i+1, bindArgs[i]);
				}
				result = statement.executeInsert();
				statement.close();
			}
		}else{
			throw new RuntimeException("数据库已关闭");
		}
		return result;
	}
	
	/**
	 * 插入数据
	 * @param table		数据库表名称
	 * @param values	数据
	 * @return			返回插入对应的id
	 * 						0 --> 插入失败	
	 */
	public Long insertData(String table, ContentValues values) throws Exception{
		long result = 0;
		if(sqliteDatabase.isOpen()){
			result = sqliteDatabase.insertOrThrow(table, null, values);
		}else{
			throw new RuntimeException("数据库已关闭");
		}
		return result;
	}
	
	/**
	 * 批量插入数据
	 * @param table		数据库表名称
	 * @param listData	数据
	 * @param args		数据的键名key
	 * @return
	 * @throws Exception
	 */
	public long insertBatchData(String table, List<Map<String,Object>> listData,String[] args) throws Exception{
		sqliteDatabase.beginTransaction();
		long returnNum = 0;
		try {
			ContentValues values = new ContentValues();
			for(int i=0; i<listData.size(); i++){
				for(int j=0; j<args.length; j++){
					values.put(args[j], listData.get(i).get(args[j]).toString());
				}
				long num = insertData(table, values);
				returnNum = returnNum + num;
			}
			sqliteDatabase.setTransactionSuccessful();
		} finally {
			sqliteDatabase.endTransaction();
		}
		return returnNum;
	}
	/**
	 * 批量插入数据
	 * @param table		数据库表名称
	 * @param list	数据
	 * @param args		数据的键名key
	 * @return
	 * @throws Exception
	 */
	public long insertBatchData2(String table, List<Map<String, Object>> list,String[] args) throws Exception{
		sqliteDatabase.beginTransaction();//手动设置开始事务
		long returnNum = 0;
		try {
			ContentValues values = new ContentValues();
			for(int i=0; i<list.size(); i++){
				for(int j=0; j<args.length; j++){
					values.put(args[j], list.get(i).get(args[j]).toString());
				}
				long num = insertData(table, values);
				returnNum = returnNum + num;
			}
			sqliteDatabase.setTransactionSuccessful();//设置事务处理成功，不设置会自动回滚不提交。
		} finally {
			sqliteDatabase.endTransaction();//处理完成
		}
		return returnNum;
	}
	
	/**
	 * 更新数据
	 * @param sql  		执行更新操作的sql语句
	 * @param bindArgs	sql语句中的参数,参数的顺序对应占位符顺序
	 */
	public void updateDataBySql(String sql, String[] bindArgs) throws Exception{
		if(sqliteDatabase.isOpen()){
			SQLiteStatement statement = sqliteDatabase.compileStatement(sql);
			if(bindArgs != null){
				int size = bindArgs.length;
				
				for(int i = 0; i < size; i++){
					statement.bindString(i+1, bindArgs[i]);
				}
				
				statement.execute();
				statement.close();
			}
		}
		else{
			throw new RuntimeException("数据库已关闭");
		}
	}
	
	/**
	 * 更新数据
	 * @param table			表名
	 * @param values		表示更新的数据
	 * @param whereClause	表示SQL语句中条件部分的语句
	 * @param whereArgs		表示占位符的值
	 * @return
	 */
	public int updataData(String table, ContentValues values, String whereClause, String[] whereArgs)throws Exception{
		int result = 0;
		if(sqliteDatabase.isOpen()){
			result = sqliteDatabase.update(table, values, whereClause, whereArgs);
		}else{
			throw new RuntimeException("数据库已关闭");
		}
		return result;
	}

	/**
	 * 删除数据
	 * @param sql  		执行更新操作的sql语句
	 * @param bindArgs	sql语句中的参数,参数的顺序对应占位符顺序
	 */
	public void deleteDataBySql(String sql, String[] bindArgs) throws Exception{
		if(sqliteDatabase.isOpen()){
			SQLiteStatement statement = sqliteDatabase.compileStatement(sql);
			if(bindArgs != null){
				int size = bindArgs.length;
				for(int i = 0; i < size; i++){
					statement.bindString(i+1, bindArgs[i]);
				}
				statement.execute();	
				statement.close();
			}
		}else{
			throw new RuntimeException("数据库已关闭");
		}
	}

	/**
	 * 删除数据
	 * @param table			表名
	 * @param whereClause	表示SQL语句中条件部分的语句
	 * @param whereArgs		表示占位符的值
	 * @return				
	 */
	public int deleteData(String table, String whereClause, String[] whereArgs)throws Exception{
		int result = 0;
		if(sqliteDatabase.isOpen()){
			result = sqliteDatabase.delete(table, whereClause, whereArgs);
		}else{
			throw new RuntimeException("数据库已关闭");
		}
		return result;
	}
	
	/**
	 * 查询数据 
	 * @param table				表名称
	 * @param columns			查询需要返回的字段
	 * @param selection			sql语句中的条件语句
	 * @param selectionArgs		占位符的值
	 * @param groupBy			表示分组，可以为null
	 * @param having			sql语句中的having，可以为null
	 * @param orderBy			表示结果的排序，可以为null
	 * @return
	 */
	public Cursor queryData(String table, String[] columns, String selection,
								String[] selectionArgs, String groupBy,
									String having, String orderBy)throws Exception{
		return queryData(table, columns, selection, selectionArgs, groupBy, having, orderBy, null);
	}
	
	/**
	 * 查询数据 
	 * @param table				表名称
	 * @param columns			查询需要返回的字段
	 * @param selection			sql语句中的条件语句
	 * @param selectionArgs		占位符的值
	 * @param groupBy			表示分组，可以为null
	 * @param having			sql语句中的having，可以为null
	 * @param orderBy			表示结果的排序，可以为null
	 * @param limit				表示分页，组拼成完整的sql语句
	 * @return
	 */
	public Cursor queryData(String table, String[] columns, String selection,
								String[] selectionArgs, String groupBy, String having,
									String orderBy, String limit)throws Exception{
		return queryData(false, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}
	
	/**
	 * 查询数据
	 * @param distinct			true if you want each row to be unique, false otherwise.
	 * @param table				表名称
	 * @param columns			查询需要返回的字段
	 * @param selection			sql语句中的条件语句
	 * @param selectionArgs		占位符的值
	 * @param groupBy			表示分组，可以为null
	 * @param having			sql语句中的having，可以为null
	 * @param orderBy			表示结果的排序，可以为null
	 * @param limit				表示分页，组拼成完整的sql语句
	 * @return
	 */
	public Cursor queryData(boolean distinct, String table, String[] columns,
								String selection, String[] selectionArgs, String groupBy,
										String having, String orderBy, String limit)throws Exception{
	    return queryData(null, distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}
	
	/**
	 * 查询数据
	 * @param cursorFactory		游标工厂
	 * @param distinct			true if you want each row to be unique, false otherwise.
	 * @param table				表名称
	 * @param columns			查询需要返回的字段
	 * @param selection			sql语句中的条件语句
	 * @param selectionArgs		占位符的值
	 * @param groupBy			表示分组，可以为null
	 * @param having			sql语句中的having，可以为null
	 * @param orderBy			表示结果的排序，可以为null
	 * @param limit				表示分页，组拼成完整的sql语句
	 * @return
	 */
	public Cursor queryData(CursorFactory cursorFactory, boolean distinct, String table,
								String[] columns, String selection, String[] selectionArgs,
									String groupBy, String having, String orderBy, String limit)throws Exception{
		Cursor cursor = null;
		if(sqliteDatabase.isOpen()){
			cursor = sqliteDatabase.queryWithFactory(cursorFactory, distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
		}else{
			throw new RuntimeException("数据库已关闭");
		}
		return cursor;
	}
	
	/**
	 * 查询数据
	 * @param sql 		执行查询操作的sql语句
	 * @param selectionArgs		查询条件
	 * @return 					返回查询的游标，可对数据自行操作，需要自己关闭游标
	 */
	public Cursor queryData2Cursor(String sql, String[] selectionArgs) throws Exception{
		Cursor cursor = null;
		if(sqliteDatabase.isOpen()){
			cursor = sqliteDatabase.rawQuery(sql, selectionArgs);
		}else{
			throw new RuntimeException("数据库已关闭");
		}
		return cursor;
	}
	
	/**
	 * 查询数据
	 * @param sql							执行查询操作的sql语句	
	 * @param selectionArgs					查询条件
	 * @param object						Object的对象
	 * @return	List<Map<String, Object>> 	返回查询结果	
	 * @throws Exception
	 */
	public List<Map<String, Object>> query2ListMap(String sql, String[] selectionArgs, Object object)throws Exception{
		List<Map<String, Object>> mList = new ArrayList<Map<String,Object>>();
		if(sqliteDatabase.isOpen()){
			Cursor cursor = null;
			try {
				cursor = sqliteDatabase.rawQuery(sql, selectionArgs);
				Field[] f;
				HashMap<String, Object> map;
				if (cursor != null && cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						map = new HashMap<String, Object>();
						f = object.getClass().getDeclaredFields();
						for (int i = 0; i < f.length; i++) {
							map.put(f[i].getName(),cursor.getString(cursor.getColumnIndex(f[i].getName())));
						}
						mList.add(map);
					}
				}
			} finally {
				cursor.close();
			}
		}else{
			throw new RuntimeException("数据库已关闭");
		}
		return mList;
	}
	
}

