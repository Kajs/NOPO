package dmri.nopo;


import dmri.nopo.R;
import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Button;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;


public class MenuActivity extends Activity {
    
	private Button saveButton;
	private Button logoutButton;
	private TextView username;
	private SeekBar vibroBar;
	private SeekBar lydBar;
	private SeekBar lysBar;
	private Spinner highlightChooser;
	private Spinner numberIncChooser;
	private Context context;
	private EditText number;
	
    private NotificationManager manager;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.menu);
        this.context = this;
        manager = NotificationManager.getInstance(this);
        
        ArrayAdapter<CharSequence> highlightAdapter = ArrayAdapter.createFromResource(this, R.array.highlightNames, 
        		android.R.layout.simple_spinner_item);
        highlightChooser = (Spinner) findViewById(R.id.highlightSpinner);
        highlightChooser.setAdapter(highlightAdapter);
        int highlightPosition = highlightAdapter.getPosition(LoginActivity.highlightTime + " min");
        highlightChooser.setSelection(highlightPosition);
        
        ArrayAdapter<CharSequence> SMSAdapter = ArrayAdapter.createFromResource(this, R.array.numberSMSNames, 
        		android.R.layout.simple_spinner_item);
        numberIncChooser = (Spinner) findViewById(R.id.numberSMSSpinner);
        numberIncChooser.setAdapter(SMSAdapter);
        int smsPosition = SMSAdapter.getPosition(LoginActivity.showNumberIncomingSMS + " sms");
        numberIncChooser.setSelection(smsPosition);
        
        username = (TextView) findViewById(R.id.userlogin);
        username.setText(LoginActivity.userName);
        
        vibroBar = (SeekBar) findViewById(R.id.vibrobar);
        vibroBar.setProgress(LoginActivity.vibration);
        
        lydBar = (SeekBar) findViewById(R.id.lydbar);
        lydBar.setProgress(LoginActivity.sound);
        
        lysBar = (SeekBar) findViewById(R.id.lysbar);
        lysBar.setProgress(LoginActivity.sound);
        
        number = (EditText) findViewById(R.id.receiveFrom);
        number.setText(LoginActivity.number);
        
        this.saveButton = (Button) findViewById(R.id.saveButton);
        this.saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				int newVib = vibroBar.getProgress();
				int newSou = lydBar.getProgress();
				int newLig = lysBar.getProgress();
				LoginActivity.setNotificationAndroid(newVib, newSou, newLig);
				
				String newHigh = highlightChooser.getSelectedItem().toString();
				String newSMS = numberIncChooser.getSelectedItem().toString();
				String newNumber = number.getText().toString();
				
				LoginActivity.setHighlightTime(newHigh);
				LoginActivity.setNumberIncommingSMS(newSMS);
				LoginActivity.setReceivenumber(newNumber);
				Toast.makeText(getApplicationContext(), "Indstillinger gemt", Toast.LENGTH_SHORT).show();
			}
		});
        
        this.logoutButton = (Button) findViewById(R.id.sletloginButton);
        this.logoutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LoginActivity.appEditor.remove("user");
				LoginActivity.appEditor.commit();
				LoginActivity.indivEditor.clear();
				LoginActivity.indivEditor.commit();
                DBAdapter.getInstance(context).close();                
            	Intent loginpage = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(loginpage);
                finish();
			}
		});
    }
}