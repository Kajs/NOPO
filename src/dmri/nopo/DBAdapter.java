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
	private static final String DATABASE_NAME = "NOPO";
	private static final int DATABASE_VERSION = 1;
	private static String log_table;
	private static String filter_table;
	
	private SQLiteDatabase db;
	private DatabaseHelper DBHelper;
	private static Context context;
	
	private static DBAdapter instance;
	
	private DBAdapter(Context ctx)
	{
		DBAdapter.context = ctx;
		log_table = LoginActivity.userName+"log";
		filter_table =  LoginActivity.userName+"filter";
		this.DBHelper = new DatabaseHelper(context);

	}
	
	public static DBAdapter getInstance(Context context){
		if (DBAdapter.instance == null)
		{
			DBAdapter.instance = new DBAdapter(context);
		}
		return DBAdapter.instance;
	}
	
	public static void updateTableNames(String logTable, String filterTable) {
		log_table = logTable;
		filter_table = filterTable;		
	}
	
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context)
		{
			super(context, DBAdapter.DATABASE_NAME, null, DBAdapter.DATABASE_VERSION);
			log_table = LoginActivity.userName+"log";
			filter_table =  LoginActivity.userName+"filter";
		}
		
		/**
		 * A tablename for incoming sms is based on the user's login-id.
		 * onCreate called when the db does not exist
		 */
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			try{
				db.execSQL("create table "+log_table+ "("+KEY_ID+" INTEGER primary key auto increment, "+
						KEY_TIME +" INTEGER, "+ KEY_TEXT +" TEXT not null);");
				db.execSQL(
						"create table "+filter_table+"("+KEY_TEXT+" TEXT primary key, "+ 
						KEY_RECEIVE+" INTEGER not null);");	
			}
			catch (SQLException e) {
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
				db.execSQL("create table if not exists "+log_table+ "(id INTEGER primary key, "+
						KEY_TIME +" INTEGER, "+ KEY_TEXT +" TEXT not null);");
				db.execSQL("create table if not exists "+filter_table+"("+KEY_TEXT+" TEXT primary key, "+ 
						KEY_RECEIVE+" INTEGER not null);");		
			}
			catch (SQLException e){
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
		this.db = DBHelper.getWritableDatabase();
		
		return this;
	}
	
	public void close()
	{
		DBHelper.close();
	}
	
	/**
	 * 
	 * @return null SQLException has occured
	 */
	
	public Cursor readLocalFilter()
	{
		try{
			Cursor cr = db.rawQuery("Select * from "+filter_table+" order by "+KEY_TEXT+" ASC", null);
			return cr;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * The last return true happens only, if an sqlexception has occured.
	 * @param text The filter-type
	 */
	
	public boolean isInLocalFilter(String text)
	{
		try {
			Cursor cr = db.rawQuery("SELECT * FROM "+filter_table+" WHERE text = '"+text+"'", null);
			if (cr.getCount() == 0)
			{
				writeLocalFilter(text);
				return true;
			}
			cr.moveToFirst();
			if(cr.getInt(1) == 1) {
				return true;
			}
			else return false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return true;
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
			e.printStackTrace();
		}
	}
	
	public void updateLocalFilter(String sms, boolean receive) {
		if(receive == true) {
			db.execSQL("UPDATE " + filter_table + " SET " + KEY_RECEIVE + "=1 WHERE " + KEY_TEXT +"='" + sms+"'");
		}
		else {
			db.execSQL("UPDATE " + filter_table + " SET " + KEY_RECEIVE + "=0 WHERE " + KEY_TEXT +"='" + sms+"'");
		}
	}
	
	public Cursor getXFilter(String text) {
		return db.rawQuery("SELECT * FROM " + filter_table + " WHERE " + KEY_TEXT + " LIKE '%" + text + "%'", null);
	}
	
	public void insertSMS(String text)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TIME, DBAdapter.getDateStamp());
		initialValues.put(KEY_TEXT, text);
		db.execSQL("INSERT INTO "+log_table+" ("+KEY_TIME+", "+KEY_TEXT+") VALUES("+DBAdapter.getDateStamp()+", '"+text+"');"); 
	}
	
	public boolean deleteSMS(String time)
	{
		return db.delete(log_table, KEY_TIME + "=" + time, null) > 0;
	}
	
	/**
	 * 
	 * @return All sms given a user-login
	 */
	
	public Cursor getAllSMS()
	{
		return db.rawQuery("select * from "+log_table + " order by id desc", null);
	
	}
	
	public Cursor getXSMS(int x) 
	{
		String query = "select * from " + log_table + " order by id desc limit " + x;
		return db.rawQuery(query, null);
	}
	
	public Cursor getXUnblockedSMS(int x)
	{
		String query = "select * from " + log_table + " where "+ KEY_TEXT + " in (select "+KEY_TEXT+
				" from "+ filter_table + " where "+KEY_RECEIVE+"=1) order by id desc limit "+x;
		return db.rawQuery(query, null);
	}
	
	public boolean removeOldSMS()
	{
		String time = Long.toString(DBAdapter.getDateStamp()).substring(5,13);
	    int compareTimeValue = Integer.parseInt(time);
		return db.delete(log_table, "KEY_TIME < "+compareTimeValue, null) > 0;
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
}