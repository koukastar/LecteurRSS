package com.example.user.projet;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Date;

public class AccesDonnees {
    ContentResolver contentResolver;
    public final static int VERSION = 13;
    public final static String DB_NAME = "base_rss";
    public final static String TABLE_ITEM = "item";
    public final static String COLONNE_LINK = "link";
    public final static String COLONNE_TITLE = "title";
    public final static String COLONNE_DESCRIPTION = "description";
    public final static String COLONNE_DATE = "pubDate";


    public final static String TABLE_HTTP= "http";
    public final static String COLONNE_URL = "url";
    public Context c;

    private static String authority="fr.projet.rss";
    private Uri.Builder builder;
    private Uri uri;
    private ContentValues values;

    public AccesDonnees(Context c)
    {
        contentResolver = c.getContentResolver();
    }



    public void ajoutURL(String url)
    {
        builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("http");
        uri = builder.build();
        values = new ContentValues();
        values.put(COLONNE_URL,url);
        uri = contentResolver.insert(uri,values);

    }


    public  void  ajoutItem(String link , String title , String description, String date)
    {
        builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("item");
        uri= builder.build();
        values= new ContentValues();
        values.put(COLONNE_LINK,link);
        values.put(COLONNE_TITLE,title);
        values.put(COLONNE_DESCRIPTION,description);
        values.put(COLONNE_DATE,date);
        uri = contentResolver.insert(uri,values);
    }



    public  Cursor TT_les_titres()
    {
        String pubDate="";
        builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("item");
        uri = builder.build();
        String[] projection = new String[]{"rowid as _id","title"};
        String where = COLONNE_DATE + "!=?";
        String[] selectionArgs = new String[]{pubDate};
        Cursor cursor = contentResolver.query(uri,projection,where,selectionArgs,null);
        return cursor;
    }

    public  Cursor dates_pub()
    {
        String pubDate="";
        builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("item");
        uri = builder.build();
        String[] projection = new String[]{"rowid as _id","pubDate"};
        String where = COLONNE_DATE + "!=?";
        String[] selectionArgs = new String[]{pubDate};
        Cursor cursor = contentResolver.query(uri,projection,where,selectionArgs,null);
        return cursor;
    }

    public  Cursor titres_favoris()
    {
        String pubDate="";
        String[] selectionArgs = new String[]{pubDate};
        builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("item");
        uri = builder.build();
        String[] projection = new String[]{"rowid as _id","title"};
        String where = COLONNE_DATE + "=?";
        Cursor cursor = contentResolver.query(uri,projection,where,selectionArgs,null);

        return cursor;
    }




    public void Update_date(String title){
        builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("update").appendPath(title);
        uri = builder.build();
        String[] projection = new String[]{title};
        String where = COLONNE_TITLE + "=?";
        values= new ContentValues();
        values.put(COLONNE_DATE, "");
        contentResolver.update(uri, values, where, projection);

    }

    public  void Delete(String title)
    {
        builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("item");
        uri = builder.build();
        String where = COLONNE_TITLE + "=?";
        String[] projection = new String[]{title};
        contentResolver.delete(uri,where,projection);

    }


    public  void Delete_date(String date)
    {
        builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("item");
        uri = builder.build();
        String where = COLONNE_DATE + "=?";
        String[] projection = new String[]{date};
        contentResolver.delete(uri,where,projection);

    }

    public  void Delete_all()
    {
        builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("item");
        uri = builder.build();
        contentResolver.delete(uri,null,null);

    }

    public String get_description(String title)
    {
        builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("description").appendPath(title);
        uri = builder.build();

        String[] projection = new String[]{COLONNE_DESCRIPTION};
        String[] Args = new String[]{title};
        String selection = COLONNE_TITLE +" = ? ";

        Cursor cursor = contentResolver.query(uri,projection,selection,Args,"");
        if (cursor.moveToFirst())
            return cursor.getString(0);
        else return "";

    }


    public String get_link(String description)
    {
        builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("link").appendPath(description);
        uri = builder.build();

        String[] projection = new String[]{COLONNE_LINK};
        String[] Args = new String[]{description};
        String selection = COLONNE_DESCRIPTION +" = ? ";

        Cursor cursor = contentResolver.query(uri,projection,selection,Args,"");
        if (cursor.moveToFirst())
            return cursor.getString(0);
        else return "";

    }







}
