package com.andmap.ipicsta;
import java.io.File;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;


public class AddStationActivity extends Activity implements Constant{
	ImageView imageView;
	EditText stationName;
	File imageFile;
	Station station;
	DBManager mgr;

	public AddStationActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_station); 
		this.setTitle(R.string.title_station_list);
		
		Utiles.addADView(this);
		
		initView();
		
		if(mgr == null){
			mgr = new DBManager(this);
		}
	}

	private void initView() {
		station = new Station();
		
		Intent intent = getIntent();
		Clock clock = (Clock)intent.getSerializableExtra("clock");
		station.setClockid(clock.get_id());
		
		imageView = (ImageView)this.findViewById(R.id.imageView_thumnail_add_station);
		stationName = (EditText)this.findViewById(R.id.station_name_add);
		
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	public void onCameraButtonClicked(View v){
//		Toast.makeText(this, "Tapped camera", Toast.LENGTH_SHORT).show();
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
        //下面这句指定调用相机拍照后的照片存储的路径  
		if(imageFile == null){
			imageFile = Utiles.getImageFile();
		}
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));  
        //intent.putExtra("ImageFilePath", value)
        startActivityForResult(intent, add_station_from_camera_request_code); 
	}
	
	public void onAlbumButtonClicked(View v){
//		Toast.makeText(this, "Tapped album", Toast.LENGTH_SHORT).show();
		 Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
         intent.setType("image/*");  
         startActivityForResult(intent, add_station_from_album_request_code);  
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try{ 
//			Toast.makeText(this, requestCode + "  " +resultCode, Toast.LENGTH_LONG).show();
			if (requestCode == Constant.add_station_from_camera_request_code && resultCode == RESULT_OK) {
				
				Utiles.createThumbNail(imageFile);
	            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
		        imageView.setImageBitmap(bitmap); 

				station.setStationImagePath(imageFile.getPath());
				
				
		        
//		        Toast.makeText(this, "after camera !" +imageFile.getPath(), Toast.LENGTH_LONG).show();
			}
			
			if (requestCode == Constant.add_station_from_album_request_code && resultCode == RESULT_OK) {
				ContentResolver resolver = getContentResolver();  
				Uri imgUri = data.getData(); 
				if(imageFile == null){
					imageFile = Utiles.getImageFile();
				}
				File file = new File(Utiles.getImagePath(imgUri, resolver));
				
				
				Utiles.createThumbNailFromAlbum(file,imageFile);
	            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
		        imageView.setImageBitmap(bitmap); 

				station.setStationImagePath(imageFile.getPath());
				
				
		        
//		        Toast.makeText(this, "after album !" +imgUri, Toast.LENGTH_LONG).show();
			}
			
			super.onActivityResult(requestCode, resultCode, data);
			}
		catch(Exception ex){
			Log.e("100001", ex.toString());
		}
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()){
			case R.id.add_station_menu_cancel:
				setResult(RESULT_CANCELED);
				finish();
				return true;
				
			case R.id.add_station_menu_done:
				station.setStationName(stationName.getText().toString());
				saveStation(station);
				setResult(RESULT_OK);
				finish();
				return true;
				
			case android.R.id.home:
				station.setStationName(stationName.getText().toString());
				saveStation(station);
				setResult(RESULT_OK);
				this.finish();
				return true;
				
			default:
				return false;
		}
	}
	
	private void saveStation(Station station){
		if(!Utiles.isStringNullOrEmpty(station.getStationName())
				|| !Utiles.isStringNullOrEmpty(station.getStationImagePath())){
			mgr.addStation(station);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_station, menu);
		return true;
	}

	
	@Override  
    protected void onDestroy() {  
        super.onDestroy();  
        //应用的最后一个Activity关闭时应释放DB  
        mgr.closeDB();  
    }  
	@Override
	public boolean onTouchEvent(android.view.MotionEvent event){
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		return imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
	}
}
