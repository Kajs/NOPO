
package dmri.nopo;

import dmri.nopo.R;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Button;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.SeekBar;

public class MenuActivity extends Activity {
    
	private Button saveButton;
	private Button logoutButton;
	private Button testButton;
	private TextView username;
	private TextView testView;
	private SeekBar vibroBar;
	private SeekBar lydBar;
	private SeekBar lysBar;
	
    private NotificationManager manager;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.menu);
        manager = new NotificationManager(this);
        
        username = (TextView) findViewById(R.id.userlogin);
        username.setText(manager.getUser());
        
        vibroBar = (SeekBar) findViewById(R.id.vibrobar);
        vibroBar.setProgress(manager.getUserVibration());
        
        lydBar = (SeekBar) findViewById(R.id.lydbar);
        lydBar.setProgress(manager.getUserSound());
        
        lysBar = (SeekBar) findViewById(R.id.lysbar);
        lysBar.setProgress(manager.getUserLight());
   
        this.saveButton = (Button) findViewById(R.id.saveButton);
        this.saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				int newVib = vibroBar.getProgress();
				int newSou = lydBar.getProgress();
				int newLig = lysBar.getProgress();
				manager.setNotificationAndroid(newVib, newSou, newLig);
			}
		});
        
        this.logoutButton = (Button) findViewById(R.id.sletloginButton);
        this.logoutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences pref = getSharedPreferences("NOPOPref", MODE_PRIVATE);
            	SharedPreferences.Editor editor = pref.edit();
            	editor.remove("user");
            	editor.remove("pass");
            	editor.commit();
                startActivity(new Intent("android.intent.action.MENU"));
			}
		});
        
        this.testView = (TextView) findViewById(R.id.testView);
        
        this.testButton = (Button) findViewById(R.id.testButton);
        this.testButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
                testView.setText(manager.getUserVibration());
                
			}
		});
    }
}