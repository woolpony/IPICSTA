package com.andmap.ipicsta;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

public class Utiles {
	
	public static String createThumbNail(File origionFile){

		String result = "";

        try
        {
        	final int THUMBNAIL_SIZE = 240;
        	/*
            FileInputStream fis = new FileInputStream(f);
            Bitmap imageBitmap = BitmapFactory.decodeStream(fis);
            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);
			*/
        	Bitmap imageBitmap = decodeFile(origionFile,  THUMBNAIL_SIZE);
        	imageBitmap = Utiles.getSmallBitmap(origionFile.getPath());

	        int degree = readPictureDegree(origionFile.getPath());

	        if(degree!=0){//旋转照片角度
	        	imageBitmap=rotateBitmap(imageBitmap,degree);
	        }
            OutputStream fOut = null;
            File file = new File(getImageFilePath(), origionFile.getName().substring(0, origionFile.getName().length()-4)+"_s.jpg");
            fOut = new FileOutputStream(file);

            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            //delete the origion file if the file is getting from camera   
            if(origionFile.getPath().equals(getImageFilePath())){
            	origionFile.delete();
            }
            //rename the thumbnail to origion
            file.renameTo(new File(getImageFilePath(), origionFile.getName().substring(0, origionFile.getName().length()-4)+".jpg"));
        }
        catch(Exception ex) {
        	result = ex.getMessage();
        }
		return result;
    }
	
	public static String createThumbNailFromAlbum(File origionFile, File newImageFile){

		String result = "";
        try
        {
        	final int THUMBNAIL_SIZE = 240;
        	
        	Bitmap imageBitmap = decodeFile(origionFile,  THUMBNAIL_SIZE);
        	imageBitmap = Utiles.getSmallBitmap(origionFile.getPath());

	        int degree = readPictureDegree(origionFile.getPath());

	        if(degree!=0){//旋转照片角度
	        	imageBitmap=rotateBitmap(imageBitmap,degree);
	        }
            OutputStream fOut = null;
           //File file = new File(getImageFilePath(), origionFile.getName().substring(0, origionFile.getName().length()-4)+"_s.jpg");
            fOut = new FileOutputStream(newImageFile);

            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            //delete the origion file if the file is getting from camera   
            if(origionFile.getPath().equals(getImageFilePath())){
            	origionFile.delete();
            }
            //rename the thumbnail to origion
            //file.renameTo(new File(getImageFilePath(), origionFile.getName().substring(0, origionFile.getName().length()-4)+".jpg"));
        }
        catch(Exception ex) {
        	result = ex.getMessage();
        }
		return result;
    }
	
	public static Bitmap decodeFile(File f, int newSize){
    	Bitmap thumbnail = null;
        try {
            //Decode image size
        	
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            //final int REQUIRED_SIZE = 240;

            //Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while(true){
                if(width_tmp / 2 < newSize || height_tmp / 2 < newSize)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            thumbnail = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);

            return thumbnail;
        } 
        catch (FileNotFoundException e) {
        	
        }
        catch(Exception ee){
        	
        } 
        return thumbnail;
    }
	
	//计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {
	             final int heightRatio = Math.round((float) height/ (float) reqHeight);
	             final int widthRatio = Math.round((float) width / (float) reqWidth);
	             inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }
	        return inSampleSize;
	}
	
	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getSmallBitmap(String filePath) {
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(filePath, options);

	        // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, 480, 800);

	        // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;

	    return BitmapFactory.decodeFile(filePath, options);
	    }
	
	//把bitmap转换成String
	public static String bitmapToString(String filePath) {

	        Bitmap bm = getSmallBitmap(filePath);
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
	        byte[] b = baos.toByteArray();
	        return Base64.encodeToString(b, Base64.DEFAULT);
	    }
	
	/*
	压缩图片，处理某些手机拍照角度旋转的问题
	*/
	public static String compressImage(Context context,String filePath,String fileName,int q) throws FileNotFoundException {

	        Bitmap bm = getSmallBitmap(filePath);

	        int degree = readPictureDegree(filePath);

	        if(degree!=0){//旋转照片角度
	            bm=rotateBitmap(bm,degree);
	        }

	        File imageDir = null;

	        File outputFile=new File(imageDir,fileName);

	        FileOutputStream out = new FileOutputStream(outputFile);

	        bm.compress(Bitmap.CompressFormat.JPEG, q, out);

	        return outputFile.getPath();
	    }
	
	public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
	
	public static Bitmap rotateBitmap(Bitmap bitmap,int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress); 
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }
	
	public static File getImageFile(){
		File imageFile = new File(getImageFilePath(), getImageFileName());
		return imageFile;
	}
	
	public static String getImageFileName(){
		String filePath = getImageFilePath();
		String fileName = "";

		for (int i = 1; i < 100000; i++) {
			fileName = String.valueOf(i) + ".jpg";
			File file = new File(filePath + fileName);
			if(!file.exists()){
				break;
			}
		}
		
		return fileName;
	}
	
	public static String getImageFilePath(){
		String filePath;
		String directory = "ipicsta";
		String rootDirPath = Environment  
                .getExternalStorageDirectory().getPath();
		filePath = rootDirPath + File.separator + directory + File.separator;
		File ipicstadir = new File(filePath);
		if(!ipicstadir.exists()){
			ipicstadir.mkdir();
		}
		Log.i(Utiles.class.toString(), filePath);
		return filePath;
	}
	
	public static String getThumbnailFilePath(File f){
		String thumbmailFilePath="";
		File thumbnailFile = new File(getImageFilePath(), f.getName().substring(0, f.getName().length()-4)+"_s.jpg");
		thumbmailFilePath = thumbnailFile.getPath();
		return thumbmailFilePath;	
	}
	
	//get thumbnail file path from origion path
	public static String getThumbnailFilePath(String filePath){
		String thumbmailFilePath="";
		thumbmailFilePath = getThumbnailFilePath(new File(filePath));
		return thumbmailFilePath;	
	}
	
	public static void deleteImage(String filePath){
		if(!Utiles.isStringNullOrEmpty(filePath)){
			
			File file = new File(filePath);

			if(file.exists()) {
				file.delete();
			}
		}
	}
	
	public static boolean isStringNullOrEmpty(String s){
		return (s == null || s.length() <= 0) ? true : false;
	}
	
	public static String getImagePath(Uri uri, ContentResolver resolver) {
		String imagePath = "";
		Cursor cursor = resolver.query(uri, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			imagePath = cursor.getString(1); // 图片文件路径
		} else if (cursor == null) {
			imagePath = uri.toString().substring(7, uri.toString().length());
		}
		if (cursor != null)
			cursor.close();
		return imagePath;
	}
	
	public static void addADView(Activity activity){
		// advertisement
	    AdView adView = (AdView)activity.findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder()
	    .addTestDevice("F6720EA910D3234C89A59CB1BE68239C")
	    .build();
	    adView.loadAd(adRequest);
	}
}
