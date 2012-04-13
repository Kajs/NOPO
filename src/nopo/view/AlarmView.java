package nopo.view;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class AlarmView extends Activity {
    
	private Button logButton;
	private Button filterButton;
	private Button menuButton;
	private String user;
	private String pass;
	private TextView username;
	private TextView password;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
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
        this.menuButton = (Button) this.findViewById(R.id.menu);
        this.menuButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
            	Intent intent = new Intent("android.intent.action.MENU");
                startActivity(intent);
            }
        });
        username = (TextView) findViewById(R.id.username);
        password = (TextView) findViewById(R.id.password);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	user = extras.getString("username");
        	pass = extras.getString("password");
        }
        if (user != null && pass != null) {
        	username.setText(user);
        	password.setText(pass);
        }
        else {
        	username.setText("Not working");
        	password.setText("Not working");
        }
        
        
    }
}