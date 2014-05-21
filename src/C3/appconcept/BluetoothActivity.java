package C3.appconcept;

import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class BluetoothActivity extends ActionBarActivity {
	private BluetoothAdapter mBluetoothAdapter; 			//BluetoothAdapter object represents actual device bluetooth module.
	private static final int REQUEST_ENABLE_BT = 1;
	private Set<BluetoothDevice> pairedDevices;
	
	TextView BTDesc;
	//scanLE device
	/*
	private boolean mScanning;
	private Handler mHandler;
	private static final long SCAN_PERIOD = 1000;
	private void scanLeDevice(final boolean enable){
		
		if(enable){
			mHandler.postDelayed(new Runnable(){
				@Override
				public void run(){
					mScanning = false;
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
				}
			}, SCAN_PERIOD);
			
			mScanning = true;
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
	}*/
	private void bluetoothMethod(){
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		
		Log.d("MT", "Bluetooth activity started -----------");
		Log.d("MT", "getting bluetooth adapter instance.");
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		
		BTDesc = (TextView) findViewById(R.id.BTDesc);
		// Ensures Bluetooth is available on the device and it is enabled. If not,
		// displays a dialog requesting user permission to enable Bluetooth.
		boolean a = mBluetoothAdapter == null;
		boolean b = !mBluetoothAdapter.isEnabled ();
		if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
			BTDesc.setText("BT is enabled");
			Log.d("MT", "Bluetooth is enabled.");
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		    
		    pairedDevices = mBluetoothAdapter.getBondedDevices();
		}
		else
		{
			if(mBluetoothAdapter == null) Log.d("MT", "mBluetoothAdapter is NULL");
			if(!mBluetoothAdapter.isEnabled()) Log.d("MT", "Bluetooth Adapter not enabled.");
			BTDesc.setText("BT is disabled");
			Log.d("MT", "Bluetooth is not enabled or not available.");
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bluetooth, menu);
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



}
