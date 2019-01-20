package com.example.user.basededonnees;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseRSS  extends SQLiteOpenHelper {
    public final static int VERSION = 27
            ;
    public final static String DB_NAME = "base_rss";
    public final static String TABLE_ITEM = "item";
    public final static String COLONNE_LINK = "link";
    public final static String COLONNE_TITLE = "title";
    public final static String COLONNE_DESCRIPTION = "description";
    public final static String COLONNE_DATE = "pubDate";



    public final static String TABLE_HTTP= "http";
    public final static String COLONNE_URL = "url";

    public final static String CREATE_ITEM = "create table " + TABLE_ITEM + "(" +
            COLONNE_LINK + " string primary key, " +
            COLONNE_TITLE + " string, " +
            COLONNE_DESCRIPTION + " string, " +
            COLONNE_DATE + " integer " + ");";


    public final static String CREATE_HTTP = "create table " + TABLE_HTTP + "(" +
            COLONNE_URL + " string primary key " + ");";

    private static BaseRSS ourInstance;

    public static BaseRSS getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new BaseRSS(context);
        return ourInstance;
    }

    private BaseRSS(Context context)
    {
        super(context, DB_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_ITEM);
        db.execSQL(CREATE_HTTP);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (newVersion > oldVersion) {
            db.execSQL("drop table if exists " + TABLE_ITEM);
            db.execSQL("drop table if exists " +TABLE_HTTP);
            onCreate(db);
        }

    }
}
