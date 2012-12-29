package org.dennis.AndWeath;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONObject;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;





import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.AudioManager;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Debug;
import android.sax.Element;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebView.PictureListener;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class AndWeathActivity<Picture> extends Activity {
	public static String gldenstr;
	WebView webview;	
    ListView jtclv;
    ArrayList<String> queryString;
//	private String[] lv_arr = {};
//   	private ListView mainListView = null;
//  comment 
    final String SETTING_TODOLIST = "todolist";
    final String SETTING_WEBRADAR = "todowebradar";
    final String SETTING_STCODES = "todosc";
    final int JTC_CODE = 5;    
    final int STC_CODE = 555;    
    final int LISTEN_CODE = 55;

 //   @SuppressWarnings("rawtypes")
//	private ArrayList selectedItems = new ArrayList();
    @SuppressWarnings("rawtypes")
	private ArrayList selectedFSize = new ArrayList();
    int denint[] = new int[11];
    int fntint[] = new int[37];
    int stcint[] = new int[63];
    String stcstr = "US";
    String fntstr = "bml";
    String denstr[] = new String[11];
    TextView city;
    TextView ccond;
    TextView ctemp;
    TextView cwind;
    TextView cdate;
    TextView arrCond[] = new TextView[4];
    TextView arrDay[] = new TextView[4];
    TextView arrLow[] = new TextView[4];
    TextView arrHigh[] = new TextView[4];
    ImageView arrImgd[] = new ImageView[4];   

    String 	dencitystate;
    Button btnRadar;
    Button btnGoogle;
    Button btnUSObs;
    Button speakButton;
    private int MY_DATA_CHECK_CODE = 4321;
    
    private TextToSpeech tts;

    ViewFlipper flip;
    Button btnSearch;
    Button btnListen;
    EditText EditSearch;
    private ListView wordsList;
    private static final int REQUEST_CODE = 1234;
    private static final int AND_CODE = 5678;


    /** Called when the activity is first created. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      	super.onActivityResult(requestCode, resultCode, data);

    	if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
            {
                // Populate the wordsList with the String values the recognition engine thought it heard
                ArrayList<String> matches = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                wordsList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                        matches));
                this.EditSearch.setText(wordsList.getItemAtPosition(0).toString());
                this.btnSearch.performClick();
            }
            
 //           super.onActivityResult(requestCode, resultCode, data);  
    	if (requestCode == JTC_CODE) {
    	      this.LoadSelections();
    	      if (fntstr.contentEquals("usa")){
    	   	      webview.loadUrl("file:///sdcard/DCIM/denweb.html");
//    	    	  webview.lo
//    	   	      webview.loadUrl("file:///Android_res/raw/denweb.html");
    	   	         	      }
    	      else {
    	      webview.loadUrl("http://images.intellicast.com/WxImages/RadarLoop/" + fntstr + "_None_anim.gif");
    	      }     
    	      flip.setDisplayedChild(0);    		
    	}
    	else if (requestCode == STC_CODE) {
  	      this.LoadSTCSelections();
  	      webview.loadUrl("http://vortex.plymouth.edu/obs.php?Qstate=" + stcstr);
  	      flip.setDisplayedChild(0);    		
  	}
    	else if (requestCode != REQUEST_CODE){
        	 initWebR();
//            setContentView(R.layout.main);
            flip.setDisplayedChild(1);
             initBtns(); 
             }


        
        }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
 
        city  = (TextView) findViewById(R.id.city);
        arrCond[0] = (TextView) findViewById(R.id.cond1);
        arrCond[1] = (TextView) findViewById(R.id.cond2);
        arrCond[2] = (TextView) findViewById(R.id.cond3);
        arrCond[3] = (TextView) findViewById(R.id.cond4);
        arrDay[0] = (TextView) findViewById(R.id.day1);
        arrDay[1] = (TextView) findViewById(R.id.day2);
        arrDay[2] = (TextView) findViewById(R.id.day3);
        arrDay[3] = (TextView) findViewById(R.id.day4);
        arrLow[0] = (TextView) findViewById(R.id.low1);
        arrLow[1] = (TextView) findViewById(R.id.low2);
        arrLow[2] = (TextView) findViewById(R.id.low3);
        arrLow[3] = (TextView) findViewById(R.id.low4);
        arrHigh[0] = (TextView) findViewById(R.id.high1);
        arrHigh[1] = (TextView) findViewById(R.id.high2);
        arrHigh[2] = (TextView) findViewById(R.id.high3);
        arrHigh[3] = (TextView) findViewById(R.id.high4);
        arrImgd[0] = (ImageView) findViewById(R.id.imgd1);
        arrImgd[1] = (ImageView) findViewById(R.id.imgd2);
        arrImgd[2] = (ImageView) findViewById(R.id.imgd3);
        arrImgd[3] = (ImageView) findViewById(R.id.imgd4);
        

        ccond = (TextView) findViewById(R.id.ccond);
        cwind = (TextView) findViewById(R.id.cwind);
        ctemp = (TextView) findViewById(R.id.ctemp);
        cdate = (TextView) findViewById(R.id.cdate);
        for (int qq = 0;qq < arrDay.length;qq++){
        	arrDay[qq].setText("");
        	arrLow[qq].setText("");
        	arrCond[qq].setText("");
        	arrHigh[qq].setText("");
        	
        }

        EditSearch = (EditText) findViewById(R.id.editSearch);
        EditSearch.setText("");
        webview = (WebView) findViewById(R.id.webview);

//      flip.setDisplayedChild(0);
        webview.getSettings().setBuiltInZoomControls(true);
      webview.setWebViewClient(new AndWeathClient());
      webview.getSettings().setJavaScriptEnabled(true);
      webview.setPictureListener(new MyPictureListener());

 //     webview.setPictureListener(new PictureListener() {
//
 //         public void onNewPicture(WebView view, Picture picture) {
 //              webview.scrollTo(300, 300);
//
//          }
//      });

      setVolumeControlStream(AudioManager.STREAM_MUSIC);

      this.LoadSelections();
      this.LoadSTCSelections();
      webview.loadUrl("http://images.intellicast.com/WxImages/RadarLoop/" + fntstr + "_None_anim.gif");

        flip=(ViewFlipper)findViewById(R.id.flip);
        flip.setInAnimation(this,android.R.anim.fade_in);
        flip.setOutAnimation(this,android.R.anim.fade_out);
        flip.setDisplayedChild(1);
        initBtns();
        wordsList.setClickable(true);
        wordsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

          @Override
          public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

            Object o = wordsList.getItemAtPosition(position);
            EditSearch.setText(o.toString());
            btnSearch.performClick();
            /* write you handling code like...
            String st = "sdcard/";
            File f = new File(st+o.toString());
            // do whatever u want to do with 'f' File object
            */  
            
          }
        });
      
        
     }
 public void initWund(String denstr,String denstr2){
	 //   	URL url;
     final String updateDailyWeatherForecastUrl = "http://api.wunderground.com/api/d09917472096c37e/forecast3day/q/{latitude},{longitude}.json";
     URL url[] = new URL [4];

     try {
     	
         JSONArray jsonArray =  new JSONArray("[" + denstr.toString() + "]");
         Log.i(AndWeathActivity.class.getName(),
             "Number of entries " + jsonArray.length());
         for (int i = 0; i < jsonArray.length(); i++) {
           JSONObject jsonObject = jsonArray.getJSONObject(i);
           JSONObject currentConditions = jsonObject.getJSONObject("current_observation");
           JSONObject currentCity = currentConditions.getJSONObject("display_location");
            ccond.setText(currentConditions.getString("weather").toString());
           ctemp.setText(currentConditions.getString("temp_f").toString());
           cwind.setText(currentConditions.getString("wind_string").toString());
           city.setText(currentCity.getString("full").toString());
			cdate.setText( currentConditions.getString("observation_time").toString());
           String denstr3 =  getForecast(denstr2.toString());
           JSONArray fjsonArray =  new JSONArray("[" + denstr3.toString() + "]");
           Log.i(AndWeathActivity.class.getName(),
               "Number of entries " + jsonArray.length());
           
           Point denpoint = getDisplaySize (getWindowManager().getDefaultDisplay() );
           int maxiii = 0;
           if (denpoint.x > 599) {
        	   maxiii =4;
        	   arrDay[3].setVisibility(View.VISIBLE);
        	   arrLow[3].setVisibility(View.VISIBLE);
        	   arrHigh[3].setVisibility(View.VISIBLE);
        	   arrCond[3].setVisibility(View.VISIBLE);
        	   arrImgd[3].setVisibility(View.VISIBLE);
           }
           else{
           	   arrDay[3].setVisibility(View.GONE);
        	   arrLow[3].setVisibility(View.GONE);
        	   arrHigh[3].setVisibility(View.GONE);
        	   arrCond[3].setVisibility(View.GONE);
        	   arrImgd[3].setVisibility(View.GONE);
        	   maxiii = 4;
           }
           for (int ii = 0; ii < fjsonArray.length(); ii++) {
        	   JSONObject fjsonObject = new JSONObject(denstr3);
//               JSONObject fjsonObject = fjsonArray.getJSONObject(i);
               JSONArray forecastDays = fjsonObject.getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday");
               int iii = 0;
               while (iii < maxiii){
            	   JSONObject currentDay = forecastDays.getJSONObject(iii); 

            			arrDay[iii].setText((String) currentDay.getJSONObject("date").getString("weekday_short").toString());	
   
            			arrLow[iii].setText((String) currentDay.getJSONObject("low").getString("fahrenheit"));	
 
            			arrHigh[iii].setText((String) currentDay.getJSONObject("high").getString("fahrenheit"));	
  
            			arrCond[iii].setText((String) currentDay.getString("conditions").toString());
            			url[iii]  =   new URL(((String) currentDay.getString("icon_url").toString()) );
           	      	 arrImgd[iii].setImageBitmap(getRemoteImage(url[iii]));        			

 //           		} else if (name.equalsIgnoreCase("icon")) {
 //           			url[i]  = new URL("http://www.google.com" + property.getAttributes().item(0).getNodeValue() );
 //           	      	 arrImgd[i].setImageBitmap(getRemoteImage(url[i]));        			
 //           		}
           	   iii = iii + 1;
               }
               
           //    JSONObject fcurrentForecast = fjsonObject.getJSONObject("forecast");
//               JSONObject fcurrentsForecast = fcurrentForecast.getJSONObject("simpleforecast");
 //              JSONArray  forecastDays = fcurrentsForecast.getJSONArray("forecastday");
           }
    //       jsonObject.toString();
         }
     }catch (Exception e) {
            Log.e(AndWeathActivity.class.toString(), "Failed Parse JSON");
         city.setText("City or Zip Invalid");
       
    }

 }
 @SuppressWarnings("deprecation")
