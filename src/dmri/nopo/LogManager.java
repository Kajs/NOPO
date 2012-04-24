package dmri.nopo;

import android.content.Context;
import android.database.Cursor;

public abstract class LogManager extends Context {

	private DBAdapter db;
	
	public LogManager(){
		db = new DBAdapter(this);
	}
	/**
	 * Reads all sms from database
	 * @return A Cursor: c.moveToFirst(); c.moveToNext(); c.getString(0); c.getString(1);
	 */
	public Cursor readLogFile()
	{
		db.open();
		Cursor c = db.getAllSMS();
		db.close();
		return c;
	}
	
	public void writeLogFile(String sms)
	{
		db.open();
		db.insertSMS(sms);
		db.close();
	}
	
	public boolean removeOldSMS()
	{
		db.open();
		return db.removeOldSMS();
	}

}
