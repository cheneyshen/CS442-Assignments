package com.paad.broadcastintents;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MyActivity extends Activity {
  /**
   * Listing 5-12: Registering and unregistering a Broadcast Receiver in code 
   */
  private IntentFilter filter = 
      new IntentFilter(LifeformDetectedReceiver.NEW_LIFEFORM);

  private LifeformDetectedReceiver receiver = 
    new LifeformDetectedReceiver();
    LinearLayout mLinearLayout;
  @Override
  public synchronized void onResume() {
    super.onResume();

    // Register the broadcast receiver.
    registerReceiver(receiver, filter); 
  }

  @Override
  public synchronized void onPause()    {
    // Unregister the receiver
    unregisterReceiver(receiver);  

    super.onPause();
  }
  
  //
  public void detectedLifeform(String detectedLifeform, double currentLongitude, double currentLatitude) {
    Intent intent = new Intent(LifeformDetectedReceiver.NEW_LIFEFORM);
    intent.putExtra(LifeformDetectedReceiver.EXTRA_LIFEFORM_NAME,
                    detectedLifeform);
    intent.putExtra(LifeformDetectedReceiver.EXTRA_LONGITUDE,
                    currentLongitude);
    intent.putExtra(LifeformDetectedReceiver.EXTRA_LATITUDE,
                    currentLatitude);

    sendBroadcast(intent);
  }
  
  //
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      super.onCreate(savedInstanceState);

      // Create a LinearLayout in which to add the ImageView
      //mLinearLayout = new LinearLayout(this);

      // Instantiate an ImageView and define its properties
     // ImageView i = new ImageView(this);
      //i.setImageResource(R.drawable._117);
      //i.setAdjustViewBounds(true); // set the ImageView bounds to match the Drawable's dimensions
      //i.setLayoutParams(new Gallery.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,
        //      RadioGroup.LayoutParams.WRAP_CONTENT));

      // Add the ImageView to the layout and set the layout as the content view
  //    mLinearLayout.addView(i);
//      setContentView(mLinearLayout);
  }

    public void button1clk(View view) {
        detectedLifeform("时诗书书书", 99.0, 100.1);
    }

    public void button2clk(View view) {
        TextView textView1 = (TextView)findViewById(R.id.textView1);
        textView1.setText("shenfei2031@gmail.com");
        Linkify.addLinks(textView1,  Linkify.EMAIL_ADDRESSES);
        TextView textView2 = (TextView)findViewById(R.id.textView2);
        textView2.setText("2727 S Indiana Ave Apt 105, Chicago, IL 60616");
        Linkify.addLinks(textView2, Linkify.MAP_ADDRESSES);
    }
}