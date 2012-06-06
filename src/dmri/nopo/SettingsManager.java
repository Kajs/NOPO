package dmri.nopo;

import android.content.Context;
import android.database.Cursor;

public class SettingsManager {
	static int currentView;
	static String userName;
	public int vibration;
	public int sound;
	public int highlightTime;
	public int numberAlarms;
	public String receiveNumber;
	//send number of emulator 5556
	static final String defaultReceiveNumber = "15555215556";
	static boolean pendingUnregister;
	static boolean isLoggedIn;
	private static SettingsManager instance;
	private Context context;
	
	private SettingsManager(Context ctx)
	{
		context = ctx;
	}
	
	public static SettingsManager getInstance(Context context){
		if (instance == null)
		{
			instance = new SettingsManager(context);
		}
		return SettingsManager.instance;
	}
	
	public boolean shouldReceive(String sender) {
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
	
	public void setupSettings() {
		getReceiveNumber();
		DBAdapter db = DBAdapter.getInstance(context);
		db.open();
		Cursor userSettings = db.getUserSettings();
		int size = userSettings.getCount();
		if(size == 0) {
			updateUserSettings(5, 5, 5, 6);
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
	
	private void parse(String setting, int value) {
		if (setting.equals("vibration")) {
		    vibration = value;
		} else if (setting.equals("sound")) {
			sound = value;
		} else if (setting.equals("highlightTime")) {
			highlightTime = value;
		} else if (setting.equals("numberAlarms")) {
			numberAlarms = value;
		}
	}
	
	public void updateUserSettings(int newVibration, int newSound, int newHighlightTime, int newNumberAlarms) {
		vibration = newVibration;
		sound = newSound;
		highlightTime = newHighlightTime;
		numberAlarms = newNumberAlarms;	
		DBAdapter db = DBAdapter.getInstance(context);
		db.open();
		db.updateUserSettings(newVibration, newSound, newHighlightTime, newNumberAlarms);
	}

	public void setReceiveNumber(String newReceiveNumber) {
		receiveNumber = newReceiveNumber;
		DBAdapter db = DBAdapter.getInstance(context);
		db.open();
		db.setReceiveNumber(newReceiveNumber);
	}
	
	public void setLastUser(String newLastUser) {
		DBAdapter db = DBAdapter.getInstance(context);
		db.open();
		db.setLastUser(newLastUser);
	}
	
	public void getReceiveNumber() {
		DBAdapter db = DBAdapter.getInstance(context);
		db.open();
		Cursor storedNumber = db.getReceiveNumber();
		if(storedNumber.getCount() == 0) {
			receiveNumber = defaultReceiveNumber;
		}
		else {
			receiveNumber = storedNumber.getString(0);
		}		
	}
	
	public boolean hasStoredUser() {
		DBAdapter db = DBAdapter.getInstance(context);
		db.open();
		Cursor storedUser = db.getLastUser();
		if(storedUser.getCount() == 0) {
			return false;
		}
		else {
			userName = storedUser.getString(0);
			return true;
		}
	}
}
