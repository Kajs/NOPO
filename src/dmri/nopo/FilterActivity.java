package dmri.nopo;

import dmri.nopo.R;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.view.View;
import android.view.Window;

public class FilterActivity extends Activity {
    
	private Button alarmButton;
	private Button logButton;
	private Button menuButton;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.filter);
        this.alarmButton = (Button) this.findViewById(R.id.alarm);
        this.alarmButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
            	Intent intent = new Intent("android.intent.action.ALARM");
                startActivity(intent);
            }
        });
        this.logButton = (Button) this.findViewById(R.id.log);
        this.logButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
            	Intent intent = new Intent("android.intent.action.LOG");
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
    }
}
