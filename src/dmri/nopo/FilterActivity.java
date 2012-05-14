package dmri.nopo;

import java.util.ArrayList;

import dmri.nopo.R;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;

 
public class FilterActivity extends Activity {
	private ListView listView;
	private ArrayList<String> smsColumn;
	private ArrayList<Boolean> blockedColumn;
	private Cursor input;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.filter);
		smsColumn = new ArrayList<String>();
		blockedColumn = new ArrayList<Boolean>();
		Toast.makeText(this, "FilterActivity Created", Toast.LENGTH_LONG).show();
		listView = (ListView) findViewById(R.id.filterListView);
		createAlarmList();
		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, smsColumn));
		listView.setTextFilterEnabled(true);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		for (int i = 0; i < blockedColumn.size(); i ++)
		{
			listView.setItemChecked(i, blockedColumn.get(i));
		}
 
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // When clicked, show a toast with the TextView text
			    Toast.makeText(getApplicationContext(),
				((TextView) view).getText(), Toast.LENGTH_SHORT).show();
				}
			});
		}
	
	public void createAlarmList() {
		System.out.println("before");
		input = FilterManager.getInstance(this).getLocalFilter();
		input.moveToFirst();
		System.out.println("after");
		
		while(!input.isAfterLast()) {
			smsColumn.add(input.getString(0));
			if (input.getInt(1) == 1)
			{
				blockedColumn.add(new Boolean(true));
			}
			else {
				blockedColumn.add(new Boolean(false));
			}
			input.moveToNext();
		}
		
		/*
		
		smsColumn.add("Apple");
		smsColumn.add("Avocado");       
		smsColumn.add("Banana");
		smsColumn.add("Blueberry");
		smsColumn.add("Coconut");
		smsColumn.add("Durian");
		smsColumn.add("Guava");
		smsColumn.add("Kiwifruit");
		smsColumn.add("Jackfruit");
		smsColumn.add("Mango");
		smsColumn.add("Olive");
		smsColumn.add("Pear");
		smsColumn.add("Sugar-apple");
		blockedColumn.add(new Boolean(true));
		blockedColumn.add(new Boolean(false));
		blockedColumn.add(new Boolean(true));
		blockedColumn.add(new Boolean(true));
		blockedColumn.add(new Boolean(true));
		blockedColumn.add(new Boolean(true));
		blockedColumn.add(new Boolean(true));
		blockedColumn.add(new Boolean(false));
		blockedColumn.add(new Boolean(true));
		blockedColumn.add(new Boolean(true));
		blockedColumn.add(new Boolean(true));
		blockedColumn.add(new Boolean(true));
		blockedColumn.add(new Boolean(true));
		
		*/
		
	}
}