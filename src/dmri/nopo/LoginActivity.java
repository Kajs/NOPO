package dmri.nopo;

import dmri.nopo.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends Activity {
    
	private Button loginButton;
	private EditText user;
	@SuppressWarnings("unused")
	private ImageView logo;
	
	
	
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
            		SettingManager.userName = userName;
                	DBAdapter.updateTableNames(userName+"log", userName+"filter", userName+"settings");
                	SettingManager.setupSettings(getApplicationContext());
                	Intent intent = new Intent("android.intent.action.ALARM");
                	startActivity(intent);
                	}
            	else {
            		Toast.makeText(getApplicationContext(), "Brug venligst tegn fra a-z, A-Z og/eller 0-9", Toast.LENGTH_LONG).show();
            	}
            }
        });
    }
    
    public boolean checkUserName(String userName) {
    	Log.w("Matches [a-zA-Z0-9][a-zA-Z0-9]*", Boolean.toString(userName.matches("[a-zA-Z0-9][a-zA-Z0-9]*")));
    	return userName.matches("[a-zA-Z0-9][a-zA-Z0-9]*");
    }
}