 package dmri.nopo;

import android.content.Context;
import android.database.Cursor;

public class FilterManager {
	
	private DBAdapter db;
	private static FilterManager instance;
	
	
	private FilterManager(Context context){
		db = new DBAdapter(context);
	}
	
	public static FilterManager getInstance(Context context)
	{
		if (FilterManager.instance == null)
	    {
			FilterManager.instance = new FilterManager(context);
	    }
	    return FilterManager.instance;
	}
	
	
	public Cursor getLocalFilter()
	{
		db.open();
		return db.readLocalFilter();
		
	}
	public boolean isInLocalFiter(String sms)
	{
		db.open();
		return db.isInLocalFilter(sms);
	}
	public void writeLocalFilter(String sms, boolean receive)
	{
		db.open();
		db.writeLocalFilter(sms, receive);
		
	}
	public void serverFilterWriter()
	{
		
	}
}
