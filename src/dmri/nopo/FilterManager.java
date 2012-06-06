 package dmri.nopo;

import android.content.Context;
import android.database.Cursor;

public class FilterManager {
	
	private static FilterManager instance;
	private Context context;
	
	
	private FilterManager(Context ctx){
		context = ctx;		
	}
	
	public static FilterManager getInstance(Context context)
	{
		if (instance == null)
	    {
			instance = new FilterManager(context);
	    }
	    return FilterManager.instance;
	}
	
	
	public Cursor getLocalFilter()
	{
		DBAdapter db = DBAdapter.getInstance(context);
		db.open();
		return db.readLocalFilter();		
	}
	
	public boolean isInLocalFilter(String sms)
	{
		DBAdapter db = DBAdapter.getInstance(context);
		db.open();
		return db.isInLocalFilter(sms);
	}
	
	public void updateLocalFilter(String sms, Boolean bool) {
		DBAdapter db = DBAdapter.getInstance(context);
		db.open();
		db.updateLocalFilter(sms, bool);
	}
	
	public Cursor getXFilter(String text) {
		DBAdapter db = DBAdapter.getInstance(context);
		db.open();
		return db.getXFilter(text);
	}
}
