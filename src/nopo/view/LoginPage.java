package nopo.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class LoginPage extends Activity {
    
	private Button loginButton;
	private EditText pass;
	private EditText user;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        pass = (EditText) findViewById(R.id.password);
        user = (EditText) findViewById(R.id.username);
        this.loginButton = (Button) this.findViewById(R.id.login);
        this.loginButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
            	Intent intent = new Intent("android.intent.action.ALARM");
            	Bundle extras = new Bundle();
            	extras.putString("username", user.getText().toString());
            	extras.putString("password", pass.getText().toString());
            	intent.putExtras(extras);
                startActivity(intent);
            }
        });
        
        }
}