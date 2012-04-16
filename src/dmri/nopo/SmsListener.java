package dmri.nopo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    			    		
    		Intent broadcastIntent = new Intent();
    		broadcastIntent.setAction("SMS_RECEIVED_ACTION");
    		broadcastIntent.putExtra("sms", message);
    		context.sendBroadcast(broadcastIntent);
    	}	

    } 
}