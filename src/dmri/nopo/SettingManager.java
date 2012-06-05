package dmri.nopo;

import android.content.Context;
import android.database.Cursor;

public class SettingManager {
	static String userName;
	static int vibration;
	static int sound;
	static int light;
	static int highlightTime;
	static int numberAlarms;
	static String receiveNumber = "15555215556";
	
	static boolean shouldReceive(String sender) {
		if(receiveNumber.equals("")) {
			return true;
		}
		if(receiveNumber.equals(sender)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	static boolean isNewUser(Context context) {
		DBAdapter db = DBAdapter.getInstance(context);
		db.open();
		Cursor userSettings = db.getUserSettings();
		int size = userSettings.getCount();
		if(size == 0) {
			updateUserSettings(context, 5, 5, 5, 5, 6);
			return true;
		}
		else{
			int index = 0;
			userSettings.moveToFirst();
			while(index < size) {
				String setting = userSettings.getString(0);
				int value = userSettings.getInt(1);
				parse(setting, value);
				userSettings.moveToNext();
				index++;
			}
			return false;
		}		
	}
	
	static void readUserSettings(Context context) {
		DBAdapter db = DBAdapter.getInstance(context);
		db.open();
		Cursor userSettings = db.getUserSettings();
		int size = userSettings.getCount();
		int index = 0;
		userSettings.moveToFirst();
		while(index < size) {
			String setting = userSettings.getString(0);
			int value = userSettings.getInt(1);
			parse(setting, value);
			userSettings.moveToNext();
			index++;
		}				
	}
	
	static void parse(String setting, int value) {
		if (setting.equals("vibration")) {
		    vibration = value;
		} else if (setting.equals("sound")) {
			sound = value;
		} else if (setting.equals("light")) {
			light = value;
		} else if (setting.equals("highlightTime")) {
			highlightTime = value;
		} else if (setting.equals("numberAlarms")) {
			numberAlarms = value;
		}
	}
	
	static void updateUserSettings(Context context, int newVibration, int newSound, int newLight, int newHighlightTime, int newNumberAlarms) {
		vibration = newVibration;
		sound = newSound;
		light = newLight;
		highlightTime = newHighlightTime;
		numberAlarms = newNumberAlarms;	
		DBAdapter db = DBAdapter.getInstance(context);
		db.open();		
		db.updateUserSettings(newVibration, newSound, newLight, newHighlightTime, newNumberAlarms);
	}
	
	static void updateSetting(Context context, String setting, int value) {
		parse(setting, value);
		DBAdapter db = DBAdapter.getInstance(context);
		db.open();		
		db.updateUserSetting(setting, value);
	}
}
