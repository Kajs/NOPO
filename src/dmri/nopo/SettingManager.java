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
	static String receiveNumber;
	//send number of emulator 5556
	static final String defaultReceiveNumber = "15555215556";
	private static DBAdapter db = null;
	
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
	
	static void setupSettings(Context context) {
		prepareDatabase(context);
		getReceiveNumber(context);
		
		Cursor userSettings = db.getUserSettings();
		int size = userSettings.getCount();
		if(size == 0) {
			updateUserSettings(context, 5, 5, 5, 5, 6);
		}
		else{
			int index = 0;
			while(index < size) {
				String setting = userSettings.getString(0);
				int value = userSettings.getInt(1);
				parse(setting, value);
				userSettings.moveToNext();
				index++;
			}
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
		prepareDatabase(context);		
		db.updateUserSettings(newVibration, newSound, newLight, newHighlightTime, newNumberAlarms);
	}
	
	static void updateSetting(Context context, String setting, int value) {
		parse(setting, value);
		prepareDatabase(context);		
		db.updateUserSetting(setting, value);
	}
	
	static void setReceiveNumber(Context context, String newReceiveNumber) {
		receiveNumber = newReceiveNumber;
		prepareDatabase(context);	
		db.setReceiveNumber(newReceiveNumber);
	}
	
	static void setLastUser(Context context, String newLastUser) {
		prepareDatabase(context);		
		db.setReceiveNumber(newLastUser);
	}
	
	static void getReceiveNumber(Context context) {
		prepareDatabase(context);
		Cursor storedNumber = db.getReceiveNumber();
		if(storedNumber.getCount() == 0) {
			receiveNumber = defaultReceiveNumber;
		}
		else {
			receiveNumber = storedNumber.getString(0);
		}		
	}
	
	static boolean hasStoredUser(Context context) {
		prepareDatabase(context);
		Cursor storedUser = db.getLastUser();
		if(storedUser.getCount() == 0) {
			return false;
		}
		else {
			userName = storedUser.getString(0);
			return true;
		}
	}
	
	static void prepareDatabase(Context context) {
		if(db == null){
			db = DBAdapter.getInstance(context);
			db.open();
		}
	}
}
