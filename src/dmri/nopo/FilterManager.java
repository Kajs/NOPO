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
	
	public void updateLocalFilter(String sms, Boolean bool) {
		db.open();
		db.updateLocalFilter(sms, bool);
	}
	
	public Cursor getXFilter(String text) {
		db.open();
		return db.getXFilter(text);
	}
}
