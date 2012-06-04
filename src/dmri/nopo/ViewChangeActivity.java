package dmri.nopo;

import dmri.nopo.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Button;
import android.app.Fragment;

public class ViewChangeActivity extends Fragment {
	private Button logButton;
	private Button filterButton;
	private Button menuButton;
	private Button alarmButton;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        	return inflater.inflate(R.layout.viewchange, container, false);
	} 
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
	        this.alarmButton = (Button) getView().findViewById(R.id.alarm);
	        this.alarmButton.setOnClickListener(new View.OnClickListener() {
	        	@Override
	        	public void onClick(View v) {
	            	Intent intent = new Intent("android.intent.action.ALARM");
	                startActivity(intent);
	            }
	        });
	        
	        this.logButton = (Button) getView().findViewById(R.id.log);
	        this.logButton.setOnClickListener(new View.OnClickListener() {
	        	@Override
	        	public void onClick(View v) {
	            	Intent intent = new Intent("android.intent.action.LOG");
	                startActivity(intent);
	            }
	        });
	        this.filterButton = (Button) getView().findViewById(R.id.filter);
	        this.filterButton.setOnClickListener(new View.OnClickListener() {
	        	@Override
	        	public void onClick(View v) {
	            	Intent intent = new Intent("android.intent.action.FILTER");
	                startActivity(intent);
	            }
	        });
	        this.menuButton = (Button) getView().findViewById(R.id.menu);
	        this.menuButton.setOnClickListener(new View.OnClickListener() {
	        	@Override
	        	public void onClick(View v) {
	            	Intent intent = new Intent("android.intent.action.MENU");
	                startActivity(intent);
	            }
	        });      
	}
}