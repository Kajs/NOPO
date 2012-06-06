package dmri.nopo;

import android.content.Context;
import android.database.Cursor;

public class LogManager{

	private static LogManager instance;
	private Context context;
	
	
	private LogManager(Context ctx){
		context = ctx;
	}
	
	public static LogManager getInstance(Context context)
	{
		if (instance == null)
	    {
			instance = new LogManager(context);
	    }
	    return instance;
	}
	/**
	 * Reads all sms from database
	 * @return A Cursor: c.moveToFirst(); c.moveToNext(); c.getString(0); c.getString(1);
	 */
	public Cursor getAllSMS()
	{
		DBAdapter db = DBAdapter.getInstance(context);
		db.open();
		Cursor c = db.getAllSMS();
		return c;
	}
	
	public Cursor getXSMS(int x) 
	{
		DBAdapter db = DBAdapter.getInstance(context);
		db.open();
		return db.getXSMS(x);
	}
	
	public Cursor getXUnblockedSMS(int x)
	{
		DBAdapter db = DBAdapter.getInstance(context);
		db.open();
		return db.getXUnblockedSMS(x);
	}
	
	public void writeLogFile(String sms)
	{
		DBAdapter db = DBAdapter.getInstance(context);
		db.open();
		db.insertSMS(sms);
	}
	
	public void removeOldSMS()
	{
		DBAdapter db = DBAdapter.getInstance(context);
		db.open();
		db.removeOldSMS();
	}

}
