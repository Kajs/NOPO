package dmri.nopo;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import dmri.nopo.R;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AlarmActivity extends ListActivity {
  private static EfficientAdapter adap;
  static ArrayList<String> timeArray = new ArrayList<String>();
  static ArrayList<String> smsArray = new ArrayList<String>();
  private IntentFilter intentFilter;
  private Context context = this;
  
  private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
          String sms = intent.getExtras().getString("sms");
          LogManager log = LogManager.getInstance(context);
          FilterManager filter = FilterManager.getInstance(context);
          if (filter.isInLocalFiter(sms))
          {
        	  log.writeLogFile(sms);
        	  NotificationManager c = NotificationManager.getInstance(context);
        	  c.alarmNotify();
        	  showSMS();
        	  adap.notifyDataSetChanged();
          }
      }
	};

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.alarm);
    showSMS();
    EfficientAdapter newAdap = new EfficientAdapter(this);
    adap = newAdap;
    setListAdapter(adap);

    
    intentFilter = new IntentFilter();
    intentFilter.addAction("SMS_RECEIVED_ACTION");
  }
  
  public void showSMS() {
	  timeArray.clear();
	  smsArray.clear();
	  
  	LogManager log = LogManager.getInstance(context);
  	NotificationManager manager = NotificationManager.getInstance(context);
  	Cursor rows = log.readXLogFile(manager.getNumberIncomingSMSInt());
  	rows.moveToFirst();
  	while(rows.getPosition() < rows.getCount()) {
  		String rawtime = rows.getString(1);
  		String time = rawtime.substring(0, 2) + ":" + rawtime.substring(2, 4) + ":" + rawtime.substring(4, 6);
  		String sms = rows.getString(2);
  		timeArray.add(time);
  		smsArray.add(sms);
  		//String temp = "" + rows.getString(1).substring(0, 2) + ":" + rows.getString(1).substring(2, 4) + ":" + rows.getString(1).substring(4, 6) + ":\n" + rows.getString(2);
  		rows.moveToNext();
  	}
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
    
    public String concatStringArrayList(ArrayList<String> stringArrayList){
      	int start = 0;
      	int size = stringArrayList.size();
      	String resultString = "";
      	while(start < size){
      		resultString = resultString + stringArrayList.get(start);
      		start = start + 1;
      	}
      	return resultString;
      }
      
      public ArrayList<String> arrayToArrayList(String[] stringArray){
    	  int start = 0;
    	  int size = stringArray.length;
    	  ArrayList <String> resultArray = new ArrayList<String>();
    	  while(start < size){
    		  resultArray.add(stringArray[start]);
    		  start = start + 1;
    	  }
    	  	return resultArray;
    	  
      }
      
      public String arrayToString(int start, String[] array){
    	  int pointer = start;
    	  int length = array.length;
    	  String string = "";
    	  
    	  while(pointer < length - 1){
    		  Toast.makeText(context, "arr: " + array[pointer], Toast.LENGTH_SHORT).show();
    		  string = string + array[pointer];
    		  pointer = pointer + 1;
    	  }
    	  return string;
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
            Toast.makeText(context, "Click-" + String.valueOf(pos), Toast.LENGTH_SHORT).show();    
          }
        });
        

        holder.buttonLine.setOnClickListener(new OnClickListener() {
          private int pos = position;

          @Override
          public void onClick(View v) {
        	  String sms = smsArray.get(pos);
        	  Toast.makeText(context, "Blokerer " + sms, Toast.LENGTH_SHORT).show();
            FilterManager f = FilterManager.getInstance(context);
            f.updateLocalFilter(sms, false);

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
      registerReceiver(intentReceiver, intentFilter);
      //Toast.makeText(getApplicationContext(), "Registering receiver", Toast.LENGTH_LONG).show();
      super.onResume();
  }
  @Override
  protected void onPause() {
      //---unregister the receiver---
      unregisterReceiver(intentReceiver);
      //Toast.makeText(getApplicationContext(), "Unregistering receiver", Toast.LENGTH_LONG).show();
      super.onPause();
  }


}