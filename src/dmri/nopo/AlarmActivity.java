package dmri.nopo;


import java.util.ArrayList;
import dmri.nopo.R;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AlarmActivity extends Activity {
    
	private ListView listView;
	private IntentFilter intentFilter;
	private Context context = this;
	
	private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
            String sms = intent.getExtras().getString("sms");
            LogManager log = LogManager.getInstance(context);
            FilterManager filter = FilterManager.getInstance(context);
            if (filter.isInLocalFiter(sms))
            {
            	log.writeLogFile(sms);
            	showSMS();
            }
            else{
            	filter.writeLocalFilter(sms, true);
            	log.writeLogFile(sms);
            	showSMS();
            }
        }
	};
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alarm);
        showSMS();
        
        
        
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");
    }
    
    
    
    public void showSMS() {
    	LogManager log = LogManager.getInstance(context);
    	NotificationManager manager = new NotificationManager(context);
    	Cursor rows = log.readXLogFile(manager.getNumberIncomingSMSInt());
    	rows.moveToFirst();
    	ArrayList<String> result = new ArrayList<String>();
    	while(rows.getPosition() < rows.getCount()) {
    		String temp = "" + rows.getString(1).substring(0, 2) + ":" + rows.getString(1).substring(2, 4) + ":" + rows.getString(1).substring(4, 6) + ":\n" + rows.getString(2);
    		result.add(temp);
    		rows.moveToNext();
    	}
    	prepareListView(result);
    }
    
    public void prepareListView(ArrayList<String> array) {
    	listView = (ListView) findViewById(R.id.alarmListView);
		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, array));
		listView.setTextFilterEnabled(true);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
    
    @Override
    protected void onResume() {
        //---register the receiver---
        registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }
    @Override
    protected void onPause() {
        //---unregister the receiver---
        // unregisterReceiver(intentReceiver);
        super.onPause();
    }

}