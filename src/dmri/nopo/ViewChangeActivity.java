package dmri.nopo;

import java.util.ArrayList;

import dmri.nopo.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Button;
import android.app.Fragment;

public class ViewChangeActivity extends Fragment {
	static Button alarmButton;
	static Button logButton;
	static Button filterButton;
	static Button menuButton;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        	return inflater.inflate(R.layout.viewchange, container, false);
	} 
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		alarmButton = (Button) getView().findViewById(R.id.alarm);
	    alarmButton.setOnClickListener(new View.OnClickListener() {
	    	@Override
	    	public void onClick(View v) {
	    		SettingsManager.currentView = 0;
	    		Intent intent = new Intent("android.intent.action.ALARM");
	    		startActivity(intent);
	    	}
	    });
	    
	    logButton = (Button) getView().findViewById(R.id.log);
	    logButton.setOnClickListener(new View.OnClickListener() {
	    	@Override
	    	public void onClick(View v) {
	    		SettingsManager.currentView = 1;
	    		Intent intent = new Intent("android.intent.action.LOG");
	    		startActivity(intent);
	    	}
	    });
	    
	    filterButton = (Button) getView().findViewById(R.id.filter);
	    filterButton.setOnClickListener(new View.OnClickListener() {
	    	@Override
	    	public void onClick(View v) {
	    		SettingsManager.currentView = 2;
	    		Intent intent = new Intent("android.intent.action.FILTER");
	    		startActivity(intent);
	    	}
	    });
	    
	    menuButton = (Button) getView().findViewById(R.id.menu);
	    menuButton.setOnClickListener(new View.OnClickListener() {
	    	@Override
	    	public void onClick(View v) {
	    		SettingsManager.currentView = 3;
	    		Intent intent = new Intent("android.intent.action.MENU");
	    		startActivity(intent);
	    	}
	    });
	    //colorButtonsViaArray();
	}
	
	static void colorButtonsViaArray(int focusButton) {
		ArrayList<Button> buttons = new ArrayList<Button>();
		buttons.add(alarmButton);
		buttons.add(logButton);
		buttons.add(filterButton);
		buttons.add(menuButton);
		int index = 0;
		while(index < 4) {
			Button currentButton = buttons.get(index);
			if(index == focusButton) {
				currentButton.setTextColor(-16776961);
			}
			else {
				currentButton.setTextColor(-16777216);
			}
			index++;
		}
	}
}