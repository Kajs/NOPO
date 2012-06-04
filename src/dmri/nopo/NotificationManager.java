package dmri.nopo;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.io.IOException;
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
	private Context c;
	private static NotificationManager instance;
	private MediaPlayer player;
	private int soundRepeats;
	
	private NotificationManager(Context context) {
		c = context;
	}
	
	public static NotificationManager getInstance(Context context){
		if (NotificationManager.instance == null){
			NotificationManager.instance = new NotificationManager(context);
		}
		return NotificationManager.instance;
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
		vibrate();
		playSound();
	}
	
	public void vibrate() {
		if(LoginActivity.vibration > 0){
			int index = -1;
			Vibrator v = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);
			long[] pattern = vibrationPattern(0, 400, 100, LoginActivity.vibration);
			try{
				v.vibrate(pattern, index);
			}
			catch(Exception e){
				System.out.println("Vibrationsfejl");
				e.printStackTrace();
				Toast.makeText(c, "Proevede at vibrere", Toast.LENGTH_LONG).show();
				}
		}		
	}
	
	public void playSound() {
		if(LoginActivity.sound > 0) {
			soundRepeats = LoginActivity.sound - 1;
			if(player == null) {
				player = MediaPlayer.create(c, R.raw.beep);
				MediaPlayer.OnCompletionListener listener = new MediaPlayer.OnCompletionListener() {
					public void onCompletion(MediaPlayer mp) {
						if(soundRepeats > 0) {
							soundRepeats--;
							mp.start();
						}
					}
				};
				
				player.setOnCompletionListener(listener);
			}
			player.start();
		}		
	}
}
