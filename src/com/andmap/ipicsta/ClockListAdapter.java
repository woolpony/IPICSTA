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

public class ClockListAdapter extends BaseAdapter {
	private Context mContext;
	private List<Clock> clocks;

	public ClockListAdapter(Context context, int resourceId, List<Clock> clocks) {
		this.mContext = context;
		this.clocks = clocks;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;
		if (v == null) {
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.clock_row, null);
            holder = new ViewHolder();
            
    		holder.clockNameTextView = (TextView)v.findViewById(R.id.clockName);
    		holder.clockImageView = (ImageView)v.findViewById(R.id.clockImage);
    		v.setTag(holder);
        }
		else{
			holder = (ViewHolder) v.getTag();
		}
		
		Clock clock = clocks.get(position);
		Log.i("100001", clock.toString());
		
		
		holder.clockNameTextView.setText(clock.getClockName());
		
		if(clock.getClockImagePath()!=null){
			Bitmap bitmap = BitmapFactory.decodeFile(
					clock.getClockImagePath());
			if(bitmap != null){
				holder.clockImageView.setImageBitmap(bitmap);
			}
		}
		
		return v;
	}
	 @Override
	 public int getCount() {
	  return clocks.size();
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
		  ImageView clockImageView;
		  TextView clockNameTextView;
		 }
}
