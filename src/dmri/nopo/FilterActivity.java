package dmri.nopo;

import java.util.ArrayList;
import java.util.Arrays;

import dmri.nopo.R;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
 
public class FilterActivity extends Activity {
	private ListView listView;
 
	static final String[] FRUITS = new String[] { "Apple", "Avocado", "Banana",
			"Blueberry", "Coconut", "Durian", "Guava", "Kiwifruit",
			"Jackfruit", "Mango", "Olive", "Pear", "Sugar-apple" };
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.filter);
		
		// no more this
		// setContentView(R.layout.list_fruit);
 
		// setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, FRUITS));
 
		listView = (ListView) findViewById(R.id.filterListView);
		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, FRUITS));
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
 
}
