package com.andmap.ipicsta;

import java.util.List;

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

import com.google.android.gms.ads.*;

public class StationListActivity extends ListActivity implements Constant{

	DBManager mgr;
	ListView listView;
	int selectItemID;
	List<Station> stationes;
	Clock clock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_station_list);
		this.setTitle(R.string.title_station_list);
		
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
		Intent intent = this.getIntent();
		clock = (Clock)intent.getSerializableExtra("clock");
		stationes = mgr.queryAllStation(clock);
		StationListAdapter adapter = new StationListAdapter(this, R.layout.station_row, stationes);
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
		getMenuInflater().inflate(R.menu.station_list, menu);
		return true;
	}
	
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	            case R.id.menu_add:
//	                Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
	                
	                Intent intent = new Intent(this, AddStationActivity.class);
	                intent.putExtra("clock", clock);
	                startActivityForResult(intent, Constant.station_list_request_code);
	                return true;

	        }
	        return super.onOptionsItemSelected(item);
	    }
	 
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == Constant.station_list_request_code 
				&& resultCode == RESULT_OK){
			refreshListView();
		}
		if(requestCode == Constant.edit_station_request_code 
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
		 	Station station = stationes.get(info.position);
		 	switch (item.getItemId()) {  
	            case EDIT_STATION:  
//	                Toast.makeText(getApplicationContext(), "id: "+ stationes.get(info.position), 1500).show();  
	                Intent intent = new Intent(this, EditStationActivity.class);
	                intent.putExtra("station", station);
	                
	                startActivityForResult(intent,Constant.edit_station_request_code);
	                
	                break; 
	            case DELETE_STATION:  
//	                Toast.makeText(getApplicationContext(), "id: "+ stationes.get(info.position), 1500).show();  
	                mgr.deleteStation(station);
	                Utiles.deleteImage(station.getStationImagePath());
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
		 Station station = stationes.get(position);
		 Intent intent = new Intent(this, PreviewStationActivity.class);
		 intent.putExtra("station", station);
		 startActivityForResult(intent, preview_station_request_code);
	}
	 
	 
}