private static Point getDisplaySize(final Display display) {
	    final Point point = new Point();
	    try {
	        display.getSize(point);
	    } catch (java.lang.NoSuchMethodError ignore) { // Older device
	        point.x = display.getWidth();
	        point.y = display.getHeight();
	    }
	    return point;
	}

    public void InitGoogle(String denstr){

 //   	URL url;

        URL url[] = new URL [4];

        try {
        	
        Document doc = XMLfromString(denstr);
        NodeList nodes = doc.getElementsByTagName("forecast_conditions");
//   	    String denstr4 = nodes.item(0).getChildNodes().item(0).getAttributes().getNamedItem("data").toString();
//    	 String denstr4 =  nodes.item(0).getFirstChild().getAttributes().item(0).getNodeValue();
//      	 String denstr6 =  nodes.item(0).getNextSibling().getAttributes().item(0).getNodeValue();
//        URL url1 = new URL("http://www.google.com");
        for (int i=0;i<nodes.getLength();i++){
        	Node item = nodes.item(i);
        	NodeList properties = item.getChildNodes();
        	for (int j=0;j<properties.getLength();j++){
        		Node property = properties.item(j);
        		String name = property.getNodeName();

        		if (name.equalsIgnoreCase("day_of_week")) {
        			arrDay[i].setText(property.getAttributes().item(0).getNodeValue());	
        		} else if (name.equalsIgnoreCase("low")) {
        			arrLow[i].setText(property.getAttributes().item(0).getNodeValue());
        		} else if (name.equalsIgnoreCase("high")) {
        			arrHigh[i].setText(property.getAttributes().item(0).getNodeValue());
        		} else if (name.equalsIgnoreCase("condition")) {
        			arrCond[i].setText(property.getAttributes().item(0).getNodeValue());
        		} else if (name.equalsIgnoreCase("icon")) {
        			url[i]  = new URL("http://www.google.com" + property.getAttributes().item(0).getNodeValue() );
        	      	 arrImgd[i].setImageBitmap(getRemoteImage(url[i]));        			
        		}

        		
        	}
          }

//         URL url1 = new URL("http://www.google.com" + nodes.item(0).getChildNodes().item(3).getAttributes().item(0).getNodeValue() );
//      	 arrImgd[0].setImageBitmap(getRemoteImage(url1));

//         URL url2  = new URL("http://www.google.com" + nodes.item(1).getChildNodes().item(3).getAttributes().item(0).getNodeValue() );
//      	 arrImgd[1].setImageBitmap(getRemoteImage(url2));

//      	 URL url3  = new URL("http://www.google.com" + nodes.item(2).getChildNodes().item(3).getAttributes().item(0).getNodeValue() );
//      	 arrImgd[2].setImageBitmap(getRemoteImage(url3));
        NodeList citynodes = doc.getElementsByTagName("forecast_information");
       	NodeList CurCondnodes = doc.getElementsByTagName("current_conditions");
//      	ccond.setText(doc.getElementsByTagName("current_conditions/condition").item(0).getAttributes().item(0).getNodeValue());
//       	ctemp.setText(doc.getElementsByTagName("current_conditions/temp_f").item(0).getAttributes().item(0).getNodeValue());       	
//       	ccond.setText(doc.getElementsByTagName("current_conditions/wind_conditions").item(0).getAttributes().item(0).getNodeValue());
       	for (int i=0;i<CurCondnodes.getLength();i++){
        	Node item = CurCondnodes.item(i);
        	NodeList properties = item.getChildNodes();

       	for (int j=0;j<properties.getLength();j++){
    		Node property = properties.item(j);
    		String name = property.getNodeName();

    		if (name.equalsIgnoreCase("condition")) {
    			ccond.setText(property.getAttributes().item(0).getNodeValue());	
    		} else if (name.equalsIgnoreCase("temp_f")) {
    			ctemp.setText(property.getAttributes().item(0).getNodeValue());
    		} else if (name.equalsIgnoreCase("wind_condition")) {
    			cwind.setText(property.getAttributes().item(0).getNodeValue());
    		}
       	}
    		
    	}

//      	ccond.setText(CurCondnodes.item(0).getChildNodes().item(0).getAttributes().item(0).getNodeValue());
//      	ctemp.setText(CurCondnodes.item(0).getChildNodes().item(1).getAttributes().item(0).getNodeValue());
//      	cwind.setText(CurCondnodes.item(0).getChildNodes().item(5).getAttributes().item(0).getNodeValue());
     	city.setText(citynodes.item(0).getChildNodes().item(0).getAttributes().item(0).getNodeValue().trim()); 
     	cdate.setText(citynodes.item(0).getChildNodes().item(5).getAttributes().item(0).getNodeValue().trim());
//     	ctemp.append(" " + cwind.getText().toString().substring(5));
//     			+ " " + ccond.getText().toString().trim()
//			+ " " + ctemp.getText().toString().trim()
// 			+ " " + cwind.getText().toString().trim());
     	

    }catch (Exception e) {
        city.setText("City or Zip Invalid");
      
   }
//      	 String denstr6 =  nodes.item(0).getChildNodes().item(1).getAttributes().item(0).getNodeValue();
	        //fill in the list items from the XML document
//      for (int i = 0; i < nodes.getLength(); i++) {
//	            HashMap<string, string=""> map = new HashMap<string, string="">(); 
//	 	   if (i == 0) {
//	            Element e = (Element)nodes.item(i);
	           
//	            String denstr1 = e.getChild("low").
// 	            Toast.makeText(this, "string=" + denstr4 + denstr6, Toast.LENGTH_LONG).show();

//	            String denstr2 = XMLfunctions.getValue(e, "name");
//	            String denstr3 = XMLfunctions.getValue(e, "tempf");

//	 	             }
//	 	  } 
 	
    }
    public void initBtns(){
        btnRadar = (Button) findViewById(R.id.btnRadar);
        btnRadar.setOnClickListener(new OnClickListener() {

           public void onClick(View v) {

 //               Toast.makeText(getApplicationContext(),
 //                       " You clicked Back button", Toast.LENGTH_SHORT).show();

 //               setResult(Activity.RESULT_OK);
 //         intnFont();
     	      webview.loadUrl("http://images.intellicast.com/WxImages/RadarLoop/" + fntstr + "_None_anim.gif");
     	      flip.setDisplayedChild(0);    		

           }     
        });   
        btnGoogle = (Button) findViewById(R.id.bntGoogle);
        btnGoogle.setOnClickListener(new OnClickListener() {

           public void onClick(View v) {

 //               Toast.makeText(getApplicationContext(),
 //                       " You clicked Back button", Toast.LENGTH_SHORT).show();

 //               setResult(Activity.RESULT_OK);
        intnGoogle("03845");
            
           }     
        });   
        btnUSObs = (Button) findViewById(R.id.btnNSWObs);
        btnUSObs.setOnClickListener(new OnClickListener() {

           public void onClick(View v) {

 //               Toast.makeText(getApplicationContext(),
 //                       " You clicked Back button", Toast.LENGTH_SHORT).show();

 //               setResult(Activity.RESULT_OK);
       	      webview.loadUrl("http://vortex.plymouth.edu/obs.php?Qstate=" + stcstr);
      	      flip.setDisplayedChild(0);    		

            
           }     
        });
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

  //               Toast.makeText(getApplicationContext(),
  //                       " You clicked Back button", Toast.LENGTH_SHORT).show();

  //               setResult(Activity.RESULT_OK);
         String denstr =  EditSearch.getText().toString();
         intnGoogle(denstr);
             
            }     
         });   

        speakButton = (Button) findViewById(R.id.speakButton);
        
        wordsList = (ListView) findViewById(R.id.list);
 
        // Disable button if no recognition service is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0)
        {
            speakButton.setEnabled(false);
            speakButton.setText("No Snd");
        }
        btnListen = (Button) findViewById(R.id.listenButton);   
        btnListen.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

  //               Toast.makeText(getApplicationContext(),
  //                       " You clicked Back button", Toast.LENGTH_SHORT).show();

  //               setResult(Activity.RESULT_OK);
            	
