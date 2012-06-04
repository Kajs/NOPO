package dmri.nopo;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmri.nopo.R;
import android.util.Log;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends Activity {
    
	private Button loginButton;
	static boolean tryAutoLogin = true;
	//private EditText pass;
	private EditText user;
	static SharedPreferences appPref;
	static SharedPreferences indivPref;
	static SharedPreferences.Editor appEditor;
	static SharedPreferences.Editor indivEditor;
	private String fileName;
	static String userName;
	static int vibration;
	static int sound;
	static int light;
	static int showNumberIncomingSMS;
	static int highlightTime;
	static String receiveNumber;
	@SuppressWarnings("unused")
	private ImageView logo;
	
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        //pass = (EditText) findViewById(R.id.password);
        user = (EditText) findViewById(R.id.username);
        logo = (ImageView) findViewById(R.id.loginlogo);
        
        if(tryAutoLogin) {
        	tryAutoLogin();
        }
        
        this.loginButton = (Button) this.findViewById(R.id.login);
        this.loginButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
            	try {
	        		Intent intent = new Intent("android.intent.action.ALARM");

	        		userName = user.getText().toString();
	        		DBAdapter.updateTableNames(userName+"log", userName+"filter");
	        		
	        		appPref = getSharedPreferences("NOPOPref", Context.MODE_PRIVATE);
	        		receiveNumber = appPref.getString("receiveNumber", "15555215556");
	                appEditor = appPref.edit();
	                appEditor.putString("user", userName);
	            	appEditor.commit();
	            	
	            	fileName = userName + "Notify";
	                indivPref = getSharedPreferences(fileName, Context.MODE_PRIVATE);
	                readUserFile();
	                indivEditor = indivPref.edit();
	            	
	                startActivity(intent);
	                finish();
	            	}      	
            	catch (Exception e) {
            		String fail = e.getMessage();
            		Log.e("Fejl ved login", fail);
            	}
            
        	}});
    }

    public void tryAutoLogin() {
    	appPref = getSharedPreferences("NOPOPref", Context.MODE_PRIVATE);
    	String lastUser = appPref.getString("user", "default");
    	
    	if(lastUser != "default") {
    		Toast.makeText(this, "Logger ind som sidste bruger: " + lastUser, Toast.LENGTH_LONG).show();
    		Intent intent = new Intent("android.intent.action.ALARM");
    			
    		userName = lastUser;
    		DBAdapter.updateTableNames(userName+"log", userName+"filter");
    			
    		appPref = getSharedPreferences("NOPOPref", Context.MODE_PRIVATE);
        	receiveNumber = appPref.getString("receiveNumber", "15555215556");
            appEditor = appPref.edit();
            	
            fileName = userName + "Notify";
            indivPref = getSharedPreferences(fileName, Context.MODE_PRIVATE);
            readUserFile();
            indivEditor = indivPref.edit();
            	
            startActivity(intent);
            finish();
    		}
    		else{    			
    		}
    	}
    
    private void readUserFile() {
		vibration = indivPref.getInt("vibrationValue", 5);
        sound = indivPref.getInt("soundValue", 5);
        light = indivPref.getInt("lightValue", 50);
        highlightTime = indivPref.getInt("highlightValue", 5);
        showNumberIncomingSMS = indivPref.getInt("numberIncomingSMS", 6);
	}
	
    static void setNotificationAndroid(int vib, int sou, int lig) {
		vibration = vib;
		sound = sou;
		light = lig;
		indivEditor.putInt("vibrationValue", vibration);
		indivEditor.putInt("soundValue", sound);
		indivEditor.putInt("lightValue", light);
		indivEditor.commit();
	}
	
	static void setNumberIncommingSMS(String number) {
		Pattern intsOnly = Pattern.compile("\\d+");
		Matcher makeMatch = intsOnly.matcher(number);
		makeMatch.find();
		String inputInt = makeMatch.group();
		showNumberIncomingSMS = Integer.parseInt(inputInt);
		indivEditor.putInt("numberIncomingSMS", showNumberIncomingSMS);
		indivEditor.commit();
	}
	
	static void setHighlightTime(String time) {
		Pattern intsOnly = Pattern.compile("\\d+");
		Matcher makeMatch = intsOnly.matcher(time);
		makeMatch.find();
		String inputInt = makeMatch.group();
		highlightTime = Integer.parseInt(inputInt);
		indivEditor.putInt("highlightValue", highlightTime);
		indivEditor.commit();
	}
	
    static void setReceivenumber(String newNumber) {
		receiveNumber = newNumber;
		appEditor.putString("receiveNumber", receiveNumber);
		appEditor.commit();
	}
	
	static boolean shouldReceive(String sender) {
		if(receiveNumber.equals("")) {
			return true;
		}
		if(receiveNumber.equals(sender)) {
			return true;
		}
		else return false;
	}
	
	@Override 
	public void onDestroy() {
		tryAutoLogin = true;
		super.onDestroy();
	}
}