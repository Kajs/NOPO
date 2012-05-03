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
		
		Toast.makeText(this, "FilterActivity Created", Toast.LENGTH_LONG).show();
		listView = (ListView) findViewById(R.id.filterListView);
		smsColumn = getAlarms();
		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, smsColumn));
		listView.setTextFilterEnabled(true);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setItemChecked(1, true);
 
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // When clicked, show a toast with the TextView text
			    Toast.makeText(getApplicationContext(),
				((TextView) view).getText(), Toast.LENGTH_SHORT).show();
				}
			});
		}
	
	public ArrayList<String> getAlarms() {
		ArrayList<String> output = new ArrayList<String>();
		input = LogManager.getInstance(this).readLogFile();
		input.moveToFirst();
		
		while(!input.isAfterLast()) {
			output.add(input.getString(1));
			input.moveToNext();
		}

		output.add("Apple");
		output.add("Avocado");
/**
		output.add("Banana");
		output.add("Blueberry");
		output.add("Coconut");
		output.add("Durian");
		output.add("Guava");
		output.add("Kiwifruit");
		output.add("Jackfruit");
		output.add("Mango");
		output.add("Olive");
		output.add("Pear");
		output.add("Sugar-apple");
*/
		return output;
	}
}