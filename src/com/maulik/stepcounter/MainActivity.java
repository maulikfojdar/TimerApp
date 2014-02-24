package com.maulik.stepcounter;



import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity {
	
	Camera camera;
	Button enable_timer,close;
	Boolean isFlashOn = false;
	EditText timer;
	TextView time_left;
	long time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Context context = this;
		PackageManager pm = context.getPackageManager();
 
		if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			Log.e("err", "Device has no camera!");
			return;
		}
 
		camera = Camera.open();
		final Parameters p = camera.getParameters();
 
		enable_timer = (Button) findViewById(R.id.enable_timer);
		close = (Button) findViewById(R.id.close);
		timer = (EditText) findViewById(R.id.timer_text);
		time_left = (TextView) findViewById(R.id.timer_value);
		
		
		close.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				finish();
				
			}
		});
		
		enable_timer.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				
				if (isFlashOn) {
					 
					Log.i("info", "torch is turn off!");
 
					p.setFlashMode(Parameters.FLASH_MODE_OFF);
					camera.setParameters(p);
					camera.stopPreview();
					isFlashOn = false;
					enable_timer.setText("Enable Timer");
					timer.setText("");
					time_left.setText("0");
					
				}
				else {
					
					String text = timer.getText().toString();
					try{
					time = Integer.parseInt(text);
					time_left.setText(time+"");
					}
					catch(NumberFormatException e)
					{
					Log.e("Exception:","Invalid timer set");
					}
					/*for (long i = time; i>=0 ; i--){
					try {
					    Thread.sleep(1000);
					} catch(InterruptedException ex) {
					    Thread.currentThread().interrupt();
					}
						time_left.setText(i+"");
					}
				*/
					
					new CountDownTimer(time*1000, 1000) {

					     public void onTick(long millisUntilFinished) {
					         time_left.setText("" + millisUntilFinished / 1000);
					     }

					     public void onFinish() {
					         time_left.setText("Time up!");
					         Log.i("info", "torch is turn on!");
					         
								p.setFlashMode(Parameters.FLASH_MODE_TORCH);
			 
								camera.setParameters(p);
								camera.startPreview();
								isFlashOn = true;
								enable_timer.setText("Turn off the lights");
					     }
					  }.start();
					  
					
 
				}
 
			}
		});
	
	}
	


}
