package dmri.nopo;

import java.util.ArrayList;
import dmri.nopo.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.DialogInterface;
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
    	logMan.removeOldSMS();
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
    	ViewChangeActivity.colorButtonsViaArray(1);
    	if (SettingsManager.pendingClose) {
    		finish();
		} else if (SettingsManager.pendingMinimize) {
			moveTaskToBack(true);
		} else {
        	getLog();
            showLog();
		}
    	super.onResume();
    }
    
    @Override
    public void onBackPressed() {
  	  AlertDialog alertDialog = new AlertDialog.Builder(this).create();
  	  alertDialog.setTitle("Advarsel");
  	  alertDialog.setMessage("Hvis du vaelger luk, vil programmet ikke modtage alarmer");
  	  alertDialog.setButton(-1, "Luk", new DialogInterface.OnClickListener() {
  	     public void onClick(DialogInterface dialog, int which) {
  	    	 SettingsManager.keepLoggedIn = false;
  	    	 SettingsManager.pendingClose = true;
  	    	 finish();
  	     }
  	  });
  	  alertDialog.setButton(-2, "Annuller", new DialogInterface.OnClickListener() {
      	 public void onClick(DialogInterface dialog, int which) {
      	 }
  	  });
  	  alertDialog.setButton(-3, "Minimer", new DialogInterface.OnClickListener() {
  	     public void onClick(DialogInterface dialog, int which) {
  	    	 SettingsManager.pendingMinimize = true;
  	    	 moveTaskToBack(true);
  	     }
  	  });
  	  alertDialog.show();
    }
    
}