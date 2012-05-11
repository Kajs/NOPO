package dmri.nopo;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

public class LogManager{

	private DBAdapter db;
	private static LogManager instance;
	
	
	private LogManager(Context context){
		db = new DBAdapter(context);
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
	public Cursor readLogFile()
	{
		
		db.open();
		Cursor c = db.getAllSMS();
		return c;
	}
	
	public Cursor readXLogFile(int x) 
	{
		db.open();
		Cursor c = db.getXSMS(x);
		return c;
	}
	
	public void writeLogFile(String sms)
	{
		db.open();
		db.insertSMS(sms);
	}
	
	public boolean removeOldSMS()
	{
		db.open();
		return db.removeOldSMS();
	}

}
