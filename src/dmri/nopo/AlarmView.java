package dmri.nopo;


import dmri.nopo.R;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.widget.TextView;

public class AlarmView extends Activity {
    
	private TextView showSMS1;
	private IntentFilter intentFilter;
	
	private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
				showSMS1 = (TextView) findViewById(R.id.SMSdisplay1);
				showSMS1.setText(intent.getExtras().getString("sms"));
			}
	};
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alarm);
        
        
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");
    }
    
    @Override
    protected void onResume() {
    	registerReceiver(intentReceiver, intentFilter);
    	super.onResume();
    }
    
    @Override
    protected void onPause() {
    	unregisterReceiver(intentReceiver);
    	super.onPause();
    }
}