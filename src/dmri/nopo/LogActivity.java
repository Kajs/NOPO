package dmri.nopo;

import dmri.nopo.R;
import android.app.Activity;
import android.os.Bundle;
import android.database.Cursor;
import android.view.Window;
import android.widget.TextView;

public class LogActivity extends Activity {
    

	private TextView showInput;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.log);
        
        showInput = (TextView) findViewById(R.id.showInput);
        NotificationManager manager = new NotificationManager(this);
        LogManager log = LogManager.getInstance(this);
        Cursor c = log.readXLogFile(manager.getNumberIncomingSMSInt());
        c.moveToFirst();
        while(c.getPosition() < c.getCount()) {
        	showInput.setText(c.getString(1));
        	c.moveToNext();
        }
        
    }
    
    
}