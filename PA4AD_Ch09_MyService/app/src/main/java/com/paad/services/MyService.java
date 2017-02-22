package com.paad.services;

/**
 * Listing 9-1: A skeleton Service class
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.os.Message;
import android.os.Handler;
import android.widget.Toast;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Intent;

public class MyService extends Service {
    private Timer mTimer;
    public final static  String TAG = "MyService";
    public static int value;
    public final static String vString = "0";
    private static final int NOTIFICATION_REF = 1;
  @Override
  public void onCreate() {
    // TODO: Actions to perform when service is created.
    // init timer
    mTimer = new Timer();
    Log.i(TAG, "onCreate");
}

  @Override
  public IBinder onBind(Intent intent) {
    // TODO: Replace with service binding implementation.
    return null;
  }

  @Override
  public void onDestroy() {
    Toast.makeText(this, "My Service Stopped at " + String.valueOf(value), Toast.LENGTH_SHORT).show();
    Log.i(TAG, "onDestroy");
    if (mTimer != null) {
      mTimer.cancel();
      mTimer.purge();
      mTimer = null;
    }
  }
  /**
   * Listing 9-3: Overriding Service restart behavior
   */
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if (intent.getExtras().get(vString) != null) {
        value = (int) intent.getExtras().get(vString);
    }
      else {
        value ++;
    }
    Log.i(TAG, String.valueOf(value));
    Toast.makeText(this, "My Service Starts from " + String.valueOf(value), Toast.LENGTH_SHORT).show();
    startBackgroundTask(intent, startId);
      return Service.START_STICKY;
  }
  
  private void  startBackgroundTask(Intent intent, int startId) {
    // Start a background thread and begin the processing.
    backgroundExecution();
  }
  
  /**
   * Listing 9-14: Moving processing to a background Thread
   */
  //This method is called on the main GUI thread.
  private void backgroundExecution() {
   // This moves the time consuming operation to a child thread.
   Thread thread = new Thread(null, doBackgroundThreadProcessing,
                              "Background");
   thread.start();
  }
  
  //Runnable that executes the background processing method.
  private Runnable doBackgroundThreadProcessing = new Runnable() {
   public void run() {
     backgroundThreadProcessing();
   }
  };
  
  //Method which does some processing in the background.
  private void backgroundThreadProcessing() {
   // [ ... Time consuming operations ... ]

    // start timer task
    setTimerTask();

  }
  private void setTimerTask() {
    mTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        Message message = new Message();
        message.what = value++;
        doActionHandler.sendMessage(message);
      }
    }, 1000, 10000/* Means after 1 second， loop notify every 10 seconds */);
  }
  /**
   * do some action
   */
  private Handler doActionHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      int msgId = msg.what;
      Toast.makeText(getApplicationContext(), String.valueOf(msgId), Toast.LENGTH_SHORT).show();

        NotificationManager notificationManager
                = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_REF,
                customLayoutNotification(msgId).getNotification());
    }
  };


    private Notification.Builder customLayoutNotification(int value) {
        Notification.Builder builder =
                new Notification.Builder(MyService.this);

        String BUTTON_CLICK_ACTION = "com.paad.notifications.BUTTON_CLICK";
        Intent newIntent = new Intent(BUTTON_CLICK_ACTION);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(MyService.this, 2, newIntent, 0);
        Bitmap myIconBitmap = null; // TODO Obtain Bitmap

        /**
         * Listing 10-36: Applying a custom layout to the Notification status window
         */
        builder.setSmallIcon(R.drawable.ic_launcher)
                .setTicker("Notification")
                .setWhen(System.currentTimeMillis())
                .setContentTitle(String.valueOf(value))
                .setContentText(String.valueOf(value))
                .setContentInfo("Info")
                .setLargeIcon(myIconBitmap)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND |
                        Notification.DEFAULT_VIBRATE)
                .setSound(
                        RingtoneManager.getDefaultUri(
                                RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.RED, 0, 1);;

        //
        return builder;
    }
}