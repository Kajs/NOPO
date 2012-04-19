package dmri.nopo;

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
	private static final String DATABASE_NAME = "SMS_Storage";
	private static final int DATABASE_VERSION = 1;
	
	private SQLiteDatabase db;
	private DatabaseHelper DBHelper;
	private final Context context;
	
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
				db.execSQL("create table " + getUserName() + " ("+
				DBAdapter.KEY_TIME +" TEXT primary key, "+ KEY_TEXT +" TEXT not null);");
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
		initialValues.put(KEY_TIME, "strftime('%Y-%m-%d %H:%M:%f', 'now'");
		initialValues.put(KEY_TEXT, text);
		return db.insert(getUserName(), null, initialValues);
	}
	
	public boolean deleteSMS(String time)
	{
		return db.delete(getUserName(), KEY_TIME + "=" + time, null) > 0;
	}
	
	/**
	 * 
	 * @return All sms given a user-login
	 */
	
	public Cursor getAllSMS()
	{
		return db.query(getUserName(), new String[] {KEY_TIME, KEY_TEXT}, null, null, null, null, null);
	}
}
