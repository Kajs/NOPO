package nopo.view;


import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.widget.TextView;

public class AlarmView extends Activity {
    
	private Button logButton;
	private Button filterButton;
	private Button menuButton;
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
        setContentView(R.layout.main);
        
        this.logButton = (Button) this.findViewById(R.id.log);
        this.logButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
            	Intent intent = new Intent("android.intent.action.LOG");
                startActivity(intent);
            }
        });
        this.filterButton = (Button) this.findViewById(R.id.filter);
        this.filterButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
            	Intent intent = new Intent("android.intent.action.FILTER");
                startActivity(intent);
            }
        });
        this.menuButton = (Button) this.findViewById(R.id.menu);
        this.menuButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
            	Intent intent = new Intent("android.intent.action.MENU");
                startActivity(intent);
            }
        });
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