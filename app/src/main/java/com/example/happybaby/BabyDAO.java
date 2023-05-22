package com.example.happybaby;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class BabyDAO extends SQLiteOpenHelper {

    private static final int DATA_BASE_VERSION = 1;
    private static final String DATA_BASE_NAME = "baby_data";
    private static final String TABLE_NAME = "measurements";

    private static final String KEY_ID = "id";

    private static final String BABY_HEIGHT = "baby_height";
    private static final String BABY_WEIGHT = "baby_weight";


    public BabyDAO(Context context) {
        super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_MEASUREMENTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + BABY_HEIGHT + " REAL,"
                + BABY_WEIGHT + " REAL" + ")";
        db.execSQL(CREATE_MEASUREMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void create(Baby baby) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BABY_HEIGHT, baby.getBabyHeight());
        values.put(BABY_WEIGHT, baby.getBabyWeight());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<Baby> readAll(){
        List<Baby> measurements = new ArrayList<>();
        String quarry = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(quarry,null);

        if(cursor.moveToFirst()){
            do{
                Baby baby = new Baby();
                baby.setId(Integer.parseInt(cursor.getString(0)));
                baby.setBabyHeight(cursor.getDouble(1));
                baby.setBabyWeight(cursor.getDouble(2));
                measurements.add(baby);
            } while (cursor.moveToNext());
            db.close();
        }
        return measurements;

    }

    public int update (Baby baby){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BABY_HEIGHT, baby.getBabyHeight());
        values.put(BABY_WEIGHT, baby.getBabyWeight());

        int message = db.update(TABLE_NAME,values,KEY_ID + " =?",
                new String[]{String.valueOf(baby.getId())});
        db.close();
        return message;
    }

    public void delete (Baby baby){
        SQLiteDatabase db=this.getWritableDatabase();

        db.delete(TABLE_NAME, KEY_ID + "=?",
                new String[]{String.valueOf(baby.getId())});
        db.close();
    }
}
