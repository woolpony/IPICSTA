package com.andmap.ipicsta;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StationListAdapter extends BaseAdapter {
	private Context mContext;
	private List<Station> stationes;

	public StationListAdapter(Context context, int resourceId, List<Station> stationes) {
		this.mContext = context;
		this.stationes = stationes;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;
		if (v == null) {
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.station_row, null);
            holder = new ViewHolder();
            
    		holder.stationNameTextView = (TextView)v.findViewById(R.id.stationName);
    		holder.stationImageView = (ImageView)v.findViewById(R.id.stationImage);
    		v.setTag(holder);
        }
		else{
			holder = (ViewHolder) v.getTag();
		}
		
		Station station = stationes.get(position);
		Log.i("100001", station.toString());
		
		
		holder.stationNameTextView.setText(station.getStationName());
		
		if(station.getStationImagePath()!=null){
			Bitmap bitmap = BitmapFactory.decodeFile(
					station.getStationImagePath());
			if(bitmap != null){
				holder.stationImageView.setImageBitmap(bitmap);
			}
		}
		
		return v;
	}
	 @Override
	 public int getCount() {
	  return stationes.size();
	 }

	 @Override
	 public Object getItem(int position) {
	  return position;
	 }

	 @Override
	 public long getItemId(int position) {
	  return position;
	 }
	
	private static class ViewHolder {
		  ImageView stationImageView;
		  TextView stationNameTextView;
		 }
}
