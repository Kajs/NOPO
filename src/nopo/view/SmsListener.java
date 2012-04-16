package nopo.view;

import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
    	Bundle bundle = intent.getExtras();
    	SmsMessage[] msgs = null;
    	String str = "";
    	
    	if (bundle != null) {	
    		Object[] pdus = (Object[]) bundle.get("pdus"); 
    		msgs = new SmsMessage[pdus.length]; 
    		for (int i = 0; i < msgs.length; i++) {
    			msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
    			str += "SMS from " + msgs[i].getOriginatingAddress();
    			str += " :";
    			str += msgs[i].getMessageBody().toString();
    			str += "\n";
    		}
    		Intent broadcastIntent = new Intent();
    		broadcastIntent.setAction("SMS_RECEIVED_ACTION");
    		broadcastIntent.putExtra("sms", str);
    		context.sendBroadcast(broadcastIntent);
    	}	

    } 
}