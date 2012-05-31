package org.dennis.AndWeath;



import android.app.Activity;
import android.content.Context;
	import android.content.Intent;
import android.media.AudioManager;
	import android.os.Bundle;
	import android.speech.tts.TextToSpeech;
	import android.speech.tts.TextToSpeech.OnInitListener;
	import android.view.View;
	import android.view.View.OnClickListener;
	import android.widget.Button;
	import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
	 
	public class TtsActivity extends Activity implements OnInitListener {
	     
	    private int MY_DATA_CHECK_CODE = 0;
	     
	    private TextToSpeech tts;
	    private Button speakButton;	     
	    TextView listenText;
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	     
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.mainlisten);
	  speakButton = (Button) findViewById(R.id.speak_button);
	  listenText = (TextView) findViewById(R.id.listentext); 
	  speakButton.setOnClickListener(new OnClickListener() {           
	   @Override
	   public void onClick(View v) {

//		   AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
//		   int amStreamMusicMaxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//		   am.setStreamVolume(AudioManager.STREAM_MUSIC, amStreamMusicMaxVol, 0);
//		   am.setStreamVolume(AudioManager.STREAM_MUSIC, am.ADJUST_LOWER, 0);


		   
           listenText.setText(AndWeathActivity.gldenstr);
//	   	    Toast.makeText(TtsActivity.this, "Saying: " + AndWeathActivity.gldenstr, Toast.LENGTH_LONG).show();
	   	    tts.speak(AndWeathActivity.gldenstr, TextToSpeech.QUEUE_ADD, null);
       }
	   
	      });

	   
	  Intent checkIntent = new Intent();
	      checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
	      startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
 
   
	    }
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (requestCode == MY_DATA_CHECK_CODE) {
	            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
	                // success, create the TTS instance
	                tts = new TextToSpeech(this, this);


	            }
	            else {
	                // missing data, install it
	                Intent installIntent = new Intent();
	                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
	                startActivity(installIntent);
	            }
	        }
	 
	    }
	 
	    @Override
	    public void onInit(int status) {       
	        if (status == TextToSpeech.SUCCESS) {
//            Toast.makeText(TtsActivity.this,
//	                    "Text-To-Speech engine is initialized", Toast.LENGTH_LONG).show();

	        }
	        else if (status == TextToSpeech.ERROR) {
	            Toast.makeText(TtsActivity.this,
	                    "Error occurred while initializing Text-To-Speech engine", Toast.LENGTH_LONG).show();
	        }
		     speakButton.performClick(); 	
	    }
     
	}
