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

public class LoginActivity extends Activity {
    
	private Button loginButton;
	//private EditText pass;
	private EditText user;
	private ImageView logo;
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
	static String number;
	
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        //pass = (EditText) findViewById(R.id.password);
        user = (EditText) findViewById(R.id.username);
        logo = (ImageView) findViewById(R.id.loginlogo);
        
        this.loginButton = (Button) this.findViewById(R.id.login);
        this.loginButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
            	try {
	        		Intent intent = new Intent("android.intent.action.ALARM");
	        		
	        		userName = user.getText().toString();
	        		DBAdapter.updateTableNames(userName+"log", userName+"filter");
	        		
	        		appPref = getSharedPreferences("NOPOPref", Context.MODE_PRIVATE);
	                appEditor = appPref.edit();
	                appEditor.putString("user", userName);
	            	appEditor.commit();
	            	
	            	fileName = userName + "Notify";
	                indivPref = getSharedPreferences(fileName, Context.MODE_PRIVATE);
	                readUserFile();
	                indivEditor = indivPref.edit();
	            	
	                startActivity(intent);
	            	}      	
            	catch (Exception e) {
            		String fail = e.getMessage();
            		Log.e("Fejl ved login", fail);
            	}
            
        	}});
        
        
    }
    private void readUserFile() {
		vibration = indivPref.getInt("vibrationValue", 5);
        sound = indivPref.getInt("soundValue", 5);
        light = indivPref.getInt("lightValue", 50);
        highlightTime = indivPref.getInt("highlightValue", 5);
        showNumberIncomingSMS = indivPref.getInt("numberIncomingSMS", 6);
        number = indivPref.getString("receiveNumber", "15555215556");
	}
    
    public int getUserVibration() {
		return vibration;
	}
	
	public int getUserSound() {
		return sound;
	}
	
	public int getUserLight() {
		return light;
	}
	
	public String getUser() {
		return userName;
	}
	
	public static String getUserStatic(Context context) {
		SharedPreferences pref = context.getSharedPreferences("NOPOPref", Context.MODE_PRIVATE);
		String result = pref.getString("user", "default");
		pref = null;
		return result;
	}
	
	public int getHighlightTimeInt() {
		return highlightTime;
	}
	
	public String getHighlightTimeString() {
		String result = highlightTime + " min";
		return result;
	}
	
	public int getNumberIncomingSMSInt() {
		return showNumberIncomingSMS;
	}
	
	public String getNumberIncomingSMSString() {
		String result = showNumberIncomingSMS + " sms";
		return result;
	}
	
	public int getReceiveNumberInt() {
		Pattern intsOnly = Pattern.compile("\\d+");
		Matcher makeMatch = intsOnly.matcher(number);
		makeMatch.find();
		String inputInt = makeMatch.group();
		int phoneNumber = Integer.parseInt(inputInt);
		return phoneNumber;
	}
	
	public String getReceiveNumberString() {
		return number;
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
		number = newNumber;
		indivEditor.putString("receiveNumber", number);
		indivEditor.commit();
	}
	
	static boolean shouldReceive(String sender) {
		if(number.equals(sender)) {
			return true;
		}
		else return false;
	}
}