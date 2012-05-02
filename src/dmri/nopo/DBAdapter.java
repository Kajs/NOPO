package dmri.nopo;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Calendar;

import android.database.Cursor;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.widget.Toast;

public class DBAdapter {
	private static final String KEY_TIME = "time";
	private static final String KEY_TEXT = "text";
	private static final String DATABASE_NAME = "NOPO";
	private static final int DATABASE_VERSION = 1;
	
	private SQLiteDatabase db;
	private DatabaseHelper DBHelper;
	private static Context context;
	
	public DBAdapter(Context ctx)
	{
		DBAdapter.context = ctx;
		this.DBHelper = new DatabaseHelper(context);
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
				String table_name = NotificationManager.getUserStatic(context)+"log";
				db.execSQL("create table "+table_name+ "("+
				KEY_TIME +" INTEGER primary key, "+ KEY_TEXT +" TEXT not null);");
				
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
				
				String table_name = NotificationManager.getUserStatic(context)+"log";
				db.execSQL("create table if not exists "+table_name+ "("+
				KEY_TIME +" INTEGER primary key, "+ KEY_TEXT +" TEXT not null);");
				
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
	
	public long insertSMS(String text)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TIME, DBAdapter.getTimeStamp());
		initialValues.put(KEY_TEXT, text);
		String table_name = NotificationManager.getUserStatic(context)+"log";
		return db.insert(table_name, null, initialValues);
	}
	
	public boolean deleteSMS(String time)
	{
		String table_name = NotificationManager.getUserStatic(context)+"log";
		return db.delete(table_name, KEY_TIME + "=" + time, null) > 0;
	}
	
	/**
	 * 
	 * @return All sms given a user-login
	 */
	
	public Cursor getAllSMS()
	{
		String table_name = NotificationManager.getUserStatic(context)+"log";
		return db.rawQuery("select * from "+table_name, null);
	
	}
	
	public Cursor getXSMS(int x) 
	{
		String table_name = NotificationManager.getUserStatic(context) +"log";
		String query = "select * from " + table_name + " order by time desc limit " + x;
		return db.rawQuery(query, null);
	}
	
	public boolean removeOldSMS()
	{
		String time = Long.toString(DBAdapter.getTimeStamp());
		StringBuffer sb = new StringBuffer(time.substring(0,8));
	    for (int i = 0; i < 19; i++)
	      {
	        sb.append("0");
	      }
	    String value = sb.toString();
	    long compareTimeValue = Integer.parseInt(value);
	    String table_name = NotificationManager.getUserStatic(context)+"log";
		return db.delete(table_name, "KEY_TIME < "+compareTimeValue, null) > 0;
	}
	
	/**
	 * Use this timeStamp-method to get BOTH date and currentTimeMillis
	 * as a long (for storing sms)
	 * @return yyyyMMddHHmmss+timeMillis
	 */
	
	public static long getTimeStamp()
	  {
	      DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	      Calendar cal = Calendar.getInstance();
	      long timeMillis = System.currentTimeMillis();
	      String time = dateFormat.format(cal.getTime())+""+timeMillis;
	      BigInteger bi = new BigInteger(time);
	      return bi.longValue();
	  }
}
