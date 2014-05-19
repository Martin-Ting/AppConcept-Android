package C3.appconcept;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ReceiptActivity extends ActionBarActivity {
	private String displayStringTL = "", displayStringTR = "", displayStringBL = "", displayStringBR = "";
	private int numTL, numTR, numBL, numBR;
	private String nameTL, nameTR, nameBL, nameBR;
	private int priceTL, priceTR, priceBL, priceBR;
	private TextView confDescTL, confDescTR, confDescBL, confDescBR, orderTotal;
	private Button finishButton;
	
	private final String apiURL = "http://138.23.221.170:8080/Jelli_deli/jaxrs/order";
	private final String targetDomain = "138.23.221.170"; // :8080?
	private String APIresponse = null;
	/*
	 * This api package should be called by using apiCall(generateXML( ... ))
	 */
	private String generateXML(int pTL, int pTR, int pBL, int pBR){
		Log.d("MT", "generateXML---------");
		String returnString = "<sandwichess>";
		if(pTL != 0) returnString += "<sandwiches><description>TLD</description><id>1</id><name>TLN</name><price>"+pTL+"</price></sandwiches>"; 
		if(pTR != 0) returnString += "<sandwiches><description>TRD</description><id>2</id><name>TRN</name><price>"+pTR+"</price></sandwiches>";
		if(pBL != 0) returnString +="<sandwiches><description>BLD</description><id>3</id><name>BLN</name><price>"+pBL+"</price></sandwiches>";
		if(pBR != 0) returnString +="<sandwiches><description>BRD</description><id>4</id><name>BRN</name><price>"+pBR+"</price></sandwiches>";

		returnString += "</sandwichess>";
		Log.d("MT", "generated: " + returnString);
		return returnString;
				
	}
	private void apiCall(String xmlContent){
		Log.d("MT", "apiCall(...) ----------------");
		Log.d("MT", "HTTP Client");
		DefaultHttpClient httpClient = new DefaultHttpClient();
		Log.d("MT", "Setting target Host...");
		HttpHost targetHost = new HttpHost(targetDomain, 8080, "http");
		Log.d("MT", "Creating post connection...");
		HttpPost httpPost = new HttpPost(apiURL);
		Log.d("MT", "Adding headers.");
		//httpPost.addHeader("Accept", "text/xml");
		//httpPost.addHeader("Content-Type", "application/xml");
		
		Log.d("MT", "Writing to host...");
		try{
			Log.d("MT", "Creating POST entity...");
			StringEntity entity = new StringEntity(xmlContent, "UTF-8");
			entity.setContentType("application/xml");
			httpPost.setEntity(entity);
			
			Log.d("MT", "EXECUTING POST.");
			HttpResponse response = httpClient.execute(targetHost, httpPost);
			
			//deal with response
			Log.d("MT", "GOT RESPONSE");
			StatusLine statusLine = response.getStatusLine();
			Log.d("MT", "statusLine recieved");
		    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				Log.d("MT", "HTTP STATUS OK");
		        ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        Log.d("MT", "Wrote response to stream");
		        String responseString = out.toString();
		        
		        Log.d("MT", "Response finished!");
		        Log.d("MT", "Got string: " + responseString);
		        APIresponse = responseString;
		        Log.d("MT", "APIresponse SET");
		    } else{
		        //Closes the connection.
				Log.d("MT", "HTTP STATUS FAILED!!");
		        response.getEntity().getContent().close();
		        throw new IOException(statusLine.getReasonPhrase());
		    }
		}catch (ClientProtocolException e) {
			Log.d("MT", "ClientProtocolException: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.d("MT", "IOException: " + e.getMessage());
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receipt);

		confDescTL = (TextView) findViewById(R.id.confirmDesc01);
		confDescTR = (TextView) findViewById(R.id.confirmDesc02);
		confDescBL = (TextView) findViewById(R.id.confirmDesc03);
		confDescBR = (TextView) findViewById(R.id.confirmDesc04);
		orderTotal = (TextView) findViewById(R.id.orderTotal);
		finishButton = (Button) findViewById(R.id.finish_button);
		
		// Remove TextViews
		confDescTL.setVisibility(View.GONE);
		confDescTR.setVisibility(View.GONE);
		confDescBL.setVisibility(View.GONE);
		confDescBR.setVisibility(View.GONE);
		orderTotal.setVisibility(View.GONE);
		
		
		Intent currIntent = getIntent();
		// Log ========================================================================
		Log.d("MT", "TL: " + currIntent.getIntExtra("TL",0));
		Log.d("MT", "TR: " + currIntent.getIntExtra("TR",0));
		Log.d("MT", "BL: " + currIntent.getIntExtra("BL",0));
		Log.d("MT", "BR: " + currIntent.getIntExtra("BR",0));
		Log.d("MT", "TL: " + currIntent.getStringExtra("TLname"));
		Log.d("MT", "TR: " + currIntent.getStringExtra("TRname"));
		Log.d("MT", "BL: " + currIntent.getStringExtra("BLname"));
		Log.d("MT", "BR: " + currIntent.getStringExtra("BRname"));
		Log.d("MT", "TL: " + currIntent.getIntExtra("TLprice", -1));
		Log.d("MT", "TR: " + currIntent.getIntExtra("TRprice", -1));
		Log.d("MT", "BL: " + currIntent.getIntExtra("BLprice", -1));
		Log.d("MT", "BR: " + currIntent.getIntExtra("BRprice", -1));
		
		// Get Extras ==================================================================
		numTL = currIntent.getIntExtra("TL", -1);
		numTR = currIntent.getIntExtra("TR", -1);
		numBL = currIntent.getIntExtra("BL", -1);
		numBR = currIntent.getIntExtra("BR", -1);
		nameTL = currIntent.getStringExtra("TLname");
		nameTR = currIntent.getStringExtra("TRname");
		nameBL = currIntent.getStringExtra("BLname");
		nameBR = currIntent.getStringExtra("BRname");
		priceTL = currIntent.getIntExtra("TLprice", -1);
		priceTR = currIntent.getIntExtra("TRprice", -1);
		priceBL = currIntent.getIntExtra("BLprice", -1);
		priceBR = currIntent.getIntExtra("BRprice", -1);
		
		// Setting texts if it is ordered =====================================================
		if(numTL !=0 ){
			Log.d("MT", "Displaying TL");
			displayStringTL = nameTL + " [" + numTL + "]: $" + priceTL*numTL + ".00";
			confDescTL.setText(displayStringTL);
			confDescTL.setVisibility(View.VISIBLE);
		}
		if(numTR !=0 ){
			Log.d("MT", "Displaying TR");
			displayStringTR = nameTR + " [" + numTR + "]: $" + priceTR*numTR + ".00";
			confDescTR.setText(displayStringTR);
			confDescTR.setVisibility(View.VISIBLE);
		}
		if(numBL !=0 ){
			Log.d("MT", "Displaying BL");
			displayStringBL = nameBL + " [" + numBL + "]: $" + priceBL*numBL + ".00";
			confDescBL.setText(displayStringBL);
			confDescBL.setVisibility(View.VISIBLE);			
		}
		if(numBR !=0 ){
			Log.d("MT", "Displaying BR");
			displayStringBR = nameBR + " [" + numBR + "]: $" + priceBR*numBR + ".00";
			confDescBR.setText(displayStringBR);
			confDescBR.setVisibility(View.VISIBLE);
		}
		// TODO This is where you do ordertotal code
		apiCall(generateXML(numTL*priceTL, numTR*priceTR, numBL*priceBL, numBR*priceBR));
		orderTotal.setText("Order Total:                                                        $" + APIresponse +".00");
		orderTotal.setVisibility(View.VISIBLE);

		
		finishButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//submit to server
				Intent intent = new Intent(ReceiptActivity.this, MainActivity.class);
				intent.putExtra("splashDisplayed", true);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.receipt, menu);
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
