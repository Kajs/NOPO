package dmri.nopo;


import dmri.nopo.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Button;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;


public class MenuActivity extends Activity {
    
	private Button saveButton;
	private Button deleteUserButton;
	private Button logoutButton;
	private TextView username;
	private SeekBar vibroBar;
	private SeekBar lydBar;
	private SeekBar lysBar;
	private Spinner highlightChooser;
	private Spinner numberIncChooser;
	private Context context;
	private EditText receiveNumber;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        this.context = this;
        
        ArrayAdapter<CharSequence> highlightAdapter = ArrayAdapter.createFromResource(this, R.array.highlightNames, 
        		android.R.layout.simple_spinner_item);
        highlightChooser = (Spinner) findViewById(R.id.highlightSpinner);
        highlightChooser.setAdapter(highlightAdapter);
        int highlightPosition = highlightAdapter.getPosition(Integer.toString(SettingManager.highlightTime) + " min");
        highlightChooser.setSelection(highlightPosition);
        
        ArrayAdapter<CharSequence> SMSAdapter = ArrayAdapter.createFromResource(this, R.array.numberSMSNames, 
        		android.R.layout.simple_spinner_item);
        numberIncChooser = (Spinner) findViewById(R.id.numberSMSSpinner);
        numberIncChooser.setAdapter(SMSAdapter);
        int smsPosition = SMSAdapter.getPosition(Integer.toString(SettingManager.numberAlarms) + " alarmer");
        numberIncChooser.setSelection(smsPosition);
        
        username = (TextView) findViewById(R.id.userlogin);
        username.setText(SettingManager.userName);
        
        vibroBar = (SeekBar) findViewById(R.id.vibrobar);
        vibroBar.setProgress(SettingManager.vibration);
        
        lydBar = (SeekBar) findViewById(R.id.lydbar);
        lydBar.setProgress(SettingManager.sound);
        
        lysBar = (SeekBar) findViewById(R.id.lysbar);
        lysBar.setProgress(SettingManager.light);
        
        receiveNumber = (EditText) findViewById(R.id.receiveFrom);
        receiveNumber.setText(SettingManager.receiveNumber);
        
        this.saveButton = (Button) findViewById(R.id.saveButton);
        this.saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				int newVib = vibroBar.getProgress();
				int newSou = lydBar.getProgress();
				int newLig = lysBar.getProgress();				
				int newHigh = new Integer(highlightChooser.getSelectedItem().toString().split(" ")[0]);
				Log.w("testSettings", "Menu.newHigh: " + Integer.toString(newHigh));
				int newSMS = new Integer(numberIncChooser.getSelectedItem().toString().split(" ")[0]);
				Log.w("testHighLight", "Menu.newSMS: " + Integer.toString(newHigh));
				SettingManager.updateUserSettings(context, newVib, newSou, newLig, newHigh, newSMS);
				
				String newNumber = receiveNumber.getText().toString();
				SettingManager.setReceiveNumber(context, newNumber);
				Toast.makeText(getApplicationContext(), "Indstillinger gemt", Toast.LENGTH_SHORT).show();
			}
		});
        
        this.deleteUserButton = (Button) findViewById(R.id.deleteUserButton);
        this.deleteUserButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog alertDialog = new AlertDialog.Builder(context).create();
	        	  alertDialog.setTitle("Slet Bruger");
	        	  alertDialog.setMessage("Advarsel! Dette vil slette brugeren " + SettingManager.userName + ", brugerens alarmfilter samt brugerens indstillinger fra systemet. Vil du fortsaette?");
	        	  alertDialog.setButton(-1, "Godkend", new DialogInterface.OnClickListener() {
	        	     public void onClick(DialogInterface dialog, int which) {
	        	    	 DBAdapter db = DBAdapter.getInstance(context);
	        	    	 db.deleteUser();
	        	    	 db.close();
	        	    	 Intent loginpage = new Intent(MenuActivity.this, LoginActivity.class);
	                     startActivity(loginpage);
	                     finish();
	        	     }
	        	  });
	        	  alertDialog.setButton(-2, "Annuller", new DialogInterface.OnClickListener() {
	            	 public void onClick(DialogInterface dialog, int which) {
	            	 }
	        	  });
	        	  alertDialog.show();
			}
		});
        
        this.logoutButton = (Button) findViewById(R.id.logoutButton);
        this.logoutButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog alertDialog = new AlertDialog.Builder(context).create();
	        	  alertDialog.setTitle("Log ud");
	        	  alertDialog.setMessage("Skal der logges af systemet?");
	        	  alertDialog.setButton(-1, "Ja", new DialogInterface.OnClickListener() {
	        	     public void onClick(DialogInterface dialog, int which) {
	        	    	 DBAdapter.getInstance(context).close();    
	        	    	 Intent loginpage = new Intent(MenuActivity.this, LoginActivity.class);
	                     startActivity(loginpage);
	                     finish();
	        	     }
	        	  });
	        	  alertDialog.setButton(-2, "Nej", new DialogInterface.OnClickListener() {
	            	 public void onClick(DialogInterface dialog, int which) {
	            	 }
	        	  });
	        	  alertDialog.show();
			}
		});
    }
}