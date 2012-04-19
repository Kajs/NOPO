package dmri.nopo;

import dmri.nopo.R;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class LogActivity extends Activity {
    
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