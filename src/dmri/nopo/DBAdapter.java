package dmri.nopo;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Calendar;
import android.database.Cursor;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBAdapter {
	private static final String KEY_ID = "id";
	private static final String KEY_TIME = "time";
	private static final String KEY_TEXT = "text";
	private static final String KEY_RECEIVE = "receive";
	private static final String KEY_VIBRATION = "vibration";
	private static final String KEY_SOUND = "sound";
	private static final String KEY_HIGHLIGHTTIME = "highligthTime";
	private static final String KEY_NUMBERALARMS = "numberAlarms";
	private static final String KEY_SETTING = "setting";
	private static final String KEY_SETTINGVALUE = "settingValue";
	private static final String KEY_LASTUSER = "lastUser";
	private static final String KEY_RECEIVENUMBER = "receiveNumber";
	private static final String DATABASE_NAME = "NOPO";
	private static final int DATABASE_VERSION = 1;
	private static String log_table;
	private static String filter_table;
	private static String user_table;
	private static final String application_table = "applicationSettings";
	
	private SQLiteDatabase db;
	private DatabaseHelper DBHelper;
	private static Context context;
	
	private static DBAdapter instance;
	
	private DBAdapter(Context ctx)
	{
		DBAdapter.context = ctx;
		prepareTableNames();
		this.DBHelper = new DatabaseHelper(context);
	}
	
	public static DBAdapter getInstance(Context context){
		if (DBAdapter.instance == null)
		{
			instance = new DBAdapter(context);
		}
		return instance;
	}	
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context)
		{
			super(context, DBAdapter.DATABASE_NAME, null, DBAdapter.DATABASE_VERSION);
		}
		
		/**
		 * A tablename for incoming sms is based on the user's login-id.
		 * onCreate called when the db does not exist
		 */
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			try{
				prepareTableNames();
				db.execSQL("create table if not exists "+log_table+ "("+KEY_ID+" INTEGER primary key autoincrement, "+
						KEY_TIME +" INTEGER, "+ KEY_TEXT +" TEXT not null);");
				db.execSQL(
						"create table if not exists "+filter_table+"("+KEY_TEXT+" TEXT primary key, "+ 
						KEY_RECEIVE+" INTEGER not null);");
				db.execSQL("CREATE TABLE if not exists "+user_table+"("+KEY_SETTING+" TEXT PRIMARY KEY, "+
						KEY_SETTINGVALUE+" INTEGER not null);");
				db.execSQL("CREATE TABLE if not exists "+application_table+"("+KEY_SETTING+" TEXT PRIMARY KEY, "+
						KEY_SETTINGVALUE+" TEXT not null);");
				
			}
			catch (SQLException e) {
				Log.w("Database", "onCreate: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		/**
		 * onOpen method called when app is opening.
		 */
		
		@Override
		public void onOpen(SQLiteDatabase db)
		{
			try{
				prepareTableNames();
				db.execSQL("create table if not exists "+log_table+ "(id INTEGER primary key, "+
						KEY_TIME +" INTEGER, "+ KEY_TEXT +" TEXT not null);");
				db.execSQL("create table if not exists "+filter_table+"("+KEY_TEXT+" TEXT primary key, "+ 
						KEY_RECEIVE+" INTEGER not null);");	
				db.execSQL("CREATE TABLE if not exists "+user_table+"("+KEY_SETTING+" TEXT PRIMARY KEY, "+
						KEY_SETTINGVALUE+" INTEGER not null);");
				db.execSQL("CREATE TABLE if not exists "+application_table+"("+KEY_SETTING+" TEXT PRIMARY KEY, "+
						KEY_SETTINGVALUE+" TEXT not null);");
			}
			catch (SQLException e){
				Log.w("Database", "onOpen: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			Log.w("DatabaseAdapter", "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
		}	
	}
	
	public DBAdapter open() throws SQLException
	{
		try {
			this.db = DBHelper.getWritableDatabase();		
			return this;
		} catch (Exception e) {
			Log.w("Database", "open: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public void close()
	{
		DBHelper.close();
	}
	
	/**
	 * 
	 * @return null SQLException has occured
	 */
	
	public void setReceiveNumber(String receiveNumber) {
		try {
			db.execSQL("INSERT or REPLACE into "+application_table+" VALUES('"+KEY_RECEIVENUMBER+"', '"+receiveNumber+"');");
		} catch (SQLException e) {
			Log.w("Database", "setReceiveNumber: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void setLastUser(String lastUser) {
		try {
			db.execSQL("INSERT or REPLACE into "+application_table+" VALUES('"+KEY_LASTUSER+"', '"+lastUser+"');");
		} catch (SQLException e) {
			Log.w("Database", "setLastUser: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public Cursor getReceiveNumber() {
		try {
			Cursor storedNumber = db.rawQuery("SELECT "+KEY_SETTINGVALUE+" FROM "+application_table+" WHERE "+KEY_SETTING+" = '"+KEY_RECEIVENUMBER+"';", null);
			storedNumber.moveToFirst();
			return storedNumber;
		} catch (Exception e) {
			Log.w("Database", "getReceiveNumber: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public Cursor getLastUser() {
		try {
			Cursor storedUser = db.rawQuery("SELECT "+KEY_SETTINGVALUE+" FROM "+application_table+" WHERE "+KEY_SETTING+" = '"+KEY_LASTUSER+"';", null);
			storedUser.moveToFirst();
			return storedUser;
		} catch (Exception e) {
			Log.w("Database", "getLastUser: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public Cursor getUserSettings() {
		try{
			Cursor cr = db.rawQuery("Select * from "+user_table+";", null);
			cr.moveToFirst();
			return cr;
		}
		catch(SQLException e)
		{
			Log.w("Database", "getUserSettings: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public void updateUserSettings(int vibration, int sound, int highlightTime, int numberAlarms) {
		try{
			db.execSQL("INSERT or REPLACE into "+user_table+" VALUES('"+KEY_VIBRATION+"', "+vibration+");");
			db.execSQL("INSERT or REPLACE into "+user_table+" VALUES('"+KEY_SOUND+"', "+sound+");");
			db.execSQL("INSERT or REPLACE into "+user_table+" VALUES('"+KEY_HIGHLIGHTTIME+"', "+highlightTime+");");
			db.execSQL("INSERT or REPLACE into "+user_table+" VALUES('"+KEY_NUMBERALARMS+"', "+numberAlarms+");");
		}
		catch (SQLException e){
			Log.w("testingDatabase", "updateUserSettings: " +e.getMessage());
			e.printStackTrace();
		}
	}

	public void deleteUser() {
		try {
			db.delete(log_table, null, null);
			db.delete(filter_table, null, null);
			db.delete(user_table, null, null);
		} catch (Exception e) {
			Log.w("Database", "deleteUser: " +e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * The last return true happens only, if an sqlexception has occured.
	 * @param text The filter-type
	 */
	
	public boolean isInLocalFilter(String text)
	{
		try {
			Cursor cr = db.rawQuery("SELECT * FROM "+filter_table+" WHERE text = '"+text+"'", null);
			cr.moveToFirst();
			if (cr.getCount() == 0)
			{
				writeLocalFilter(text);
				return true;
			}
			if(cr.getInt(1) == 1) {
				return true;
			}
			else return false;
		}
		catch (SQLException e)
		{
			Log.w("Database", "isInLocalFilter: " +e.getMessage());
			e.printStackTrace();
		}
		return true;
	}
	
	public Cursor readLocalFilter()
	{
		try{
			Cursor cr = db.rawQuery("Select * from "+filter_table+" order by "+KEY_TEXT+" ASC", null);
			cr.moveToFirst();
			return cr;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * insert a filter item	
	 * @param the text you see on filteractivity
	 */
	public void writeLocalFilter(String text){
		try{
			db.execSQL("INSERT INTO "+filter_table+" ("+KEY_TEXT+", "+KEY_RECEIVE+") VALUES('"+text+"', 1);");
		}
		catch(SQLException e)
		{
			Log.w("Database", "writeLocalFilter: " +e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void updateLocalFilter(String sms, boolean receive) {
		try {
			if(receive == true) {
				db.execSQL("INSERT or REPLACE into "+filter_table+" VALUES('"+sms+"', '1');");
			}
			else {
				db.execSQL("INSERT or REPLACE into "+filter_table+" VALUES('"+sms+"', '0');");
			}
		} catch (SQLException e) {
			Log.w("Database", "updateLocalFilter: " +e.getMessage());
			e.printStackTrace();
		}
	}
	
	public Cursor getXFilter(String text) {
		try {
			Cursor cr = db.rawQuery("SELECT * FROM " + filter_table + " WHERE " + KEY_TEXT + " LIKE '%" + text + "%'", null);
			cr.moveToFirst();
			return cr;
		} catch (Exception e) {
			Log.w("Database", "getXFilter: " +e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public void insertSMS(String text)
	{
		try {
			ContentValues initialValues = new ContentValues();
			initialValues.put(KEY_TIME, DBAdapter.getDateStamp());
			initialValues.put(KEY_TEXT, text);
			db.execSQL("INSERT INTO "+log_table+" ("+KEY_TIME+", "+KEY_TEXT+") VALUES("+DBAdapter.getDateStamp()+", '"+text+"');");
		} catch (SQLException e) {
			Log.w("Database", "insertSMS: " +e.getMessage());
			e.printStackTrace();
		} 
	}
	
	/**
	 * 
	 * @return All sms given a user-login
	 */
	
	public Cursor getAllSMS()
	{
		try {
			Cursor cr = db.rawQuery("select * from "+log_table + " order by id desc", null);
			cr.moveToFirst();
			return cr;
		} catch (Exception e) {
			Log.w("Database", "getAllSMS: " +e.getMessage());
			e.printStackTrace();
			return null;
		}
	
	}
	
	public Cursor getXSMS(int x) 
	{
		String query = "select * from " + log_table + " order by id desc limit " + x;
		try {
			Cursor cr = db.rawQuery(query, null);
			cr.moveToFirst();
			return cr;
		} catch (Exception e) {
			Log.w("Database", "getXSMS: " +e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public Cursor getXUnblockedSMS(int x)
	{
		String query = "select * from " + log_table + " where "+ KEY_TEXT + " in (select "+KEY_TEXT+
				" from "+ filter_table + " where "+KEY_RECEIVE+"=1) order by id desc limit "+x;
		try {
			Cursor cr = db.rawQuery(query, null);
			cr.moveToFirst();
			return cr;
		} catch (Exception e) {
			Log.w("Database", "getXUnblockedSMS: " +e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean deleteSMS(String time)
	{
		return db.delete(log_table, KEY_TIME + "=" + time, null) > 0;
	}
	
	public void removeOldSMS()
	{
		try {
			long rawTime = DBAdapter.getDateStamp();
			String croppedTime = Long.toString(rawTime).substring(0,8);
			String finalCroppedTime = croppedTime + "000000";
			db.delete(log_table, KEY_TIME + " < " + finalCroppedTime, null);
		} catch (Exception e) {
			Log.w("Database", "removeOldSMS: " +e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Use this timeStamp-method to get BOTH date and currentTimeMillis
	 * as a long (for storing sms)
	 * @return yyyyMMddHHmmss+timeMillis
	 */
	
	public static long getDateStamp()
	  {
	      DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	      Calendar cal = Calendar.getInstance();
	      String time = dateFormat.format(cal.getTime());
	      long longvalue = new Long(time);
	      return longvalue;
	  }
	
	static void prepareTableNames() {
		String userName = SettingsManager.userName;
		log_table = userName + "log";
		filter_table = userName + "filter";
		user_table = userName + "settings";
	}
}