//              String restrr =        	cgmtedit(cdate.getText().toString());
             String restrr =        	cdate.getText().toString();
             String cname;
             if (isnumeric(EditSearch.getText().toString()) == true ){
            	cname = dencitystate.substring(0,dencitystate.length()-4).toLowerCase() + " " + lustatename(dencitystate);
             }
             else{
            	 cname = EditSearch.getText().toString().substring(0,EditSearch.length()-5) + " " + lustatename(EditSearch.getText().toString()) ;
             }
         gldenstr = 
        	  " The Current Weather for " 
        	 + cname 
        	 + " at " + restrr
        	 + " is " + ccond.getText()
             + " with a Temperature of " + ctemp.getText()
//             + "and the wind out of the " + getWind( cwind.getText())       	
             + ". Today's Weather for " 
        	 + cname
        	 + " will be " + arrCond[0].getText()
             + " with a High of " + arrHigh[0].getText()
             + " and a Low of " + arrLow[0].getText()
             + ". Tommorrow's Weather for " 
        	 + cname
        	 + " will be " + arrCond[1].getText()
             + " with a High of " + arrHigh[1].getText()
             + " and a Low of " + arrLow[1].getText()
             + ". " +  getDOW(arrDay[2].getText()) 
             + "'s Weather for " 
        	 + cname
        	 + " will be " + arrCond[2].getText()
             + " with a High of " + arrHigh[2].getText()
             + " and a Low of " + arrLow[2].getText()
             + ". " +  getDOW(arrDay[3].getText()) 
             + "'s Weather for " 
        	 + cname
        	 + " will be " + arrCond[3].getText()
             + " with a High of " + arrHigh[3].getText()
             + " and a Low of " + arrLow[3].getText()
             ;
