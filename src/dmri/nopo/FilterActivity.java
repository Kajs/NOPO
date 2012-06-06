package dmri.nopo;

import java.util.ArrayList;

import dmri.nopo.R;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;

 
public class FilterActivity extends Activity {
	private ListView listView;
	private Context context;
	private EditText search;
	private FilterManager f;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter);
		context = getApplicationContext();
		f = FilterManager.getInstance(context);
		Cursor c = f.getLocalFilter();
		createAlarmList(c);
		Log.w("NOPOActivities", "Creating FilterActivity");
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    String sms = (String)((TextView) view).getText();
			    SparseBooleanArray checked = listView.getCheckedItemPositions();
			    boolean bool = checked.get(position);
			    
			    FilterManager f = FilterManager.getInstance(context);
			    f.updateLocalFilter(sms, bool);
				}
			});
		
		search = (EditText) findViewById(R.id.searchField);
		search.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				Cursor c = f.getXFilter(arg0.toString());
				createAlarmList(c);
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}
		});
		}
	
	public void createAlarmList(Cursor c) {
		ArrayList<String> smsColumn = new ArrayList<String>();
		ArrayList<Boolean> blockedColumn = new ArrayList<Boolean>();
		
		while(!c.isAfterLast()) {
			smsColumn.add(c.getString(0));
			if (c.getInt(1) == 1)
			{
				blockedColumn.add(new Boolean(true));
			}
			else {
				blockedColumn.add(new Boolean(false));
			}
			c.moveToNext();
		}
		prepareListview(smsColumn);
		setMarked(blockedColumn);
		
	}
	
	public void prepareListview(ArrayList<String> rows) {
		listView = (ListView) findViewById(R.id.filterListView);
		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, rows));
		listView.setTextFilterEnabled(true);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}
	
	public void setMarked(ArrayList<Boolean> array) {
		for (int i = 0; i < array.size(); i ++)
		{
			listView.setItemChecked(i, array.get(i));
		}
	}
	
	//Test methods
	
	public void testContent() {
		int i = 0;
		while(i < listView.getCount())
		{
			Log.w("NOPO", listView.getItemAtPosition(i).toString());
			i = i + 1;
		}
	}
	
	public void testBlocking() {
	  	Cursor rows = f.getLocalFilter();
	  	while(rows.getPosition() < rows.getCount()) {
	  		String sms = rows.getString(0);
	  		String isBlocked = rows.getString(1);
	  		Log.w("NOPO", sms + ": " + isBlocked);

	  		rows.moveToNext();
	  	}
	  }
	
	public void testCursor(Cursor c) {
		c.moveToFirst();
		int size = c.getCount();
		Log.w("NOPOActivities", "Initial size: " + Integer.toString(size));
		while(c.getPosition() < size) {
			String current = "";
			Log.w("NOPOActivities", "Input size: " + Integer.toString(size));
			current = current + c.getString(0) + ": " + Integer.toString(c.getInt(1));
			Log.w("NOPOActivities", "Pos " + Integer.toString(c.getPosition()) + ": " + current);
			c.moveToNext();
		}
	}
	
	@Override	
	protected void onResume() {
		Log.w("NOPOActivities", "FilterActivity.onResume()");
		f = FilterManager.getInstance(context);
		Cursor c = f.getLocalFilter();
		createAlarmList(c);
		super.onResume();
	}
}