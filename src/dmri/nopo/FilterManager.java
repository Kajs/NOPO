 package dmri.nopo;

import android.content.Context;
import android.database.Cursor;

public class FilterManager {
	
	private DBAdapter db;
	private static FilterManager instance;
	
	
	private FilterManager(Context context){
		db = DBAdapter.getInstance(context, SettingsManager.userName);
		db.open();
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
		return db.readLocalFilter();		
	}
	
	public boolean isInLocalFilter(String sms)
	{
		return db.isInLocalFilter(sms);
	}
	
	public void updateLocalFilter(String sms, Boolean bool) {
		db.updateLocalFilter(sms, bool);
	}
	
	public Cursor getXFilter(String text) {
		db.open();
		return db.getXFilter(text);
	}
}
