package dmri.nopo;

public interface NotificationManagerInterface { // Constructoren tager Context som parameter, brug new NotificationManager(this);
	public void setNotificationAndroid(int vibration, int sound, int light); // Tilføjer nye værdier for den individuelle bruger.
	public void alarmNotify(); // Sætter notifikationen igang, bruges når en sms modtages.
	public int getUserVibration(); //Returnerer vibrationen for den individuelle bruger.
	public int getUserSound(); //Returnerer lyden for den idividuelle bruger.
	public int getUserLight(); //Returnerer lyset for den individuelle bruger.
	public String getUser(); // Returnerer navnet på brugeren.
}