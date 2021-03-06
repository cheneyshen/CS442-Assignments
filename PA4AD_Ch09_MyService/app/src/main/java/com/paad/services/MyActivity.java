package com.paad.services;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.View;

public class MyActivity extends Activity {

    TextView asyncTextView;
    ProgressBar asyncProgress;
    private static final String TAG = "MyService";
    private static final String BUTTON_CLICK_ACTION = "com.paad.services.BUTTON_CLICK";
    static int fromInt = 1;
    static String fromString;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    asyncTextView = (TextView)findViewById(R.id.asyncTextView);
    asyncProgress = (ProgressBar)findViewById(R.id.asyncProgress);
    Button start = (Button)findViewById(R.id.button1);
    start.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        Log.i(TAG, "onClick: starting service");
        EditText input = (EditText)findViewById(R.id.editText);
        fromString =  input.getText().toString();
        if (fromString != null && !"".equals(fromString.trim())) {
          if (fromString.matches("^[0-9]*$")) {
              fromInt = Integer.parseInt(fromString);
              Log.i(TAG, fromString + String.valueOf(fromInt));
          }
        }
        explicitStart(fromInt);
      }
    });

    Button stop = (Button)findViewById(R.id.button2);
    stop.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        Log.i(TAG, "onClick: stopping service");
        stopServices();
      }
    });
  }
  
  private void explicitStart(int i) {
    // Explicitly start My Service
    Intent intent = new Intent(this, MyService.class);
    intent.putExtra(MyService.vString, i);
      Log.i(TAG, String.valueOf(i));
    // TODO Add extras if required.
    startService(intent);
    }

  private void implicitStart() {
    // Implicitly start a music Service
    //Intent intent = new Intent(MyMusicService.PLAY_ALBUM);
    //intent.putExtra(MyMusicService.ALBUM_NAME_EXTRA, "United");
   // intent.putExtra(MyMusicService.ARTIST_NAME_EXTRA, "Pheonix");
   // startService(intent);
  } 

  private void stopServices() {
    /**
     * Listing 9-5: Stopping a Service
     */
    // Stop a service explicitly.
    stopService(new Intent(this, MyService.class));
        
    // Stop a service implicitly.
    //Intent intent = new Intent(MyMusicService.PLAY_ALBUM);
    //stopService(intent);
  }
  

  /**
   * Listing 9-11: AsyncTask implementation using a string parameter and result, with an integer progress value
   */
  public class MyAsyncTask extends AsyncTask<String, Integer, String> {
    @Override
    protected String doInBackground(String... parameter) {
      // Moved to a background thread.
      String result = "";
      int myProgress = 0;
      
      int inputLength = parameter[0].length();

      // Perform background processing task, update myProgress]
      for (int i = 1; i <= inputLength; i++) {
        myProgress = i;
        result = result + parameter[0].charAt(inputLength-i);
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) { }
        publishProgress(myProgress);
      }

      // Return the value to be passed to onPostExecute
      return result;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
      // Synchronized to UI thread.
      // Update progress bar, Notification, or other UI elements
      asyncProgress.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
      // Synchronized to UI thread.
      // Report results via UI update, Dialog, or notifications
      asyncTextView.setText(result);
    }
  }
  
  private void executeAsync() {
    /**
     * Listing 9-12: Executing an asynchronous task
     */
    String input = "redrum ... redrum";
    new MyAsyncTask().execute(input); 
  }
  
  /**
   * Listing 9-15: Using a Handler to synchronize with the GUI Thread
   */
  //This method is called on the main GUI thread.
  private void backgroundExecution() {
    // This moves the time consuming operation to a child thread.
    Thread thread = new Thread(null, doBackgroundThreadProcessing,
                               "Background");
    thread.start();
  }
  
  // Runnable that executes the background processing method.
  private Runnable doBackgroundThreadProcessing = new Runnable() {
    public void run() {
      backgroundThreadProcessing();
    }
  };
  
  // Method which does some processing in the background.
  private void backgroundThreadProcessing() {
    // [ ... Time consuming operations ... ]
    
    // Use the Handler to post the doUpdateGUI 
    // runnable on the main UI thread.
    handler.post(doUpdateGUI);
  }
  
  //Initialize a handler on the main thread.
  private Handler handler = new Handler();
  
  // Runnable that executes the updateGUI method.
  private Runnable doUpdateGUI = new Runnable() {
    public void run() {
      updateGUI();
    }
  };
  
  // This method must be called on the UI thread.
  private void updateGUI() {
    // [ ... Open a dialog or modify a GUI element ... ]
  }
  
  private void setAlarm() {
    /**
     * Listing 9-16: Creating a waking Alarm that triggers in 10 seconds
     */
    // Get a reference to the Alarm Manager
    AlarmManager alarmManager = 
     (AlarmManager)getSystemService(Context.ALARM_SERVICE);
  
    // Set the alarm to wake the device if sleeping.
    int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
  
    // Trigger the device in 10 seconds.
    long timeOrLengthofWait = 10000;
  
    // Create a Pending Intent that will broadcast and action
    String ALARM_ACTION = "ALARM_ACTION";
    Intent intentToFire = new Intent(ALARM_ACTION);
    PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0,
      intentToFire, 0);
  
    // Set the alarm
    alarmManager.set(alarmType, timeOrLengthofWait, alarmIntent);
    
    /**
     * Listing 9-17: Canceling an Alarm
     */
    alarmManager.cancel(alarmIntent);
  }
  
  private void setInexactRepeatingAlarm() {
    /**
     * Listing 9-18: Setting an inexact repeating alarm
     */
    //Get a reference to the Alarm Manager
    AlarmManager alarmManager = 
    (AlarmManager)getSystemService(Context.ALARM_SERVICE);
    
    //Set the alarm to wake the device if sleeping.
    int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
    
    //Schedule the alarm to repeat every half hour.
    long timeOrLengthofWait = AlarmManager.INTERVAL_HALF_HOUR;
    
    //Create a Pending Intent that will broadcast and action
    String ALARM_ACTION = "ALARM_ACTION";
    Intent intentToFire = new Intent(ALARM_ACTION);
    PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0,
     intentToFire, 0);
    
    //Wake up the device to fire an alarm in half an hour, and every 
    //half-hour after that.
    alarmManager.setInexactRepeating(alarmType,
            timeOrLengthofWait,
            timeOrLengthofWait,
            alarmIntent);
  }

}