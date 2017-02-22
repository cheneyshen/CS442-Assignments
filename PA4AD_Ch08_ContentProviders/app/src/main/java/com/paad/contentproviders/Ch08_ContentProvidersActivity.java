package com.paad.contentproviders;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Ch08_ContentProvidersActivity extends Activity {

  ArrayList<String> contacts = new ArrayList<String>();
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);


    final ListView contactsView = (ListView) findViewById(R.id.contactslistView);
    ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts);
    contactsView.setAdapter(aa);
    new ShowContacts().execute(aa);

    Button insertContactButton = (Button)findViewById(R.id.button6);
    insertContactButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        insertContactWithIntent();
      }
    });

  }


  private class ShowContacts extends AsyncTask<ArrayAdapter<String>, ArrayAdapter<String>, ArrayAdapter<String>> {

    @Override
    protected ArrayAdapter<String> doInBackground(ArrayAdapter<String>... params) {
      String[] result = getNameAndNumber();
      if (result != null)
        for (int i = 0; i < result.length; i++)
          contacts.add(result[i]);
      return params[0];
    }

    @Override
    protected void onPostExecute(ArrayAdapter<String> result) {
      result.notifyDataSetChanged();
    }
  }


  private String[] getSongListing() {
    /**
     * Listing 8-35: Accessing the Media Store Content Provider
     */
    // Get a Cursor over every piece of audio on the external volume, 
    // extracting the song title and album name.
    String[] projection = new String[] {
      MediaStore.Audio.AudioColumns.ALBUM,
      MediaStore.Audio.AudioColumns.TITLE
    };

    Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

    Cursor cursor =
      getContentResolver().query(contentUri, projection,
                                 null, null, null);

    // Get the index of the columns we need.
    int albumIdx =
      cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM);
    int titleIdx =
      cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE);

    // Create an array to store the result set.
    String[] result = new String[cursor.getCount()];

    // Iterate over the Cursor, extracting each album name and song title.
    while (cursor.moveToNext()) {
      // Extract the song title.
      String title = cursor.getString(titleIdx);
      // Extract the album name.
      String album = cursor.getString(albumIdx);

      result[cursor.getPosition()] = title + " (" + album + ")";
    }

    // Close the Cursor.
    cursor.close();

    //
    return result;
  }

  private String[] getNames() {
    /**
     * Listing 8-36: Accessing the Contacts Contract Contact Content Provider
     */
    // Create a projection that limits the result Cursor
    // to the required columns.
    String[] projection = {
      ContactsContract.Contacts._ID,
      ContactsContract.Contacts.DISPLAY_NAME
    };

   // Get a Cursor over the Contacts Provider.
    Cursor cursor =
      getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                                 projection, null, null, null);

    // Get the index of the columns.
    int nameIdx =
      cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
    int idIdx =
      cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);

    // Initialize the result set.
    String[] result = new String[cursor.getCount()];

    // Iterate over the result Cursor.
    while(cursor.moveToNext()) {
      // Extract the name.
      String name = cursor.getString(nameIdx);
      // Extract the unique ID.
      String id = cursor.getString(idIdx);

      result[cursor.getPosition()] = name + " (" + id + ")";
    }

    // Close the Cursor.
    cursor.close();

    //
    return result;
  }

  private String[] getNameAndNumber() {
    /**
     * Listing 8-37: Finding contact details for a contact name
     */
    ContentResolver cr = getContentResolver();
    String[] result = null;

    // Find a contact using a partial name match
    //String searchName = "andy";
    String searchName = "Fei";
    Uri lookupUri =
      Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI,
              searchName);

    // Create a projection of the required column names.
    String[] projection = new String[] {
      ContactsContract.Contacts._ID
    };

    // Get a Cursor that will return the ID(s) of the matched name.
    Cursor idCursor = cr.query(lookupUri,
      projection, null, null, null);

    // Extract the first matching ID if it exists.
    String id = null;
    if (idCursor.moveToFirst()) {
      int idIdx =
        idCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
      id = idCursor.getString(idIdx);
    }

    // Close that Cursor.
    idCursor.close();

    // Create a new Cursor searching for the data associated with the returned Contact ID.
    if (id != null) {
      // Return all the PHONE data for the contact.
      String where = ContactsContract.Data.CONTACT_ID +
        " = " + id + " AND " +
        ContactsContract.Data.MIMETYPE + " = '" +
        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE +
        "'";

      projection = new String[] {
        ContactsContract.Data.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER
      };

      Cursor dataCursor =
        getContentResolver().query(ContactsContract.Data.CONTENT_URI,
          projection, where, null, null);

      // Get the indexes of the required columns.
      int nameIdx =
        dataCursor.getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME);
      int phoneIdx =
        dataCursor.getColumnIndexOrThrow(
          ContactsContract.CommonDataKinds.Phone.NUMBER);

      result = new String[dataCursor.getCount()];

      while(dataCursor.moveToNext()) {
        // Extract the name.
        String name = dataCursor.getString(nameIdx);
        // Extract the phone number.
        String number = dataCursor.getString(phoneIdx);

        result[dataCursor.getPosition()] = name + " (" + number + ")";
      }

      dataCursor.close();
    }

    return result;
  }

  private String performCallerId() {
    /**
     * Listing 8-38: Performing a caller-ID lookup
     */
    String incomingNumber = "(650)253-0000";
    String result = "Not Found";

    Uri lookupUri =
      Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                           incomingNumber);

    String[] projection = new String[] {
      ContactsContract.Contacts.DISPLAY_NAME
    };

    Cursor cursor = getContentResolver().query(lookupUri,
      projection, null, null, null);

    if (cursor.moveToFirst()) {
      int nameIdx =
        cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);

      result = cursor.getString(nameIdx);
    }

    cursor.close();


    return result;
  }

  private void insertContactWithIntent() {
    /**
     * Listing 8-40: Inserting a new contact using an Intent
     */
    Intent intent =
            new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT,
                    ContactsContract.Contacts.CONTENT_URI);
    intent.setData(Uri.parse("tel:(650)253-0000"));

    intent.putExtra(ContactsContract.Intents.Insert.COMPANY, "Google");
    intent.putExtra(ContactsContract.Intents.Insert.POSTAL,
            "1600 Amphitheatre Parkway, Mountain View, California");

    startActivity(intent);

  }
}