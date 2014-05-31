package com.andmap.ipicsta;
import java.io.File;

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


public class EditClockActivity extends Activity implements Constant{
	ImageView imageView;
	EditText clockName;
	File imageFile;
	Clock clock;
	DBManager mgr;

	public EditClockActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_clock); 
		this.setTitle(R.string.title_clock_list);
		clock = new Clock();
		
		Utiles.addADView(this);
		
		initView();
		
		
		
		
		if(mgr == null){
			mgr = new DBManager(this);
		}
	}
	
	public void initView(){
		Intent intent = getIntent();
		int _id = intent.getIntExtra("id", 0);
		String clockNameString = intent.getStringExtra("clockName");
		String imageFilePath = intent.getStringExtra("imageFilePath");
		
		
		imageView = (ImageView)this.findViewById(R.id.imageView_thumnail_add_clock);
		clockName = (EditText)this.findViewById(R.id.clock_name_add);
		

		clockName.setText(clockNameString);
		if(!Utiles.isStringNullOrEmpty(imageFilePath)){
			Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
	        imageView.setImageBitmap(bitmap); 
			
	        imageFile = new File(imageFilePath);
		}
		
        clock.set_id(_id);
        clock.setClockImagePath(imageFilePath);
        
        
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
        startActivityForResult(intent, Constant.add_clock_from_camera_request_code); 
	}
	
	public void onAlbumButtonClicked(View v){
//		Toast.makeText(this, "Tapped album", Toast.LENGTH_SHORT).show();
		 Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
         intent.setType("image/*");  
         startActivityForResult(intent, add_clock_from_album_request_code);  
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try{ 
//			Toast.makeText(this, requestCode + "  " +resultCode, Toast.LENGTH_LONG).show();
			if (requestCode == Constant.add_clock_from_camera_request_code && resultCode == RESULT_OK) {
				
				Utiles.createThumbNail(imageFile);
	            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
		        imageView.setImageBitmap(bitmap); 

				clock.setClockImagePath(imageFile.getPath());
				
				
		        
//		        Toast.makeText(this, "after camera !" +imageFile.getPath(), Toast.LENGTH_LONG).show();
			}
			if (requestCode == Constant.add_clock_from_album_request_code && resultCode == RESULT_OK) {
				ContentResolver resolver = getContentResolver();  
				Uri imgUri = data.getData(); 
				if(imageFile == null){
					imageFile = Utiles.getImageFile();
				}
				File file = new File(Utiles.getImagePath(imgUri, resolver));
				
				
				Utiles.createThumbNailFromAlbum(file,imageFile);
	            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
		        imageView.setImageBitmap(bitmap); 

				clock.setClockImagePath(imageFile.getPath());
					        
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
		case R.id.add_clock_menu_cancel:
			setResult(RESULT_CANCELED);
			finish();
			return true;
			
		case R.id.add_clock_menu_done:
			clock.setClockName(clockName.getText().toString());
			saveClock(clock);
			setResult(RESULT_OK);
			finish();
			return true;
			
		case android.R.id.home:
			clock.setClockName(clockName.getText().toString());
			saveClock(clock);
			setResult(RESULT_OK);
			this.finish();
			return true;
			
		default:
			return false;
		}
	}
	
	private void saveClock(Clock clock){
		if(!Utiles.isStringNullOrEmpty(clock.getClockName())
				|| !Utiles.isStringNullOrEmpty(clock.getClockImagePath())){
			mgr.updateClock(clock);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_clock, menu);
		return true;
	}
	@Override
	   public boolean onTouchEvent(android.view.MotionEvent event){
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		return imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
	}
	
	@Override  
    protected void onDestroy() {  
        super.onDestroy();  
        //应用的最后一个Activity关闭时应释放DB  
        mgr.closeDB();  
    }  
}
