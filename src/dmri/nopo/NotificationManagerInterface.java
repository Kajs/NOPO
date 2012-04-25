package dmri.nopo;

public interface NotificationManagerInterface { // Constructoren tager Context som parameter, brug new NotificationManager(this);
	public void setNotificationAndroid(int vibration, int sound, int light); // Tilføjer nye værdier for den individuelle bruger.
	public void setNumberIncommingSMS(String number); // Tager inputtet fra spineren og gemmer som en int.
	public void setHighlightTime(String time); // Tager inputtet fra spineren og gemmer som int.
	public void alarmNotify(); // Sætter notifikationen igang, bruges når en sms modtages.
	public int getUserVibration(); //Returnerer vibrationen for den individuelle bruger.
	public int getUserSound(); //Returnerer lyden for den idividuelle bruger.
	public int getUserLight(); //Returnerer lyset for den individuelle bruger.
	public String getUser(); // Returnerer navnet på brugeren.
	public int getHighlightTimeInt(); // Returnerer tiden som int. default er 5 min.
	public String getHighlightTimeString(); // Returnerer tiden som String. fx "5 min".
	public int getNumberIncomingSMSInt(); // Returnerer antal sms'er der vises, som int. default er 6.
	public String getNumberIncomingSMSString(); // Returnerer anntal sms'er der vises som String. fx "6 sms".
}