//         TextView ltext =    (TextView) findViewById(R.id.listentext);
 //        ltext.setText(gldenstr);
//         Toast.makeText(getApplicationContext(), gldenstr, Toast.LENGTH_LONG).show();
//	       String text = inputText.getText().toString();
             initListen();
	   }
	      });
	   
	         
	    }
public String lustatename (String stcode){
	int i=0;
	
    String[] myStTable = getResources().getStringArray(R.array.denstnames);

	while (i < myStTable.length){
		if (myStTable[i].substring(myStTable[i].length()-2).toLowerCase().endsWith(stcode.substring(stcode.length()-2).toLowerCase()))
			return myStTable[i].substring(0,myStTable[i].length() -2).toLowerCase();
		i = i + 1;
	}
    return "";
}
public String getWind (CharSequence wind){
    String denstr = " ";
    String CompStr = " ";
    int j = 0;
    int i = 6;
    XmlResourceParser todolistXml = getResources().getXml(R.layout.winddir );
   for (i=6;!denstr.equalsIgnoreCase(wind.toString().substring(i, i+1) );i++){
	   j=i+1;
   }
   CompStr = wind.toString().substring(6, j);
    int eventType = -1;
    while (eventType != XmlResourceParser.END_DOCUMENT) {
         if (eventType == XmlResourceParser.START_TAG) {

            String strNode = todolistXml.getName();
            if (strNode.equalsIgnoreCase("ITEM")) {
            	if( CompStr.equalsIgnoreCase(todolistXml.getAttributeValue(null, "title").toString().substring(24).trim())){
            		return (String) todolistXml.getAttributeValue(null, "title").toString().substring(0,23).trim() + " " + wind.subSequence(j+1, wind.length());
            	};
            }
        }



        try {
            eventType = todolistXml.next();
        } catch (XmlPullParserException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         } catch (IOException e) {
            // TODO Auto-generated catch block
             e.printStackTrace();

        }
     }
	
    return (String) todolistXml.getAttributeValue(null, "title").toString().substring(0,23).trim() + " " + wind.subSequence(j+1, wind.length());
}
public String getDOW (CharSequence day3){
	String denret;
	denret ="";
	if (day3.toString().equals("Sun")) {
		denret = "Sunday";
	}else	
		if (day3.toString().equals("Mon")) {
			denret = "Monday";
		}else
			if (day3.toString().equals("Tue")){
				denret = "Tuesday";
			}else
				if (day3.toString().equals("Wed")) {
					denret = "Wednesday";
				}else
					if (day3.toString().equals("Thu")) {
						denret = "Thursday";
					}else
						if (day3.toString().equals("Fri")) {
							denret = "Friday";
						}else
							if (day3.toString().equals("Sat")) {
								denret = "Saturday";
							}
return denret;
	
		
	
}

	  public String cgmtedit(String day3){
      String s = (String) day3.toString().substring(0,19);

	  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  df.setTimeZone(TimeZone.getTimeZone("GMT"));
	  Date timestamp = null;
	  try {
	   try {
		timestamp =  df.parse(s);
	} catch (java.text.ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	      df.setTimeZone(TimeZone.getDefault());
	      System.out.println(df.format(timestamp));
	  } catch (ParseException e) {
	      e.printStackTrace();
	  }
	  SimpleDateFormat dff = new SimpleDateFormat("hh:mma");  
	  return dff.format(timestamp);
	  }
/*	  date.setYear((Integer.valueOf( day3.toString().substring(0, 4)))); 
	  date.setMonth((Integer.valueOf( day3.toString().substring(5, 7)))-1); 
	  date.setDate((Integer.valueOf( day3.toString().substring(8, 10)))); 
	  date.setHours((Integer.valueOf( day3.toString().substring(11, 13)))); 
	  date.setMinutes((Integer.valueOf( day3.toString().substring(14, 16)))); 
	  date.setSeconds((Integer.valueOf( day3.toString().substring(17, 19)))); 
     
	  SimpleDateFormat estFormat = new SimpleDateFormat();
	  SimpleDateFormat gmtFormat = new SimpleDateFormat();
	  TimeZone gmtTime = TimeZone.getTimeZone("GMT");
	  TimeZone estTime = TimeZone.getTimeZone("EST");
	  estFormat.setTimeZone(estTime);
	  gmtFormat.setTimeZone(gmtTime);
	  System.out.println("EST Time: " + estFormat.format(date));
	  System.out.println("GMT Time: " + gmtFormat.format(date));
	  return estFormat.format(date);
	  }
	
  */ 


	/**
     * Handle the action of the button being clicked
     */
    public void initListen(){
        Intent intentListen = new Intent(this, TtsActivity.class);
        startActivityForResult(intentListen,LISTEN_CODE);
	
    }
    public void speakButtonClicked(View v)
    {
        startVoiceRecognitionActivity();
    }
    private void startVoiceRecognitionActivity()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Whose weather do you want?");
        startActivityForResult(intent, REQUEST_CODE);
    }
 
    /**
     * Handle the results from the voice recognition activity.
     */
    public void intnUSObs(String USState){
        Intent intentFont = new Intent(this, StateCodes.class);
        startActivityForResult(intentFont,STC_CODE);
   	
    }
    public void intnFont(){
        Intent intentFont = new Intent(this, WebRadar.class);
        startActivityForResult(intentFont,JTC_CODE);
    }
    public void initWebR(){
 //       setContentView(R.layout.main);

    }
    public boolean isnumeric (String denstr){
    	char denc = denstr.charAt(0);
    	if ((denc < '0') ||
    	   (denc > '9')){
    		return false;
    	}
    		else{
    		return true;
    		}
    	}
    public String ziptastic(String denstr2){
//    String dencityzip;
    final String updateDailyWeatherForecastUrl = "http://zip.elevenbasetwo.com/?zip=" + denstr2;
   	String line = null;


    StringBuilder builder = new StringBuilder();
//	String denstr = "http://www.google.com/ig/api?weather=" + StrS;	 
//	String denstr = "http://api.wunderground.com/api/87a0b809020ad1fe/conditions/q/CA/San_Francisco.json";	 
//	String denstr = "http://api.wunderground.com/api/87a0b809020ad1fe/conditions/q/" + State  + "/" + City + ".json";	 
		        try {
		 
		            DefaultHttpClient httpClient = new DefaultHttpClient();
//	                String requestString = updateDailyWeatherForecastUrl.replace("{latitude}", latitude.toString());
//	                requestString = requestString.replace("{longitude}", longitude.toString());

		            HttpGet httpGet = new HttpGet(updateDailyWeatherForecastUrl.replace(" ", "%20"));
//	                    DefaultHttpClient httpClient = new DefaultHttpClient();
		 
		            HttpResponse response = httpClient.execute(httpGet);
		            StatusLine statusLine = response.getStatusLine();
		            int statusCode = statusLine.getStatusCode();
		            if (statusCode == 200) {
		                HttpEntity entity = response.getEntity();
		                InputStream content = entity.getContent();
		                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
		              
		                while ((line = reader.readLine()) != null) {
		                  builder.append(line);
		                }
		                String Strs =		        builder.toString();
		                String State = Strs.substring(Strs.length()-22,Strs.length()-19).toUpperCase();
		                //string fcity = Strs.
		                //String City =  Strs.substring(37,Strs.length()-2);
		                int i;
		                int j;
		                for (i = 11; Strs.substring(i, i+1).startsWith(":")==false;i++){
		                	   j=i+1;
		                }
		                //
		                j=i;
		                i=11;
		                String City = Strs.substring(i-1,j-10);


		                    return City + ", " + State;
//   		            HttpEntity httpEntity = response.getEntity();
//   		            line = EntityUtils.toString(httpEntity);
		            } else {
		                Log.e(AndWeathActivity.class.toString(), "Failed to download file");
		              }

		        } catch (UnsupportedEncodingException e) {
		            line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
		        } catch (MalformedURLException e) {
		            line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
		        } catch (IOException e) {
	                    line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
		        }
                return "Zip not Found";

    }
    public void intnGoogle(String StrSS){
		String citystate = null;
		String ocitystate = "abcdefff";
	    ArrayList<String> matches = new ArrayList<String>();
    	String denstr2 = StrSS;
    	if (denstr2.trim().equals("")){
/*           	matches.add("Hurricane, UT");
           	matches.add("Intervale, NH");
           	matches.add("Las Vegas, NV");
           	matches.add("Saint George, UT");
           	matches.add("Mesquite, NV");
           	matches.add("Danvers, MA");
           	matches.add("Lake Placid, NY");
*/
    		InputStream stream = getResources().openRawResource(R.raw.favweath); 
           	String dentext = convertStreamToString (stream,matches);
            wordsList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    matches));
           }
            
 //           else {

