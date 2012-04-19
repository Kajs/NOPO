package dmri.nopo;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;
import android.os.Vibrator;

public class NotificationManager {
	
	private String fileName;
	private String user;
	private int vibration;
	private int sound;
	private int light;
	private SharedPreferences appPref;
	private SharedPreferences indivPref;
	private Editor indivEditor;
	
	public NotificationManager(Context context) {
		appPref = context.getSharedPreferences("NOPOPref", context.MODE_PRIVATE);
		user = appPref.getString("user", "default");
        fileName = user + "Notify";
        indivPref = context.getSharedPreferences(fileName, context.MODE_PRIVATE);
        indivEditor = indivPref.edit();
        readUserFile();
	}
	
	private void readUserFile() {
		vibration = indivPref.getInt("vibrationValue", 50);
        sound = indivPref.getInt("soundValue", 50);
        light = indivPref.getInt("lightValue", 50);
	}
	
	private void writeUserFile() {
		indivEditor.putInt("vibrationValue", vibration);
		indivEditor.putInt("soundValue", sound);
		indivEditor.putInt("lightValue", light);
		indivEditor.commit();
	}
	public void setNotificationAndroid(int vib, int sou, int lig) {
		vibration = vib;
		sound = sou;
		light = lig;
		writeUserFile();
	}
	
	public void alarmNotify(Context c) {
		Vibrator v = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(Long.valueOf(vibration));
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
}
