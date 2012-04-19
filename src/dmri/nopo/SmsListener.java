package dmri.nopo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.util.Calendar;
import android.telephony.SmsMessage;

	public class SmsListener extends BroadcastReceiver {
		
	    @Override
	    public void onReceive(Context context, Intent intent) {
	    	Bundle bundle = intent.getExtras();
	    	SmsMessage msg = null;
	    	String message = "";
	    	String sender = "";
	    	
	    	if (bundle != null) {	
	    		Object[] pdus = (Object[]) bundle.get("pdus");     		
	    		msg = SmsMessage.createFromPdu((byte[])pdus[0]);
	    		message += msg.getMessageBody().toString();
	    		sender += msg.getOriginatingAddress(); 
	    		
	    		Calendar c = Calendar.getInstance();
	    		
	    		int[] time = new int[3];
	    		time[0] = c.get(Calendar.HOUR_OF_DAY);
	    		time[1] = c.get(Calendar.MINUTE);
	    		time[2] = c.get(Calendar.SECOND);
	    		
	            Intent broadcastIntent = new Intent();
	            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
	            broadcastIntent.putExtra("sms", message);
	            broadcastIntent.putExtra("dmri.nopo.time", time);
	            broadcastIntent.putExtra("sender", sender);
	            context.sendBroadcast(broadcastIntent);
			}
	    }
}