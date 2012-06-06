package dmri.nopo;

import dmri.nopo.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends Activity {
    
	private Button loginButton;
	private EditText user;
	@SuppressWarnings("unused")
	private ImageView logo;
	private SettingsManager settingsManager;
	private Context context = this;
	
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        user = (EditText) findViewById(R.id.username);
        logo = (ImageView) findViewById(R.id.loginlogo);  	
        this.loginButton = (Button) this.findViewById(R.id.login);
        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	String userName = user.getText().toString();
            	if(checkUserName(userName)) {
            		settingsManager = SettingsManager.getInstance(getApplicationContext());
            		SettingsManager.userName = userName;
            		settingsManager.setLastUser(userName);
                	settingsManager.setupSettings();
                	SettingsManager.pendingUnregister = false;
                	Intent intent = new Intent("android.intent.action.ALARM");
                	startActivity(intent);
                	}
            	else {
            		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            		alertDialog.setTitle("Ugyldige tegn i brugernavn");
            		alertDialog.setMessage("Brug venligst tegn fra a-z, A-Z og/eller 0-9");
            		alertDialog.setButton(-1, "ok", new DialogInterface.OnClickListener() {
            			public void onClick(DialogInterface dialog, int which) {}
            			});
            		alertDialog.show();
            		}
            	}
            });
                
        tryAutoLogin();
    }
    
    public boolean checkUserName(String userName) {
    	return userName.matches("[a-zA-Z0-9][a-zA-Z0-9]*");
    }
   
    public void tryAutoLogin() {
    	settingsManager = SettingsManager.getInstance(getApplicationContext());
    	if(settingsManager.hasStoredUser()) {
        	settingsManager.setupSettings();
        	SettingsManager.pendingUnregister = false;
        	Intent intent = new Intent("android.intent.action.ALARM");
        	startActivity(intent);
    	}
    }
}