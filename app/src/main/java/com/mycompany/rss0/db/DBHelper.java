package com.mycompany.rss0.db;

/**
 * Created by gposabella on 1/27/15.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {
    private static String TABLE_RSS = "rss";
    private static String TABLE_HOTWORD = "hotword";

    private static final String DB_NAME = "rssdb";
    private static final String BACKUP_DB = "rss_backup.db";

    public static String getDbName() {
        return DB_NAME;
    }

    private static final int DB_VERSION = 1;
    private static final String OUTFILE = "/backup_";

    private static DBHelper helper = null;

    public static DBHelper getHelper(Context context) {
        if (helper == null)
            helper = new DBHelper(context);
        return helper;
    }

    private Context context;

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "";
        //
        // sql += "CREATE TABLE android_metadata (locale TEXT);";
        // sql += "INSERT INTO android_metadata VALUES ('en_US');";
        //
        // // db.execSQL(sql);
        // sql = "";

        sql += "CREATE  TABLE   " + TABLE_RSS + "   (";
        sql += "_id INTEGER PRIMARY KEY,";
        sql += "link    TEXT    NOT NULL,";
        sql += "nome    TEXT    ";

        sql += ")";
        db.execSQL(sql);
        sql = "CREATE   TABLE   " + TABLE_HOTWORD + "   (";
        sql += "_id INTEGER PRIMARY KEY,";
        sql += "word    TEXT    ";

        sql += ")";
        db.execSQL(sql);
        sql = "insert into " + TABLE_HOTWORD + "  (word) values ('Renzi')";
        db.execSQL(sql);
        String[] lista = RSSList.getList();

        for (int i = 0; i < lista.length; i++) {
            sql = "insert into " + TABLE_RSS + "  (link) values ('" + lista[i]
                    + "')";
            db.execSQL(sql);
        }

        // sql = "";
        //
        // sql += "CREATE   TABLE   " + TABLE_IMAGES + "    (";
        // sql += "_id  INTEGER PRIMARY KEY,";
        // sql += "key  INTEGER NOT NULL,";
        // sql += "image    TEXT    NOT NULL";
        //
        // sql += ")";
        // db.execSQL(sql);
    }

    public static void exportDB(Context context) {
        exportDataBase(context);

    }

    public static void importDB(Context context) {
        SQLiteDatabase db = getHelper(context).getReadableDatabase();
        String sd = Environment.getExternalStorageDirectory().getAbsolutePath();

        File root = Environment.getExternalStorageDirectory();
        InputStream is = null;
        try {
            is = context.getAssets().open(BACKUP_DB);

            String currentDBPath = db.getPath();
            System.out.println(currentDBPath);
            // String backupDBPath = BACKUP_DB;

            File currentDB = new File(currentDBPath);
            FileOutputStream os = new FileOutputStream(currentDB);

            byte[] buffer = new byte[1024];
            try {
                int l;
                while ((l = is.read(buffer)) != -1) {
                    os.write(buffer, 0, l);
                }
                os.close();
                is.close();

                Toast.makeText(context, "DB loaded!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private static void exportDataBase(Context context) {
        SQLiteDatabase db = getHelper(context).getReadableDatabase();

        String sd = Environment.getExternalStorageDirectory().getAbsolutePath();

        FileChannel source = null;
        FileChannel destination = null;

        String currentDBPath = db.getPath();
        System.out.println(currentDBPath);
        String backupDBPath = BACKUP_DB;

        File currentDB = new File(currentDBPath);
        File backupDB = new File(sd + File.separator + backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(context, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static long insertRSS(Context context, RssData dish) {
        SQLiteDatabase db = getHelper(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        // values.put("_id", dish.getLink());
        values.put("nome", dish.getName());
        values.put("link", dish.getLink());

        long id = db.insert(TABLE_RSS, null, values);

        return id;
    }

    public static List<RssData> getRss(Context context, RssData dish) {
        List<RssData> result = new ArrayList<RssData>();

        SQLiteDatabase db = getHelper(context).getWritableDatabase();
        String values[] = { dish.getName() };
        String whereClause = "";

        Cursor cursor = db.query(TABLE_RSS, null, "nome =   ?", values, null,
                null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String key = cursor.getString(1);
            String name = cursor.getString(2);
            RssData d = new RssData();

            d.setName(name);

            result.add(d);

        }
        cursor.close();
        return result;
    }

    public static List<RssData> getAllRss(Context context) {
        List<RssData> result = new ArrayList<RssData>();

        SQLiteDatabase db = getHelper(context).getWritableDatabase();

        Cursor cursor = db.query(TABLE_RSS, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String link = cursor.getString(1);
            String name = cursor.getString(2);
            RssData d = new RssData();
            d.setId(id);
            d.setLink(link);
            d.setName(name);
            result.add(d);

        }
        cursor.close();
        return result;
    }
    public static List<String> getAllHotWord(Context context) {
        List<String> result = new ArrayList<String>();

        SQLiteDatabase db = getHelper(context).getWritableDatabase();

        Cursor cursor = db.query(TABLE_HOTWORD, null, null, null, null, null, null);
        while (cursor.moveToNext()) {

            String word = cursor.getString(1);

            result.add(word);

        }
        cursor.close();
        return result;
    }

    // public static int updateDish(Context context, Dish dish, int id) {
    // SQLiteDatabase db = getHelper(context).getWritableDatabase();
    // ContentValues values = new ContentValues();
    // values.put("nome", dish.getName());
    // String whereClause = "_id    =   ?";
    // String[] whereArgs = { "" + id };
    // int r = db.update(TABLE_NAMES, values, whereClause, whereArgs);
    // return r;
    // }

    // public static void deleteDish(Context context, int id) {
    // SQLiteDatabase db = getHelper(context).getWritableDatabase();
    // String whereClause = "_id    =   ?";
    // String[] whereArgs = { "" + id };
    // int r = db.delete(TABLE_NAMES, whereClause, whereArgs);
    // whereClause = "key   =   ?";
    // r = db.delete(TABLE_IMAGES, whereClause, whereArgs);
    // }

    public static void checkAndImportDB(Context context) {
        List<RssData> ll = getAllRss(context);
        if (ll.size() > 0)
            return;

        importDB(context);
    }

}