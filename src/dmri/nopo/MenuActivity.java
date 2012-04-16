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
    
	private Button alarmButton;
	private Button logButton;
	private Button filterButton;
	private Button saveButton;
	private Button logoutButton;
	private TextView username;
	private SeekBar vibroBar;
	private SeekBar lydBar;
	private SeekBar lysBar;
	
    private SharedPreferences pref;
	private Editor editor;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.menu);
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
        this.filterButton = (Button) this.findViewById(R.id.filter);
        this.filterButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
            	Intent intent = new Intent("android.intent.action.FILTER");
                startActivity(intent);
            }
        });
        
        
        pref = getSharedPreferences("NOPOPref", MODE_PRIVATE);
        editor = pref.edit();
        
        username = (TextView) findViewById(R.id.userlogin);
        username.setText(pref.getString("user", "No username."));
        
        vibroBar = (SeekBar) findViewById(R.id.vibrobar);
        int vibroValue = pref.getInt("vibroValue", 50);
        vibroBar.setProgress(vibroValue);
        
        lydBar = (SeekBar) findViewById(R.id.lydbar);
        int lydValue = pref.getInt("lydValue", 50);
        lydBar.setProgress(lydValue);
        
        lysBar = (SeekBar) findViewById(R.id.lysbar);
        int lysValue = pref.getInt("lysValue", 50);
        lysBar.setProgress(lysValue);
   
        this.saveButton = (Button) findViewById(R.id.saveButton);
        this.saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				int newVib = vibroBar.getProgress();
				int newLyd = lydBar.getProgress();
				int newLys = lysBar.getProgress();
				editor.putInt("vibroValue", newVib);
				editor.putInt("lydValue", newLyd);
				editor.putInt("lysValue", newLys);
				editor.commit();
			}
		});
        
        this.logoutButton = (Button) findViewById(R.id.sletloginButton);
        this.logoutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
                editor.remove("user");
                editor.remove("pass");
                editor.commit();
                startActivity(new Intent("android.intent.action.MENU"));
			}
		});
    }
}