else {
   	
    	if (isnumeric(denstr2) == true){
    	dencitystate = ziptastic(denstr2);
        String denstr = getJSON(dencitystate);
        initWund(denstr,dencitystate);
    	}
    	else{
    	if (denstr2.contains(", ")){
        String denstr = getJSON(denstr2);
        initWund(denstr,denstr2);
    	}
    	else{
 //           if (denstr2.trim() == ""){
 //           	matches.add("Hurricane, UT");
 //           	matches.add("intervale, NH");
 //           	matches.add("Las Vegas, NV");
 //           }
            
 //           else {
            String denstrbld = getMJSON(denstr2.trim());
            String denstr = denstrbld.toString();
 //           } 

            
            try {

                JSONArray jsonArray =  new JSONArray("[" + denstr.substring(14).toString() + "]");
                Log.i(AndWeathActivity.class.getName(),
                    "Number of entries " + jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                  JSONObject jsonObject = jsonArray.getJSONObject(i);
//                  JSONObject currentState = jsonObject.getJSONObject("State");
//                  JSONObject currentCity = jsonObject.getJSONObject("City");
                  String State = jsonObject.getString("State").toString();
                  String City = jsonObject.getString("City").toString();
                 
                  String Zip =  jsonObject.getString("Zipcode").toString();
 //                 jsonObject.getString("State").toString();
                  citystate = City.toString() + ", " + State.toString();
if (!citystate.equals(ocitystate.toString())){
	              matches.add(citystate.toString());}
                  ocitystate = citystate;
                  if (i==0){
                	  denstr = getJSON(citystate);
                	  denstr2 = citystate;
                  }
          		
 /*                  ccond.setText(currentConditions.getString("weather").toString());
                  ctemp.setText(currentConditions.getString("temp_f").toString());
                  cwind.setText(currentConditions.getString("wind_string").toString());
                  city.setText(currentCity.getString("full").toString());
       			cdate.setText( currentConditions.getString("observation_time").toString());
       			*/
                }
                }catch (Exception e) {
                    Log.e(AndWeathActivity.class.toString(), "Failed Parse JSON");
                 city.setText("City or Zip Invalid");
           
            }
                wordsList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                        matches));

            initWund(denstr,denstr2);      
    	}
            }
    	}

 //       InitGoogle(denstr);
        flip.setDisplayedChild(1);    
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

    MenuInflater inflater = getMenuInflater();

    inflater.inflate(R.layout.denmenu , menu);

    return true;

    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
 //    	TextView text = (TextView)findViewById(R.id.footer);

            switch (item.getItemId()) {

                case R.id.obs:
     //           text.setText(String.format("Selected item %s", item.getTitle()));
                Intent intent = new Intent(this, StateCodes.class);
                startActivityForResult(intent,STC_CODE);
                    
                return true;

                case R.id.radar:
                    Intent intentFont = new Intent(this, WebRadar.class);
                    startActivityForResult(intentFont,JTC_CODE);

     //           text.setText(String.format("Selected item %s", item.getTitle()));  
                
                return true;
              
                case R.id.gweather:
           	        initWebR();
//              setContentView(R.layout.main);
                    flip.setDisplayedChild(1);
                return true;
                
                case R.id.addweath:
           	        initWebR();
//              setContentView(R.layout.main);
                    flip.setDisplayedChild(1);
                return true;
                
                case R.id.exit:
                	
               this.finish();
     //           text.setText(String.format("Selected item %s", item.getTitle()));  
                
                return true;
               

                default:
                return true;
        }

    }
    
    private void LoadSelections() {
        // if the selections were previously saved load them


    	  for (int ii = 0 ;ii < fntint.length ; ii++ )
    		  fntint[ii] = 0;
          fntint[0] = 1;    	  


        SharedPreferences settingsfActivity = getSharedPreferences("WebRadar",MODE_WORLD_WRITEABLE
        );
  	  for (int ii = 0 ;ii < fntint.length ; ii++ )
		  fntint[ii] = 0;

  	  if (settingsfActivity.contains(SETTING_WEBRADAR)){
            String savedItems = settingsfActivity
            .getString(SETTING_WEBRADAR, "");
            this.selectedFSize.clear();
            this.selectedFSize.addAll(Arrays.asList(savedItems.split(",")));
            
            ArrayList<String> listFSIZE =  PrepareFontFromXml ();
         
              if (this.selectedFSize.size() > 0)
          	  for (int ii = 0 ;ii < listFSIZE.size() ; ii++ )

          	  	  if (selectedFSize.contains(listFSIZE.get(ii).toString()))   {
                  fntstr = listFSIZE.get(ii).toString().substring(0, 3);
          		  fntint[ii]  = 1;
          	  
            }
          	  	  else {

        	  		  
          	  	  }
        }
    }
    private void LoadSTCSelections() {
        // if the selections were previously saved load them


    	  for (int ii = 0 ;ii < stcint.length ; ii++ )
    		  stcint[ii] = 0;
          stcint[0] = 1;    	  


        SharedPreferences settingsfActivity = getSharedPreferences("StateCodes",MODE_WORLD_WRITEABLE
        );
  	  for (int ii = 0 ;ii < stcint.length ; ii++ )
		  stcint[ii] = 0;

  	  if (settingsfActivity.contains(SETTING_STCODES)){
            String savedItems = settingsfActivity
            .getString(SETTING_STCODES, "");
            this.selectedFSize.clear();
            this.selectedFSize.addAll(Arrays.asList(savedItems.split(",")));
            
            ArrayList<String> listFSIZE =  PrepareSTCFromXml ();
         
              if (this.selectedFSize.size() > 0)
          	  for (int ii = 0 ;ii < listFSIZE.size() ; ii++ )

          	  	  if (selectedFSize.contains(listFSIZE.get(ii).toString()))   {
                  stcstr = listFSIZE.get(ii).toString().substring(22, 24);
          		  stcint[ii]  = 1;
          	  
            }
          	  	  else {

        	  		  
          	  	  }
        }
    }
    public static String getForecast(String Strs){

    	String line = null;
       	String State = Strs.substring(Strs.length() -2 ).toUpperCase();
    	String City =  Strs.substring(0,Strs.length() - 4);

        StringBuilder builder = new StringBuilder();
        final String updateDailyWeatherForecastUrl = "http://api.wunderground.com/api/d09917472096c37e/forecast/q/" + State  + "/" + City + ".json";       
//   	String denstr = "http://www.google.com/ig/api?weather=" + StrS;	 
//    	String denstr = "http://api.wunderground.com/api/87a0b809020ad1fe/conditions/q/CA/San_Francisco.json";	 
//    	String denstr = "http://api.wunderground.com/api/87a0b809020ad1fe/conditions/q/" + State  + "/" + City + ".json";	 
    		        try {
    		 
    		            DefaultHttpClient httpClient = new DefaultHttpClient();
//    	                String requestString = updateDailyWeatherForecastUrl.replace("{latitude}", latitude.toString());
//    	                requestString = requestString.replace("{longitude}", longitude.toString());
 
    		            HttpGet httpGet = new HttpGet(updateDailyWeatherForecastUrl.replace(" ", "%20"));
//   	                    DefaultHttpClient httpClient = new DefaultHttpClient();
   		 
    		            HttpResponse response = httpClient.execute(httpGet);
    		            StatusLine statusLine = response.getStatusLine();
    		            int statusCode = statusLine.getStatusCode();
    		            if (statusCode == 200) {
    		                HttpEntity entity = response.getEntity();
    		                InputStream content = entity.getContent();
    		                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
    		              
    		                while ((line = reader.readLine()) != null) {
    		                  builder.append(line);
    		                }

 //   		            HttpEntity httpEntity = response.getEntity();
 //   		            line = EntityUtils.toString(httpEntity);
    		            } else {
   		                Log.e(AndWeathActivity.class.toString(), "Failed to download file");
    		              }

    		        } catch (UnsupportedEncodingException e) {
    		            line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
    		        } catch (MalformedURLException e) {
    		            line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
    		        } catch (IOException e) {
    	                    line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
    		        }
    		 
    		        return builder.toString();
    		 
   	
    }
    public static String getJSON(String Strs){
    	String line = null;
    	String State = Strs.substring(Strs.length() -2 ).toUpperCase();
    	String City =  Strs.substring(0,Strs.length() - 4);
        StringBuilder builder = new StringBuilder();
        
//   	String denstr = "http://www.google.com/ig/api?weather=" + StrS;	 
//    	String denstr = "http://api.wunderground.com/api/87a0b809020ad1fe/conditions/q/CA/San_Francisco.json";	 
    	String denstr = "http://api.wunderground.com/api/87a0b809020ad1fe/conditions/q/" + State  + "/" + City + ".json";	 
    		        try {
    		 
    		            DefaultHttpClient httpClient = new DefaultHttpClient();
    		            HttpGet httpGet = new HttpGet(denstr.replace(" ", "%20"));
    		 
    		            HttpResponse response = httpClient.execute(httpGet);
    		            StatusLine statusLine = response.getStatusLine();
    		            int statusCode = statusLine.getStatusCode();
    		            if (statusCode == 200) {
    		                HttpEntity entity = response.getEntity();
    		                InputStream content = entity.getContent();
    		                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
    		              
    		                while ((line = reader.readLine()) != null) {
    		                  builder.append(line);
    		                }

 //   		            HttpEntity httpEntity = response.getEntity();
 //   		            line = EntityUtils.toString(httpEntity);
    		            } else {
   		                Log.e(AndWeathActivity.class.toString(), "Failed to download file");
    		              }

    		        } catch (UnsupportedEncodingException e) {
    		            line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
    		        } catch (MalformedURLException e) {
    		            line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
    		        } catch (IOException e) {
    	                    line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
    		        }
    		 
    		        return builder.toString();
    		 
    		}
   
    public static String getMJSON(String Strs){
    	String line = null;
    	String updenstr = Strs.toUpperCase();
    	String State = Strs.substring(Strs.length() -2 ).toUpperCase();
    	String City =  Strs.substring(0,Strs.length() - 4);
        StringBuilder builder = new StringBuilder();
        
//   	String denstr = "http://www.google.com/ig/api?weather=" + StrS;	 
//    	String denstr = "http://api.wunderground.com/api/87a0b809020ad1fe/conditions/q/CA/San_Francisco.json";	 
    	String denstr = "http://gomashup.com/json.php?fds=geo/usa/zipcode/city/" + updenstr + "&jsoncallback=?";	 
    		        try {
    		 
    		            DefaultHttpClient httpClient = new DefaultHttpClient();
    		            HttpGet httpGet = new HttpGet(denstr.replace(" ", "%20"));
    		 
    		            HttpResponse response = httpClient.execute(httpGet);
    		            StatusLine statusLine = response.getStatusLine();
    		            int statusCode = statusLine.getStatusCode();
    		            if (statusCode == 200) {
    		                HttpEntity entity = response.getEntity();
    		                InputStream content = entity.getContent();
    		                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
    		              
    		                while ((line = reader.readLine()) != null) {
    		                  builder.append(line);
    		                }

 //   		            HttpEntity httpEntity = response.getEntity();
 //   		            line = EntityUtils.toString(httpEntity);
    		            } else {
   		                Log.e(AndWeathActivity.class.toString(), "Failed to download file");
    		              }

    		        } catch (UnsupportedEncodingException e) {
    		            line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
    		        } catch (MalformedURLException e) {
    		            line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
    		        } catch (IOException e) {
    	                    line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
    		        }
    		 
    		        return builder.toString();
    		 
    		}
   
   
    public static String getXML(String StrS){
    	String line = null;

    	String denstr = "http://www.google.com/ig/api?weather=" + StrS;	 
 //   	String denstr = "http://api.wunderground.com/api/87a0b809020ad1fe/conditions/q/CA/San_Francisco.json";	 
    		        try {
    		 
    		            DefaultHttpClient httpClient = new DefaultHttpClient();
    		            HttpGet httpGet = new HttpGet(denstr.replace(" ", "%20"));
   		 
    		            HttpResponse response = httpClient.execute(httpGet);
    		            HttpEntity httpEntity = response.getEntity();
    		            line = EntityUtils.toString(httpEntity);
    		       

    		        } catch (UnsupportedEncodingException e) {
    		            line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
    		        } catch (MalformedURLException e) {
    		            line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
    		        } catch (IOException e) {
    	                    line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
    		        }
    		 
    		        return line;
    		 
    		}
    public Document XMLfromString(String xml){
   	 
	    Document doc = null;
	 
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        try {
	 
	        DocumentBuilder db = dbf.newDocumentBuilder();
 
	        InputSource is = new InputSource();
	            is.setCharacterStream(new StringReader(xml));
	            doc = db.parse(is);
	 
	        } catch (ParserConfigurationException e) {
	            System.out.println("XML parse error: " + e.getMessage());
	            return null;
	        } catch (SAXException e) {
	            System.out.println("Wrong XML file structure: " + e.getMessage());
	            return null;
	        } catch (IOException e) {
	            System.out.println("I/O exeption: " + e.getMessage());
	            return null;
	        }
	 
	        return doc;
	 
	    }
    public Bitmap getRemoteImage(final URL aURL) {
        try {
                final URLConnection conn = aURL.openConnection();
                conn.connect();
                final BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                final Bitmap bm = BitmapFactory.decodeStream(bis);
                bis.close();

                return bm;
        } catch (IOException e) {
                Log.d("DEBUGTAG", "Oh noooz an error...");
        }
        return null;
}

    private ArrayList<String> PrepareFontFromXml() {
        ArrayList<String> todoItems = new ArrayList<String>();
       XmlResourceParser todolistXml = getResources().getXml(R.layout.todofsize );

       int eventType = -1;
       while (eventType != XmlResourceParser.END_DOCUMENT) {
            if (eventType == XmlResourceParser.START_TAG) {

               String strNode = todolistXml.getName();
               if (strNode.equals("ITEM")) {
                   todoItems.add(todolistXml.getAttributeValue(null, "title"));
               }
           }



           try {
               eventType = todolistXml.next();
           } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
               // TODO Auto-generated catch block
                e.printStackTrace();

           }
        }

        return todoItems;
    }
    private static String convertStreamToString(InputStream is, ArrayList<String> matches) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
                matches.add(line);
            }
        } catch (IOException e) {
            Log.w("LOG", e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.w("LOG", e.getMessage());
            }
        }
        return sb.toString();
    }
    
    private ArrayList<String> PrepareSTCFromXml() {
        ArrayList<String> todoItems = new ArrayList<String>();
       XmlResourceParser todolistXml = getResources().getXml(R.layout.statecodes );

       int eventType = -1;
       while (eventType != XmlResourceParser.END_DOCUMENT) {
            if (eventType == XmlResourceParser.START_TAG) {

               String strNode = todolistXml.getName();
               if (strNode.equals("ITEM")) {
                   todoItems.add(todolistXml.getAttributeValue(null, "title"));
               }
           }



           try {
               eventType = todolistXml.next();
           } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
               // TODO Auto-generated catch block
                e.printStackTrace();

           }
        }

        return todoItems;
    }
   private  class MyPictureListener implements PictureListener {

 //       public void onNewPicture(WebView view, Picture arg1) {
          // put code here that needs to run when the page has finished loading and
          // a new "picture" is on the webview.  
 //           view.scrollTo(300, 300);
  //      }

		@Override
		public void onNewPicture(WebView view, android.graphics.Picture picture) {
			// TODO Auto-generated method stub
	
		}    
    }  
    private class AndWeathClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    
}