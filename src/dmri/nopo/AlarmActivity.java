package dmri.nopo;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import dmri.nopo.R;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.database.Cursor;
import android.util.Log;

public class AlarmActivity extends ListActivity {
  private static EfficientAdapter adap;
  static ArrayList<String> timeArray = new ArrayList<String>();
  static ArrayList<String> smsArray = new ArrayList<String>();
  private boolean isReceiving = false;
  private IntentFilter intentFilter;
  
  private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
          String sms = intent.getExtras().getString("sms");
          LogManager log = LogManager.getInstance(context);
          FilterManager filter = FilterManager.getInstance(context);
          NotificationManager manager = NotificationManager.getInstance(context);
          if (filter.isInLocalFilter(sms) && LoginActivity.shouldReceive(intent.getExtras().getString("sender")))
          {
        	  log.writeLogFile(sms);
        	  NotificationManager c = NotificationManager.getInstance(context);
        	  c.alarmNotify();
        	  adap.showSMS();
        	  adap.notifyDataSetChanged();
          }
      }
	};

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.alarm);
    EfficientAdapter newAdap = new EfficientAdapter(this);
    adap = newAdap;
    adap.showSMS();
    setListAdapter(adap);

    
    intentFilter = new IntentFilter();
    intentFilter.addAction("SMS_RECEIVED_ACTION");
  }
  

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    // TODO Auto-generated method stub
    super.onListItemClick(l, v, position, id);
    Toast.makeText(this, "Click-" + String.valueOf(position), Toast.LENGTH_SHORT).show();
  }

  public static class EfficientAdapter extends BaseAdapter implements Filterable {
    private LayoutInflater mInflater;
//    private Bitmap mIcon1;
    private Context context;

    public EfficientAdapter(Context context) {
      // Cache the LayoutInflate to avoid asking for a new one each time.
      mInflater = LayoutInflater.from(context);
      this.context = context;
    }
    
    public void showSMS() {
  	  timeArray.clear();
  	  smsArray.clear();
  	  
    	LogManager log = LogManager.getInstance(context);
    	NotificationManager manager = NotificationManager.getInstance(context);
    	Cursor rows = log.readXUnblockedSMS(LoginActivity.showNumberIncomingSMS);
    	rows.moveToFirst();
    	while(rows.getPosition() < rows.getCount()) {
    		String rawtime = rows.getString(1);
    		String time = rawtime.substring(8, 10) + ":" + rawtime.substring(10, 12) + ":" + rawtime.substring(12, 14);
    		String sms = rows.getString(2);
    		timeArray.add(time);
    		smsArray.add(sms);
    		rows.moveToNext();
    	}
    }

    /**
     * Make a view to hold each row.
     * 
     * @see android.widget.ListAdapter#getView(int, android.view.View,
     *      android.view.ViewGroup)
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
      // A ViewHolder keeps references to children views to avoid
      // unneccessary calls
      // to findViewById() on each row.
      ViewHolder holder;

      // When convertView is not null, we can reuse it directly, there is
      // no need
      // to reinflate it. We only inflate a new View when the convertView
      // supplied
      // by ListView is null.
      if (convertView == null) {
        convertView = mInflater.inflate(R.layout.adaptor_content, null);

        // Creates a ViewHolder and store references to the two children
        // views
        // we want to bind data to.
        holder = new ViewHolder();
        holder.textLine = (TextView) convertView.findViewById(R.id.textLine);
//        holder.iconLine = (ImageView) convertView.findViewById(R.id.iconLine);
        holder.buttonLine = (Button) convertView.findViewById(R.id.buttonLine);
        
        
        convertView.setOnClickListener(new OnClickListener() {
          private int pos = position;

          @Override
          public void onClick(View v) {
            Toast.makeText(context, getItem(position), Toast.LENGTH_LONG).show();    
          }
        });
        

        holder.buttonLine.setOnClickListener(new OnClickListener() {
          private int pos = position;

          @Override
          public void onClick(View v) {
        	  AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        	  alertDialog.setTitle("Blokering af alarm");
        	  alertDialog.setMessage("Vil du blokere alarmen " +smsArray.get(pos) + "?");
        	  alertDialog.setButton(-1, "Ja", new DialogInterface.OnClickListener() {
        	     public void onClick(DialogInterface dialog, int which) {
        	    	 String sms = smsArray.get(pos);
        	    	 Toast.makeText(context, "Blokerer " + sms, Toast.LENGTH_LONG).show();
                     FilterManager f = FilterManager.getInstance(context);
                     f.updateLocalFilter(sms, false);
                   
                     showSMS();
             	     notifyDataSetChanged();
        	     }
        	  });
        	  alertDialog.setButton(-2, "Nej", new DialogInterface.OnClickListener() {
            	 public void onClick(DialogInterface dialog, int which) {
            	 }
        	  });
        	  alertDialog.show();
          }
        });

        convertView.setTag(holder);
      } else {
        // Get the ViewHolder back to get fast access to the TextView
        // and the ImageView.
        holder = (ViewHolder) convertView.getTag();
      }

      // Get flag name and id
      /**
      String filename = "flag_" + String.valueOf(position);
      int id = context.getResources().getIdentifier(filename, "drawable", context.getString(R.string.package_str));

      // Icons bound to the rows.
      if (id != 0x0) {
        //mIcon1 = BitmapFactory.decodeResource(context.getResources(), id);
      }
      */

      // Bind the data efficiently with the holder.
//      holder.iconLine.setImageBitmap(mIcon1);
      String currentTime = Long.toString((DBAdapter.getDateStamp()));
      String alarmTime = timeArray.get(position);
      
      int hourDifference = new Integer(currentTime.substring(8, 10)) - new Integer(alarmTime.substring(0, 2));
      int minuteDifference = new Integer(currentTime.substring(10, 12)) - new Integer(alarmTime.substring(3, 5));
      int secondDifference = new Integer(currentTime.substring(12, 14)) - new Integer(alarmTime.substring(6, 8));
      
      NotificationManager manager = NotificationManager.getInstance(context);
      if(hourDifference * 60 * 60 + minuteDifference * 60 + secondDifference > LoginActivity.highlightTime * 60) {
    	  holder.textLine.setTextColor(-1);
      }
      else {
    	  Typeface tf = Typeface.DEFAULT_BOLD;
    	  holder.textLine.setTypeface(tf);
    	  holder.textLine.setTextColor(-16711936);
      }
      holder.textLine.setText(timeArray.get(position) + "\n" + smsArray.get(position));

      return convertView;
    }

    static class ViewHolder {
      TextView textLine;
 //     ImageView iconLine;
      Button buttonLine;
    }

    @Override
    public Filter getFilter() {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public long getItemId(int position) {
      // TODO Auto-generated method stub
      return 0;
    }
// angiver hvor mange gange getView skal iterere
    @Override
    public int getCount() {
      // TODO Auto-generated method stub
      return smsArray.size();
    }

    @Override
    public String getItem(int position) {
      // TODO Auto-generated method stub
      return smsArray.get(position);
    }

  }
  
 
  
  @Override
  protected void onResume() {
      //---register the receiver---
	  if(!isReceiving){
		  registerReceiver(intentReceiver, intentFilter);
		  isReceiving = true;
	  }
	  adap.showSMS();
	  adap.notifyDataSetChanged();
      super.onResume();
  }
  @Override
  protected void onPause() {
      //---unregister the receiver---
      //unregisterReceiver(intentReceiver);
      super.onPause();
  }
  
  @Override
  protected void onDestroy() {
      //---unregister the receiver---
      unregisterReceiver(intentReceiver);
      isReceiving = false;
      super.onDestroy();
      finish();
  }
  
  //Test methods
  
  public void testReceiveBlockedSms() {
	  Log.w("NOPO", "Blokeret sms modtaget");
  }


}