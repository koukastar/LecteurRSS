package com.example.user.projet;

import android.app.ListActivity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class URL_DATA extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>  {
    public ListView list;
    private SimpleCursorAdapter adapter;
    private AccesDonnees accesDonnees;
    private static String authority = "fr.projet.rss";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url__dat);
        list=(ListView) findViewById(android.R.id.list);
        accesDonnees = new AccesDonnees(this);
        getIntent();
        adapter = new SimpleCursorAdapter(this,
                android.R.layout.two_line_list_item,
                null,
                new String[]{"url"},
                new int[]{android.R.id.text1});

        setListAdapter(adapter);
        list.setAdapter(adapter);

        android.app.LoaderManager manager = getLoaderManager();
        manager.initLoader(0,null,this);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor c = (Cursor)getListAdapter().getItem(position);
        String url_name = c.getString(c.getColumnIndex("url"));
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("url",url_name);
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("http");
        Uri uri = builder.build();

        String[] projection = new String[]{"rowid as _id","*"};

        return new CursorLoader(this,uri,projection,null,null,null);
    }


    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
        adapter.notifyDataSetChanged();

    }
}
