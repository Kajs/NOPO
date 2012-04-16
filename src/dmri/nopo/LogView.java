package dmri.nopo;

import dmri.nopo.R;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class LogView extends Activity {
    
	private Button alarmButton;
	private Button filterButton;
	private Button menuButton;
	private TextView showInput;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.log);
        this.alarmButton = (Button) this.findViewById(R.id.alarm);
        this.alarmButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
            	Intent intent = new Intent("android.intent.action.ALARM");
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
        showInput = (TextView) findViewById(R.id.showInput);
        showInput.setText("testing");
        String input = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	input = extras.getString("data");
        	showInput.setText(input);
        }
    }
}