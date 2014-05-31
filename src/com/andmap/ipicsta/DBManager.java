package com.andmap.ipicsta;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
	private DBHelper helper;
	private SQLiteDatabase db;
	
	public DBManager(Context context) {
		helper = new DBHelper(context);
		//因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
		//所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		db = helper.getWritableDatabase();
	}
	
	/**
	 * add persons
	 * @param persons
	 */
	public void add(List<Person> persons) {
        db.beginTransaction();	//开始事务
        try {
        	for (Person person : persons) {
        		db.execSQL("INSERT INTO person VALUES(null, ?, ?, ?)", new Object[]{person.name, person.age, person.info});
        	}
        	db.setTransactionSuccessful();	//设置事务成功完成
        } finally {
        	db.endTransaction();	//结束事务
        }
	}
	
	
	
	/**
	 * update person's age
	 * @param person
	 */
	public void updateAge(Person person) {
		ContentValues cv = new ContentValues();
		cv.put("age", person.age);
		db.update("person", cv, "name = ?", new String[]{person.name});
	}
	
	/**
	 * delete old person
	 * @param person
	 */
	public void deleteOldPerson(Person person) {
		db.delete("person", "age >= ?", new String[]{String.valueOf(person.age)});
	}
	
	/**
	 * query all persons, return list
	 * @return List<Person>
	 */
	public List<Person> query() {
		ArrayList<Person> persons = new ArrayList<Person>();
		Cursor c = queryTheCursor();
        while (c.moveToNext()) {
        	Person person = new Person();
        	person._id = c.getInt(c.getColumnIndex("_id"));
        	person.name = c.getString(c.getColumnIndex("name"));
        	person.age = c.getInt(c.getColumnIndex("age"));
        	person.info = c.getString(c.getColumnIndex("info"));
        	persons.add(person);
        }
        c.close();
        return persons;
	}
	
	/**
	 * query all persons, return cursor
	 * @return	Cursor
	 */
	public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM person", null);
        return c;
	}
	
	public void deleteClock(){
		db.delete("clock", null, null);
	}
	
	public void deleteClock(Clock clock){
		db.delete("clock", "_id=?", new String[]{String.valueOf(clock.get_id())});
	}
	
	public void updateClock(Clock clock){
		final ContentValues cv = new ContentValues();
		cv.put("clockname", clock.getClockName());
		cv.put("clockimagepath", clock.getClockImagePath());
		db.update("clock", cv, "_id = ?", new String[]{String.valueOf(clock.get_id())});
	}
	
	public List<Clock> queryAllClock() {
		ArrayList<Clock> clocks = new ArrayList<Clock>();
		Cursor c = queryTheClockCursor();
        while (c.moveToNext()) {
        	Clock clock = new Clock();
        	clock.set_id(c.getInt(c.getColumnIndex("_id")));
        	clock.setClockName(c.getString(c.getColumnIndex("clockname")));
        	clock.setClockImagePath(c.getString(c.getColumnIndex("clockimagepath")));
        	clocks.add(clock);
        }
        c.close();
        return clocks;
	}
	
	public Cursor queryTheClockCursor() {
		Cursor c = db.rawQuery("SELECT * FROM clock ORDER BY clockname", null);
		return c;
	}
	
	public void addClock(Clock clock){
		final ContentValues cv = new ContentValues();
		cv.put("clockname", clock.getClockName());
		cv.put("clockimagepath", clock.getClockImagePath());
		db.insert("clock", null, cv);
	}
	
	public void deleteStation(Station station){
		db.delete("station", "_id=?", new String[]{String.valueOf(station.get_id())});
	}
	
	public void deleteStationes(Clock clock){
		db.delete("station", "clockid=?", new String[]{String.valueOf(clock.get_id())});
	}
	
	public void updateStation(Station station){
		final ContentValues cv = new ContentValues();
		cv.put("stationname", station.getStationName());
		cv.put("stationimagepath", station.getStationImagePath());
		db.update("station", cv, "_id = ?", new String[]{String.valueOf(station.get_id())});
	}
	
	public List<Station> queryAllStation(Clock clock) {
		ArrayList<Station> stationes = new ArrayList<Station>();
		Cursor c = queryTheStationCursor(clock);
        while (c.moveToNext()) {
        	Station station = new Station();
        	station.set_id(c.getInt(c.getColumnIndex("_id")));
        	station.setClockid(c.getInt(c.getColumnIndex("clockid")));
        	station.setStationName(c.getString(c.getColumnIndex("stationname")));
        	station.setStationImagePath(c.getString(c.getColumnIndex("stationimagepath")));
        	stationes.add(station);
        }
        c.close();
        return stationes;
	}
	
	public Cursor queryTheStationCursor(Clock clock) {
		Cursor c = db.rawQuery("SELECT * FROM station where clockid = ? ORDER BY stationname", new String[]{String.valueOf(clock.get_id())});
		return c;
	}
	
	public void addStation(Station station){
		final ContentValues cv = new ContentValues();
		cv.put("clockid", station.getClockid());
		cv.put("stationname", station.getStationName());
		cv.put("stationimagepath", station.getStationImagePath());
		db.insert("station", null, cv);
	}
	
	
	public void addClock(List<Clock> clocks) {
        db.beginTransaction();	//开始事务
        try {
        	for (Clock clock : clocks ) {
        		db.execSQL("INSERT INTO clock(clockname, clockimagepath) VALUES(?,?)", new Object[]{clock.getClockName(), clock.getClockImagePath()});
        	}
        	db.setTransactionSuccessful();	//设置事务成功完成
        } finally {
        	db.endTransaction();	//结束事务
        }
	}
	
	/**
	 * close database
	 */
	public void closeDB() {
		db.close();
	}
}
