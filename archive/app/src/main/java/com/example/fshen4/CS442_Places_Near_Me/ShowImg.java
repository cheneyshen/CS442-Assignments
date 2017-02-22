package com.example.fshen4.CS442_Places_Near_Me;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

public class ShowImg extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.example.fshen4.CS442_Places_Near_Me.R.layout.show_img);
		
		ImageView img = (ImageView) findViewById(com.example.fshen4.CS442_Places_Near_Me.R.id.imageView1);
		img.setBackgroundResource(com.example.fshen4.CS442_Places_Near_Me.R.drawable.bg114);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(com.example.fshen4.CS442_Places_Near_Me.R.menu.my_main, menu);
		return true;
	}

}
