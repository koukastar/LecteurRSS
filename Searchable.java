package com.example.user.projet;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Searchable extends ListActivity{
    String authority;
    ContentResolver contentResolver;
    SimpleCursorAdapter listAdapter;
    AccesDonnees a;

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor c = (Cursor)getListAdapter().getItem(position);
        String url_name = c.getString(c.getColumnIndex("title"));
        Affichage(v,position,this,url_name);
        //Toast.makeText(this,url_name,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        a = new AccesDonnees(this);
        authority="fr.projet.rss";
        String query = "";
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);


        }



        contentResolver=this.getContentResolver();
        doSearchAuthor(query);
        setListAdapter(listAdapter);





    }



    private void doSearchAuthor(String query) {

       Uri.Builder builder = new Uri.Builder();

        builder.scheme("content")
                .authority(authority)
                .appendPath("chercher_title").appendPath(query);

        final Uri uri = builder.build();
        Bundle bundle = new Bundle();
        bundle.putString("query", query);
        listAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, null,
                new String[]{"title"},
                new int[]{android.R.id.text1}, 0);

        getLoaderManager().initLoader(4, bundle,
                new LoaderManager.LoaderCallbacks<Cursor>() {

                    @Override
                    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                        Uri.Builder builder = new Uri.Builder();
                        builder.scheme("content").authority(authority).appendPath("item");
                        Uri uri = builder.build();
                        String query = args.getString("query");
                        String selectCond = null;
                        selectCond = "title like '%" + query + "%' OR description like '%" + query + "%'";
                        String[] projection = new String[]{"rowid as _id","title"};
                        return new CursorLoader(getApplicationContext(),uri,projection,selectCond,null,null);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                        listAdapter.swapCursor(cursor);
                        listAdapter.notifyDataSetChanged();
                    }


                    @Override
                    public void onLoaderReset(Loader<Cursor> loader) {
                        listAdapter.swapCursor(null);
                        listAdapter.notifyDataSetChanged();
                    }
                });





    }

    // Affichage de description
    private void Affichage(View v, final int position, final Context c  , final String title) {

        final String description = a.get_description(title);

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

        builder.setMessage("Description \n" + description)
                .setCancelable(false)
                .setPositiveButton("View ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String url= a.get_link(description);
                                Intent i= new Intent(c,Flux.class);
                                i.putExtra("url",String.valueOf(url));
                                c.startActivity(i);
                                Log.d("1","ok");


                            }
                        })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                });

        builder.show();

    }



}
