package nopo.controller;

import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
    	StringBuilder buf = new StringBuilder();
    	Bundle bundle = intent.getExtras();
    	if (bundle != null) {	
    		Object[] pdusObj = (Object[]) bundle.get("pdus"); 
    		SmsMessage[] messages = new SmsMessage[pdusObj.length]; 
    		for (int i = 0; i < messages.length; i++) {
    			SmsMessage message = messages[i];
    			buf.append("Received SMS from  ");
    			buf.append(message.getDisplayOriginatingAddress());
    			buf.append(" - ");
    			buf.append(message.getDisplayMessageBody());
    		}
    	}
    	
    Toast.makeText(context, buf, Toast.LENGTH_LONG).show();	

    } 
}