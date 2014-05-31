package com.andmap.ipicsta;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class PreviewStationActivity extends Activity {
	
	Station station;

	public PreviewStationActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview_station); 
		this.setTitle(R.string.title_station_list);
		
		// advertisement
	    AdView adView = (AdView)this.findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder()
	    .addTestDevice("F6720EA910D3234C89A59CB1BE68239C")
	    .build();
	    adView.loadAd(adRequest);
		
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		station  = (Station)intent.getSerializableExtra("station");
		
		
		ImageView imageView = (ImageView)this.findViewById(R.id.station_preview_image);
		TextView stationName = (TextView)this.findViewById(R.id.station_preview_name);
		

		stationName.setText(station.getStationName());
		if(!Utiles.isStringNullOrEmpty(station.getStationImagePath())){
			Bitmap bitmap = BitmapFactory.decodeFile(station.getStationImagePath());
	        imageView.setImageBitmap(bitmap); 
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()){
		case android.R.id.home:
			setResult(RESULT_CANCELED);
			finish();
			return true;
			
		default:
			return false;
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
}
