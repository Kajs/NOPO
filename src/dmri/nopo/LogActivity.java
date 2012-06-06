package dmri.nopo;

import java.util.ArrayList;
import dmri.nopo.R;
import android.app.Activity;
import android.os.Bundle;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LogActivity extends Activity {
    
	private ListView showLog;
	private ArrayList<String> log;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log);
    }
    
    public void getLog() {
    	LogManager logMan = LogManager.getInstance(this);
        Cursor c = logMan.getAllSMS();
        log = new ArrayList<String>();
        int size = c.getCount();
        while(c.getPosition() < size) {
        	String row = "" + c.getString(1).substring(8, 10) + ":" + 
        					  c.getString(1).substring(10, 12) + ":" + 
        					  c.getString(1).substring(12, 14) + "-" + 
        					  c.getString(1).substring(6, 8) + "/" + 
        					  c.getString(1).substring(4, 6) + "/" + 
        					  c.getString(1).substring(0, 4) + "\n" + 
        					  c.getString(2);
        	log.add(row);
        	c.moveToNext();
        }
    }
    
    public void showLog() {
    	showLog = (ListView) findViewById(R.id.logListView);
    	showLog.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, log));    	
    }
    
    @Override
    protected void onResume() {
    	if(SettingsManager.pendingUnregister) {
    		finish();
    	}
    	else {
    		ViewChangeActivity.colorButtonsViaArray(1);
        	getLog();
            showLog();
    	}
    	super.onResume();
    }
    
}