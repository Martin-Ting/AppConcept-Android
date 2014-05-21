package C3.appconcept;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActivity extends ActionBarActivity {
	private String APIresponse = null;
	private String apiURL = "http://138.23.220.204:8080/Jelli_deli/jaxrs/menu";
	
	private ImageView mSamTL;
	private ImageView mSamTR;
	private ImageView mSamBL;
	private ImageView mSamBR;
	private ImageView mOrderButton;
	private TextView mOrderDescr;
	private TextView mSamTLText, mSamTRText, mSamBLText, mSamBRText;
	private boolean connectionFailed = false;
	
	private String descText = "";
	
	Vector<Sandwich> menuList = null;
	private int numTL = 0, numTR = 0, numBL = 0, numBR = 0;
	private final int samTLindex = 0;
	private final int samTRindex = 1;
	private final int samBLindex = 2;
	private final int samBRindex = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		//Magic to get Network access in UI Thread
		Log.d("MT", "MenuActivity -- Network Magic occuring...");
		if (android.os.Build.VERSION.SDK_INT > 9) {
		      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		      StrictMode.setThreadPolicy(policy);
		}
		
		Log.d("MT", "MenuActivity -- Calling RESTAPI to get Menu");
		apiCall();
		Log.d("MT", "MenuActivity -- parsing XML if connection succeded.");
		if(connectionFailed == false)
			menuList = parseXML(APIresponse);
		
		//instantiate Views
		//mButton = (Button) findViewById(R.id.buttonMenu);
		//mTextView = (TextView) findViewById(R.id.menuDisplay);
		mSamTL = (ImageView) findViewById(R.id.samTL);
		mSamTR = (ImageView) findViewById(R.id.samTR);
		mSamBL = (ImageView) findViewById(R.id.samBL);
		mSamBR = (ImageView) findViewById(R.id.samBR);
		mOrderButton = (ImageView) findViewById(R.id.orderButton);
		mOrderDescr = (TextView) findViewById(R.id.orderDesc);
		mSamTLText = (TextView) findViewById(R.id.samTLtext);
		mSamTRText = (TextView) findViewById(R.id.samTRtext);
		mSamBLText = (TextView) findViewById(R.id.samBLtext);
		mSamBRText = (TextView) findViewById(R.id.samBRtext);
		//Setting button text
		mSamTLText.setText("\nName: " + menuList.get(samTLindex).name 
		           +"\nPrice: " + menuList.get(samTLindex).price
		           +"\n\"" + menuList.get(samTLindex).description+"\"");
		mSamTRText.setText("\nName: " + menuList.get(samTRindex).name 
				   +"\nPrice: " + menuList.get(samTRindex).price
				   +"\n\"" + menuList.get(samTRindex).description+"\"");
		mSamBLText.setText("\nName: " + menuList.get(samBLindex).name 
				   +"\nPrice: " + menuList.get(samBLindex).price
				   +"\n\"" + menuList.get(samBLindex).description+"\"");
		mSamBRText.setText("\nName: " + menuList.get(samBRindex).name 
				   +"\nPrice: " + menuList.get(samBRindex).price
				   +"\n\"" + menuList.get(samBRindex).description+"\"");
		// END setting button text
		mSamTL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("MT", "TL clicked.");
				updateQuantityAndOutput(samTLindex);
				Log.d("MT", "TL updated.");
			}
		});
		
		mSamTR.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("MT", "TR clicked.");
				updateQuantityAndOutput(samTRindex);
				Log.d("MT", "TR updated.");
			}
		});
		mSamBL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("MT", "BL clicked.");
				updateQuantityAndOutput(samBLindex);
				Log.d("MT", "BL updated.");
			}
		});
		mSamBR.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("MT", "BR clicked.");
				updateQuantityAndOutput(samBRindex);
				Log.d("MT", "BR updated.");
			}
		});
		
		
		mOrderButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MenuActivity.this, ReceiptActivity.class);
				intent.putExtra("TL", numTL);
				intent.putExtra("TR", numTR);
				intent.putExtra("BL", numBL);
				intent.putExtra("BR", numBR);
				intent.putExtra("TLname", menuList.get(samTLindex).name);
				intent.putExtra("TRname", menuList.get(samTRindex).name);
				intent.putExtra("BRname", menuList.get(samBLindex).name);
				intent.putExtra("BLname", menuList.get(samBRindex).name);
				intent.putExtra("TLprice", menuList.get(samTLindex).price);
				intent.putExtra("TRprice", menuList.get(samTRindex).price);
				intent.putExtra("BLprice", menuList.get(samBLindex).price);
				intent.putExtra("BRprice", menuList.get(samBRindex).price);
				startActivity(intent);
			}
		});
		
		/*mButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				mTextView.setText("");
				if(connectionFailed) return;
				for(Sandwich s : menuList){
					mTextView.append(s.name + ": #" + s.price + " | " + s.description);
				}
				Log.d("MT", "Set menu in mTextview");
			}
		});*/
