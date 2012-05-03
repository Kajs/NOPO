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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AlarmActivity extends Activity {
    
	private ListView listView;
	private static ArrayList<String> smsArray;
	private IntentFilter intentFilter;
	private Context context = this;
	
	private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
            String sms = "" + intent.getExtras().getString("sender") + "\n" + intent.getExtras().getString("sms");
            handleSms(sms, 6);
            prepareListView();
        }
	};
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alarm);
        
        if (smsArray == null) { 
        	smsArray = new ArrayList<String>(); 
        	smsArray.add("first alert"); 
        	smsArray.add("second alert");
        	}
        
        prepareListView();
        
        LogManager log = LogManager.getInstance(context);
        log.writeLogFile("dette er en test 1");        
        log.writeLogFile("dette er en test 2");
        log.writeLogFile("dette er en test 3");
        log.writeLogFile("dette er en test 4");
        log.writeLogFile("dette er en test 5");
        log.writeLogFile("dette er en test 6");
        log.writeLogFile("dette er en test 7");
        log.writeLogFile("dette er en test 8");
        
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");
}
    
    public void handleSms(String sms, int maxSize) {
    	int size = smsArray.size();
    	if (size == maxSize) {
    		int counter = size - 1;
    		while (counter > 0) {
    			smsArray.set(counter, smsArray.get(counter - 1));
    			counter = counter - 1;
    		}
    		smsArray.set(0,  sms);
    	}
    	else {
    		smsArray.add(sms);
    		int counter = size;
    		while (counter > 0) {
    			smsArray.set(counter, smsArray.get(counter - 1));
    			counter = counter - 1;
    		}
    		smsArray.set(0, sms);
    	}
    }
    
    public void prepareListView() {
    	listView = (ListView) findViewById(R.id.alarmListView);
		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, smsArray));
		listView.setTextFilterEnabled(true);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setItemChecked(1, true);
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