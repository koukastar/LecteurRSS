package com.example.user.basededonnees;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class MyContentProvider extends ContentProvider {
    private BaseRSS baseRss;

    private static final String LOG = "MyContentProvider";
    private static String authority="fr.projet.rss";
    private static final int CODE_ITEM = 1;
    private static final int CODE_HTTP = 2;
    private static final int CODE_desc=3;
    private static final int CODE_link=4;
    private static final int CODE_UPDATE=5;
    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        matcher.addURI(authority,"item",CODE_ITEM);
        matcher.addURI(authority,"http",CODE_HTTP);
        matcher.addURI(authority,"description/*",CODE_desc);
        matcher.addURI(authority,"link/*",CODE_link);
        matcher.addURI(authority,"update/*",CODE_UPDATE);


    }

    public MyContentProvider()
    {

    }


    @Override
    public boolean onCreate() {
        baseRss= BaseRSS.getInstance(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,  String sortOrder)
    {
        // TODO: Implement this to handle query requests from clients.
        SQLiteDatabase bd = baseRss.getReadableDatabase();
        int code = matcher.match(uri);
        Cursor cursor=null;
        try{
            switch (code){
                case CODE_HTTP:
                    cursor = bd.query("http",projection,selection,selectionArgs,null,null,null);
                    break;
                case CODE_ITEM:
                    cursor = bd.query("item",projection,selection,selectionArgs,null,null,null);
                    break;
                case  CODE_desc:
                    cursor = bd.query("item",projection,selection,selectionArgs,null,null,null);
                    break;
                case  CODE_link:
                    cursor = bd.query("item",projection,selection,selectionArgs,null,null,null);
                    break;

                default:
                    throw  new UnsupportedOperationException("Not yet implemented dans query");
            }
            return  cursor;
        }
        catch (SQLException e){
            Log.d(LOG,"ERREUR REQUETE : "+e.getMessage());
        }
        return cursor;
    }


    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public Uri insert(Uri uri,ContentValues values) {

        // TODO: Implement this to handle requests to insert a new row.
        SQLiteDatabase db = baseRss.getWritableDatabase();
        int code = matcher.match(uri);
        long id = 0;
        String path="";
        try{
            switch (code){
                case CODE_HTTP:

                    id = db.insertOrThrow("http",null,values);
                    path = "http";
                    Log.d(LOG,"URL values = "+values);
                    break;

                case CODE_ITEM:

                    id = db.insertOrThrow("item",null,values);
                    path = "item";
                    Log.d(LOG,"Item  values = "+values);
                    break;

                default:
                    throw new UnsupportedOperationException("this insert not yet implemented");
            }
        }
        catch (SQLException e){
            Log.d(LOG,"ERREUR",e);
        }


        Uri.Builder builder = (new Uri.Builder()).authority(authority).appendPath(path);
        return ContentUris.appendId(builder, id).build();
    }




    @Override
    public int delete( Uri uri,  String selection, String[] selectionArgs) {
        SQLiteDatabase db = baseRss.getWritableDatabase();
        int code = matcher.match(uri);
        int i = 0;
        try{
            switch (code){
                case CODE_ITEM:
                    i = db.delete("item",selection,selectionArgs);
                    Log.d(LOG,selection+" supprimé");
                    break;
                default:
                    throw new UnsupportedOperationException("this insert not yet implemented");
            }
        }
        catch (SQLException e)
        {
            Log.d(LOG,"Erreur ",e);
        }
        return i;
    }

    @Override
    public int update(Uri uri, ContentValues values,  String selection, String[] selectionArgs)
    {

        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        SQLiteDatabase bd = baseRss.getReadableDatabase();
        int code = matcher.match(uri);

        try{
            switch (code){
                case CODE_UPDATE:
                    bd.update("item",values,selection,selectionArgs);
                    break;
                default:
                    throw  new UnsupportedOperationException("Not yet implemented dans query");
            }

        }
        catch (SQLException e){
            Log.d(LOG,"ERREUR REQUETE : "+e.getMessage());
        }
        //throw new UnsupportedOperationException("Not yet implemented");
        return 1;
    }
}