/*        tv = (TextView) findViewById(R.id.menuDisplay);
		Log.d("MT", "Setting textview NOW.");
        tv.setText("");
        for(int i = 0; i < menuList.size(); ++i){
        	String sandwichOP = menuList.get(i).name + ": \n"
        			            + "\tID: " + menuList.get(i).id
        			            + "\n\tDESCR: " + menuList.get(i).description
        			            + "\n\tPRICE: $" + menuList.get(i).price + "\n"; 
        	tv.append(sandwichOP);
        }*/

        
        Log.d("MT", "MenuActivity ready for input.");

	}
	private void updateQuantityAndOutput(int which){
		switch(which){
			case samTLindex:
				numTL++;
				break;
			case samTRindex:
				numTR++;
				break;
			case samBLindex:
				numBL++;
				break;
			case samBRindex:
				numBR++;
				break;
		}
		descText = menuList.get(samTLindex).name + ": " + numTL
				+ "\n" + menuList.get(samTRindex).name + ": " + numTR
				+ "\n" + menuList.get(samBLindex).name + ": " + numBL
				+ "\n" + menuList.get(samBRindex).name + ": " + numBR;
		mOrderDescr.setText(descText);
	}        

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
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
	/* CLASS Sandwich
	 * Description: Represents a menu item sandwich
	 * @params: None. container class.
	 */
	private class Sandwich{
		int id;
		int price;
		String name;
		String description;
	}
	/* readFeed(XmlPullParser parser)
	 * Description: reads feed into sandwich list.
	 * @params: XmlPullParser already initialized.
	 */
	private Vector<Sandwich> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException{
		Log.d("MT", "readFeed -------");
		Vector<Sandwich> menuList = null;
		Sandwich current = null;
			//int limit = 0;
		int eventType = parser.getEventType();
		while(eventType != XmlPullParser.END_DOCUMENT){
			//if(++limit > 1000) {Log.d ("MT", "ITERATION LIMIT REACHED."); break;}
			String currTagName = null;
			//Log.d("MT", "while iteration");
			switch(eventType){
				case XmlPullParser.START_DOCUMENT:
					menuList = new Vector<Sandwich>(10);
					break;
				case XmlPullParser.START_TAG:
					currTagName = parser.getName();
					Log.d("MT", "   Start Tag: " + currTagName);
					if(currTagName.equals("sandwiches")){
						//Log.d("MT", "     New Sandwich");
						current = new Sandwich();
					}else if(current != null){
						if(currTagName.equals("description")){
							//Log.d("MT", "        description");
							current.description = parser.nextText();
						}else if(currTagName.equals("id")){
							//Log.d("MT", "        id");
							current.id = Integer.parseInt(parser.nextText());						
						}else if(currTagName.equals("name")){
							//Log.d("MT", "        name");
							current.name = parser.nextText(); 
						}else if(currTagName.equals("price")){
							//Log.d("MT", "        price");
							current.price = Integer.parseInt(parser.nextText());
						}
					}
					break;
				case XmlPullParser.END_TAG:
					currTagName = parser.getName();
					Log.d("MT", "   End Tag: " + currTagName);
					if(currTagName.equalsIgnoreCase("sandwiches") && current != null){
						//Log.d("MT", "        add");
						menuList.add(current);
					}
					break;
			}
			eventType = parser.next();
		}
		return menuList;
	}
	/* parseXML(String xml)
	 * Description: Stores xml objects into sandwich list
	 * @params: string containing xml
	 */
	private Vector<Sandwich> parseXML(String xml){
		Vector<Sandwich> list = null;
		try{
			Log.d("MT", "parseXML -----------------");
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(new StringReader(xml));
			//parser.nextTag();
			Log.d("MT", "reading feed....");
			list = readFeed(parser);
		}catch(XmlPullParserException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			Log.d("MT", "parseXML has successfully parsed. List size: " + list.size());
			for(int i = 0; i < list.size(); ++i){
				String op = "Sandwich: " + list.get(i).name + " [ " 
							+list.get(i).price + " : " 
							+list.get(i).description + " : "
							+list.get(i).id + " ] ";
				Log.d("MT", op);
			}
		}
		return list;
	}
	/* apiCall()
	 * Description: Calls REST API for menu
	 * @params: NONE
	 */
	private void apiCall(){
		try {
			HttpClient httpclient = new DefaultHttpClient();
			Log.d("MT", "Created HTTPCLIENT");
		    HttpResponse response = httpclient.execute(new HttpGet(apiURL));
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
		        //TextView tv = (TextView) findViewById(R.id.underTV);
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
		} catch (ClientProtocolException e) {
			Log.d("MT", "ClientProtocolException: " + e.getMessage());
			connectionFailed = true;
			e.printStackTrace();
		} catch (IOException e) {
			Log.d("MT", "IOException: " + e.getMessage());
			connectionFailed = true;
			e.printStackTrace();
		}
	}
}
