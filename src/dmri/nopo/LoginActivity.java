package dmri.nopo;

import dmri.nopo.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends Activity {
    
	private Button loginButton;
	private EditText user;
	@SuppressWarnings("unused")
	private ImageView logo;
	
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        user = (EditText) findViewById(R.id.username);
        logo = (ImageView) findViewById(R.id.loginlogo);  	
        this.loginButton = (Button) this.findViewById(R.id.login);
        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	String userName = user.getText().toString();
            	SettingManager.userName = userName;
            	DBAdapter.updateTableNames(userName+"log", userName+"filter", userName+"settings");
            	SettingManager.isNewUser(getApplicationContext());
            	Intent intent = new Intent("android.intent.action.ALARM");
            	startActivity(intent);
            	}});         	
        }
/**	
	private void getSettings() {
		DBAdapter db = DBAdapter.getInstance(this);
		db.open();
		db.updateUserSettings(1, 2, 3, 4, 5);
		Cursor settings = db.getUserSettings();
		int size = settings.getCount();
		int index = 0;
		settings.moveToFirst();
		while(index < size) {
			String result = "";
			result = result + settings.getString(0) + ": " + settings.getString(1);
			Log.w("testingDatabase", "Run " + Integer.toString(index) + ": " + result);
			settings.moveToNext();
			index++;
		}
	}
	*/
}