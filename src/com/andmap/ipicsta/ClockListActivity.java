package com.andmap.ipicsta;

import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

public class ClockListActivity extends ListActivity implements Constant {

	DBManager mgr;
	ListView listView;
	int selectItemID;
	List<Clock> clocks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clock_list);
		this.setTitle(R.string.title_clock_list);
		
		Utiles.addADView(this);
		
		listView = getListView();
		this.registerForContextMenu(listView); 
		
		if(mgr == null){
			mgr = new DBManager(this);
		}	
		//mgr.deleteClock();
		refreshListView();
	}

	private void refreshListView() {
		clocks = mgr.queryAllClock();
		ClockListAdapter adapter = new ClockListAdapter(this, R.layout.clock_row, clocks);
		this.setListAdapter(adapter);
	}
	
	@Override  
    protected void onDestroy() {  
        super.onDestroy();  
        //应用的最后一个Activity关闭时应释放DB  
        mgr.closeDB();  
    }  
      
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.clock_list, menu);
		return true;
	}
	
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	            case R.id.menu_add:
//	                Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
	                
	                Intent intent = new Intent(this, AddClockActivity.class);
	                startActivityForResult(intent, Constant.clock_list_request_code);
	                return true;

	        }
	        return super.onOptionsItemSelected(item);
	    }
	 
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == Constant.clock_list_request_code 
				&& resultCode == RESULT_OK){
			refreshListView();
		}
		if(requestCode == Constant.edit_clock_request_code 
				&& resultCode == RESULT_OK){
			refreshListView();
		}
	 }
	 
	 @Override  
	 public void onCreateContextMenu(ContextMenu menu, View v,  
	         ContextMenuInfo menuInfo) {  
	     Log.v("100001", "populate context menu");  
	     // set context menu title  
	     //menu.setHeaderTitle("What will you do?");  
	     // add context menu item  
	     menu.add(0, 1, Menu.NONE, "Edit");  
	     menu.add(0, 2, Menu.NONE, "Delete");  
	 } 
	 
	 @Override  
	    public boolean onContextItemSelected(MenuItem item) {  
		 	final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		 	Clock clock = clocks.get(info.position);
		 	switch (item.getItemId()) {  
	            case EDIT_CLOCK:  
//	                Toast.makeText(getApplicationContext(), "id: "+ clocks.get(info.position), 1500).show();  
	                Intent intent = new Intent(this, EditClockActivity.class);
	                intent.putExtra("id", clock.get_id());
	                intent.putExtra("clockName", clock.getClockName());
	                intent.putExtra("imageFilePath", clock.getClockImagePath());
	                
	                startActivityForResult(intent,Constant.edit_clock_request_code);
	                
	                break; 
	            case DELETE_CLOCK:  
//	                Toast.makeText(getApplicationContext(), "id: "+ clocks.get(info.position), 1500).show();  
	                List<Station> stationes = mgr.queryAllStation(clock);
	                for (Station station : stationes) {
						Utiles.deleteImage(station.getStationImagePath());
					}
	                Utiles.deleteImage(clock.getClockImagePath());
	                

	                mgr.deleteStationes(clock);
	                mgr.deleteClock(clock);
	                
	                this.refreshListView();
	                break; 
	             default:
	     	        return super.onContextItemSelected(item); 
	        }   
	        return true;
	    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
//		 Toast.makeText(getApplicationContext(), "id: "+ id, 1500).show();
		 Clock clock = clocks.get(position);
		 Intent intent = new Intent(this, StationListActivity.class);
		 intent.putExtra("clock", clock);
		 startActivity(intent);
	}
	 
	 
}
