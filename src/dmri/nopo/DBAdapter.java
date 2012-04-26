package dmri.nopo;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Calendar;

import android.database.Cursor;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences; //for the user-name
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
		this.context = ctx;
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
		 */
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			try{
				db.execSQL("create table " + NotificationManager.getUserStatic(context) + ".log ("+
				DBAdapter.KEY_TIME +" INTEGER primary key, "+ KEY_TEXT +" TEXT not null);");
			}
			catch (SQLException e) {
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
		return db.insert(NotificationManager.getUserStatic(context)+".log", null, initialValues);
	}
	
	public boolean deleteSMS(String time)
	{
		return db.delete(NotificationManager.getUserStatic(context)+".log", KEY_TIME + "=" + time, null) > 0;
	}
	
	/**
	 * 
	 * @return All sms given a user-login
	 */
	
	public Cursor getAllSMS()
	{
		return db.query(NotificationManager.getUserStatic(context)+".log", new String[] {KEY_TIME, KEY_TEXT}, null, null, null, null, null);
	}
	
	public boolean removeOldSMS()
	{
		StringBuffer sb = new StringBuffer(DBAdapter.getTimeStamp().substring(0,8));
	    for (int i = 0; i < 19; i++)
	      {
	        sb.append("0");
	      }
	    String value = sb.toString();
	    long compareTimeValue = Integer.parseInt(value);
		return db.delete(NotificationManager.getUserStatic(context)+".log", "KEY_TIME < "+compareTimeValue, null) > 0;
	}
	
	/**
	 * Use this timeStamp-method to get BOTH date and currentTimeMillis
	 * as a String (for storing sms)
	 * @return yyyyMMddHHmmss+timeMillis
	 */
	
	public static String getTimeStamp()
	  {
	      DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	      Calendar cal = Calendar.getInstance();
	      long timeMillis = System.currentTimeMillis();
	      return dateFormat.format(cal.getTime())+""+timeMillis;
	  }
}
