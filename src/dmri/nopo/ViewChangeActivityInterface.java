package dmri.nopo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface ViewChangeActivityInterface {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState);
	
	
	public void onActivityCreated(Bundle savedInstanceState);
}
