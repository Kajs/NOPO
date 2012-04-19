package dmri.nopo;


import dmri.nopo.R;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.widget.TextView;

public class AlarmActivity extends Activity {
    
	private TextView showSMS1;
	private IntentFilter intentFilter;
	
	private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
            showSMS1 = (TextView) findViewById(R.id.SMSdisplay1);
            String sms = "" + intent.getExtras().getString("sender") + "\n" + intent.getExtras().getString("sms");
            showSMS1.setText(sms);
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
        //---register the receiver---
        registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }
    @Override
    protected void onPause() {
        //---unregister the receiver---
        unregisterReceiver(intentReceiver);
        super.onPause();
    }

}