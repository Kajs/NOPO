package dmri.nopo;

import android.content.Context;
import android.database.Cursor;

public class LogManager{

	private DBAdapter db;
	private static LogManager instance;
	
	
	private LogManager(Context context){
		db = DBAdapter.getInstance(context);
		db.open();
	}
	
	public static LogManager getInstance(Context context)
	{
		if (LogManager.instance == null)
	    {
			LogManager.instance = new LogManager(context);
	    }
	    return LogManager.instance;
	}
	/**
	 * Reads all sms from database
	 * @return A Cursor: c.moveToFirst(); c.moveToNext(); c.getString(0); c.getString(1);
	 */
	public Cursor getAllSMS()
	{
		Cursor c = db.getAllSMS();
		return c;
	}
	
	public Cursor getXSMS(int x) 
	{
		return db.getXSMS(x);
	}
	
	public Cursor getXUnblockedSMS(int x)
	{
		return db.getXUnblockedSMS(x);
	}
	
	public void writeLogFile(String sms)
	{
		db.insertSMS(sms);
	}
	
	public void removeOldSMS()
	{
		db.removeOldSMS();
	}

}
