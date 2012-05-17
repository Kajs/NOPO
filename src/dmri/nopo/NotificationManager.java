package dmri.nopo;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.regex.*;
import android.content.Context;
import android.os.Vibrator;
import android.widget.Toast;

public class NotificationManager {
	
	private String fileName;
	private String user;
	private int vibration;
	private int sound;
	private int light;
	private int showNumberIncomingSMS;
	private int highlightTime;
	private SharedPreferences appPref;
	private SharedPreferences indivPref;
	private Editor indivEditor;
	private Context c;
	private static NotificationManager instance;
	
	private NotificationManager(Context context) {
		c = context;
		appPref = context.getSharedPreferences("NOPOPref", context.MODE_PRIVATE);
		user = appPref.getString("user", "default");
        fileName = user + "Notify";
        indivPref = context.getSharedPreferences(fileName, context.MODE_PRIVATE);
        indivEditor = indivPref.edit();
        readUserFile();
	}
	
	public static NotificationManager getInstance(Context context){
		if (NotificationManager.instance == null){
			NotificationManager.instance = new NotificationManager(context);
		}
		return NotificationManager.instance;
	}
	
	private void readUserFile() {
		vibration = indivPref.getInt("vibrationValue", 50);
        sound = indivPref.getInt("soundValue", 50);
        light = indivPref.getInt("lightValue", 50);
        highlightTime = indivPref.getInt("highlightValue", 5);
        showNumberIncomingSMS = indivPref.getInt("numberIncomingSMS", 6);
	}
	
	public void setNotificationAndroid(int vib, int sou, int lig) {
		vibration = vib;
		sound = sou;
		light = lig;
		indivEditor.putInt("vibrationValue", vibration);
		indivEditor.putInt("soundValue", sound);
		indivEditor.putInt("lightValue", light);
		indivEditor.commit();
	}
	
	public void setNumberIncommingSMS(String number) {
		Pattern intsOnly = Pattern.compile("\\d+");
		Matcher makeMatch = intsOnly.matcher(number);
		makeMatch.find();
		String inputInt = makeMatch.group();
		showNumberIncomingSMS = Integer.parseInt(inputInt);
		indivEditor.putInt("numberIncomingSMS", showNumberIncomingSMS);
		indivEditor.commit();
	}
	
	public void setHighlightTime(String time) {
		Pattern intsOnly = Pattern.compile("\\d+");
		Matcher makeMatch = intsOnly.matcher(time);
		makeMatch.find();
		String inputInt = makeMatch.group();
		highlightTime = Integer.parseInt(inputInt);
		indivEditor.putInt("highlightValue", highlightTime);
		indivEditor.commit();
	}
	
	public void alarmNotify() {
		Vibrator v = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);
		long[] level = {0, 5000, 500, 0, 5000, 500, 0, 5000, 500, 0, 5000, 500, 0, 5000, 500, 0, 5000, 500};
		try{
			v.vibrate(level, -1);
		}
		catch(Exception e){
			System.out.println("Vibrationsfejl");
			e.printStackTrace();
			Toast.makeText(c, "Proevede at vibrere", Toast.LENGTH_LONG).show();
			}
			
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
		return user;
	}
	
	public static String getUserStatic(Context context) {
		SharedPreferences pref = context.getSharedPreferences("NOPOPref", context.MODE_PRIVATE);
		String result = pref.getString("user", "default");
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
}
