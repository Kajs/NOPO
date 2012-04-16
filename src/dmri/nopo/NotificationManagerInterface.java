package dmri.nopo;


public interface NotificationManagerInterface {
	public boolean readUserFile(String user); // Get ints from sharedPreferences.
	public boolean writeUserFile();
	public void setNotificationAndroid();
	public void alarmNotify();
}