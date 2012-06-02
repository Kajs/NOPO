package dmri.nopo;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.regex.*;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class NotificationManager {
	
	private String fileName;
	private String user;
	private int vibration;
	private int sound;
	private int light;
	private int showNumberIncomingSMS;
	private int highlightTime;
	private String number;
	private SharedPreferences appPref;
	private SharedPreferences indivPref;
	private Editor indivEditor;
	private Context c;
	private static NotificationManager instance;
	private MediaPlayer player;
	
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
		vibration = indivPref.getInt("vibrationValue", 5);
        sound = indivPref.getInt("soundValue", 2);
        light = indivPref.getInt("lightValue", 50);
        highlightTime = indivPref.getInt("highlightValue", 5);
        showNumberIncomingSMS = indivPref.getInt("numberIncomingSMS", 6);
        number = indivPref.getString("receiveNumber", "15555215556");
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
	
	public void setReceivenumber(String number) {
		this.number = number;
		indivEditor.putString("receiveNumber", number);
		indivEditor.commit();
	}
	
	public boolean shouldReceive(String sender) {
		if(number.equals(sender)) {
			return true;
		}
		else return false;
	}
	
	public long[] vibrationPattern(int delay, int duration, int sleep, int repeat){
		int counter = repeat;
		int pointer = 0;
		long[] pattern = new long[1 + repeat * 2];
		pattern[0] = delay;
		while(counter > 0){
			pattern[pointer + 1] = duration;
			pattern[pointer + 2] = sleep;
			counter = counter - 1;
			pointer = pointer + 2;
		}
		return pattern;
	}
	
	public void alarmNotify() {
		if(vibration > 0){
			int index = -1;
			Vibrator v = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);
			long[] pattern = vibrationPattern(0, 400, 100, vibration);
			try{
				v.vibrate(pattern, index);
			}
			catch(Exception e){
				System.out.println("Vibrationsfejl");
				e.printStackTrace();
				Toast.makeText(c, "Proevede at vibrere", Toast.LENGTH_LONG).show();
				}
		}
		if(sound > 0) {
			player = MediaPlayer.create(c, R.raw.beep);
			CountDownTimer timer = new CountDownTimer(sound * 1000, 1000) {
				@Override
				public void onFinish() {
					player.setLooping(false);
					player.stop();
				}

				@Override
				public void onTick(long millisUntilFinished) {
					// TODO Auto-generated method stub
					
				}
			};
				try{
					player.setLooping(true);
					player.start();
					timer.start();
					
				}
				catch(Exception e) {
					Log.w("MediaPlayer", e.getMessage());
				}
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
	/**
	public class SoundManager {
		 
		private  SoundPool mSoundPool;
		private  HashMap mSoundPoolMap;
		private  AudioManager  mAudioManager;
		private  Context mContext;
		
		public void initSounds(Context theContext) {
		    mContext = theContext;
		    mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		    mSoundPoolMap = new HashMap();
		    mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
		}
		
		public void addSound(int index, int SoundID)
		{
		    mSoundPoolMap.put(index, mSoundPool.load(mContext, SoundID, 1));
		}
		
		public void playSound(int index)
		{
		float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		    mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f);
		}
		 
		public void playLoopedSound(int index)
		{
		    float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		    streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		    mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, -1, 1f);
		}
	}
	*/
}
