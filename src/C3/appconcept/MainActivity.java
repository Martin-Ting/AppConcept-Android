package C3.appconcept;



import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	private boolean splashDisplayed = false;
	private Button mMenuButton;
	private Button mFaqButton;
	private Button mBTButton;
	//private String APIresponse = "";
	//private String apiURL = "http://138.23.221.170:8080/Jelli_deli/jaxrs/menu";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        splashDisplayed = getIntent().getBooleanExtra("splashDisplayed", false);
        if(splashDisplayed == false){
            Log.e("MT", "splashDisplayed == false");
        	Intent splash = new Intent(MainActivity.this, SplashActivity.class);
        	splash.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        	startActivity(splash);
        }
        
        //Instantiate button views
        mMenuButton = (Button)findViewById(R.id.menu_button);
        mFaqButton = (Button) findViewById(R.id.faq_button);
        mBTButton = (Button) findViewById(R.id.bluetooth_button);
		Log.d("MT", "MainActivity -- App started");
        //Set Event Listeners for buttons
		mBTButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(MainActivity.this,R.string.BT_toast, Toast.LENGTH_SHORT).show();
				Intent myIntent = new Intent(MainActivity.this, BluetoothActivity.class);
				MainActivity.this.startActivity(myIntent);
			}
		});
        mMenuButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(MainActivity.this,R.string.menu_toast, Toast.LENGTH_SHORT).show();
				Intent myIntent = new Intent(MainActivity.this, MenuActivity.class);
				MainActivity.this.startActivity(myIntent);
			}
		});
		mFaqButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(MainActivity.this, R.string.faq_toast, Toast.LENGTH_SHORT).show();
				Intent myIntent = new Intent(MainActivity.this, FullscreenActivity.class);
				MainActivity.this.startActivity(myIntent);
			}
		});
        
        
        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
   /* public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }*/

}
