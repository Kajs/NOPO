package dmri.nopo;


import dmri.nopo.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Button;
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
	private Spinner highlightChooser;
	private Spinner numberIncChooser;
	private Context context;
	private EditText receiveNumber;
	private SettingsManager settingsManager;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        
        this.saveButton = (Button) findViewById(R.id.saveButton);
        this.saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				int newVib = vibroBar.getProgress();
				int newSou = lydBar.getProgress();			
				int newHigh = new Integer(highlightChooser.getSelectedItem().toString().split(" ")[0]);
				int newSMS = new Integer(numberIncChooser.getSelectedItem().toString().split(" ")[0]);
				settingsManager.updateUserSettings(newVib, newSou, newHigh, newSMS);
				
				String newNumber = receiveNumber.getText().toString();
				settingsManager.setReceiveNumber(newNumber);
				Toast.makeText(getApplicationContext(), "Indstillinger gemt", Toast.LENGTH_SHORT).show();
			}
		});
        
        this.deleteUserButton = (Button) findViewById(R.id.deleteUserButton);
        this.deleteUserButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog alertDialog = new AlertDialog.Builder(context).create();
	        	  alertDialog.setTitle("Slet Bruger");
	        	  alertDialog.setMessage("Advarsel! Dette vil slette brugeren " + SettingsManager.userName + ", brugerens alarmfilter samt brugerens indstillinger fra systemet. Vil du fortsaette?");
	        	  alertDialog.setButton(-1, "Godkend", new DialogInterface.OnClickListener() {
	        	     public void onClick(DialogInterface dialog, int which) {
	        	    	 DBAdapter db = DBAdapter.getInstance(context);
	        	    	 db.open();
	        	    	 db.deleteUser();
	        	    	 db.close();
	        	    	 SettingsManager.keepLoggedIn = false;
	        	    	 SettingsManager.pendingClose = true;
	        	    	 SettingsManager.haltAtLogin = true;
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
	        	    	 SettingsManager.keepLoggedIn = false;
	        	    	 SettingsManager.pendingClose = true;
	        	    	 SettingsManager.haltAtLogin = true;
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
    
    @Override
    protected void onResume() {
    	ViewChangeActivity.colorButtonsViaArray(3);
    	if (SettingsManager.pendingClose) {
		    finish();
		} else if (SettingsManager.pendingMinimize) {
			moveTaskToBack(true);
		} else {
            context = this;
            
            settingsManager = SettingsManager.getInstance(context);
            ArrayAdapter<CharSequence> highlightAdapter = ArrayAdapter.createFromResource(this, R.array.highlightNames, 
            		android.R.layout.simple_spinner_item);
            highlightChooser = (Spinner) findViewById(R.id.highlightSpinner);
            highlightChooser.setAdapter(highlightAdapter);
            int highlightPosition = highlightAdapter.getPosition(Integer.toString(settingsManager.highlightTime) + " min");
            highlightChooser.setSelection(highlightPosition);
            
            ArrayAdapter<CharSequence> SMSAdapter = ArrayAdapter.createFromResource(this, R.array.numberSMSNames, 
            		android.R.layout.simple_spinner_item);
            numberIncChooser = (Spinner) findViewById(R.id.numberSMSSpinner);
            numberIncChooser.setAdapter(SMSAdapter);
            int smsPosition = SMSAdapter.getPosition(Integer.toString(settingsManager.numberAlarms) + " alarmer");
            numberIncChooser.setSelection(smsPosition);
            
            username = (TextView) findViewById(R.id.userlogin);
            username.setText(SettingsManager.userName);
            
            vibroBar = (SeekBar) findViewById(R.id.vibrobar);
            vibroBar.setProgress(settingsManager.vibration);
            
            lydBar = (SeekBar) findViewById(R.id.lydbar);
            lydBar.setProgress(settingsManager.sound);
            
            receiveNumber = (EditText) findViewById(R.id.receiveFrom);
            receiveNumber.setText(settingsManager.receiveNumber);;
		}
    	
        super.onResume();
    }
    
    @Override
    public void onBackPressed() {
  	  AlertDialog alertDialog = new AlertDialog.Builder(this).create();
  	  alertDialog.setTitle("Advarsel");
  	  alertDialog.setMessage("Hvis du vaelger luk, vil programmet ikke modtage alarmer");
  	  alertDialog.setButton(-1, "Luk", new DialogInterface.OnClickListener() {
  	     public void onClick(DialogInterface dialog, int which) {
  	    	 SettingsManager.keepLoggedIn = false;
  	    	 SettingsManager.pendingClose = true;
  	    	 finish();
  	     }
  	  });
  	  alertDialog.setButton(-2, "Annuller", new DialogInterface.OnClickListener() {
      	 public void onClick(DialogInterface dialog, int which) {
      	 }
  	  });
  	  alertDialog.setButton(-3, "Minimer", new DialogInterface.OnClickListener() {
  	     public void onClick(DialogInterface dialog, int which) {
  	    	 SettingsManager.pendingMinimize = true;
  	    	 moveTaskToBack(true);
  	     }
  	  });
  	  alertDialog.show();
    }
}