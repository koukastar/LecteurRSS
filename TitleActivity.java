package com.example.user.projet;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TitleActivity extends AppCompatActivity  {


    private AccesDonnees accesDonnees;
    private static String authority = "fr.projet.rss";
    public RecyclerView recyclerView;
    public Button btn_delete_all ;
    private ArrayList<Model> item_list = new ArrayList<>();
    private  ModelAdapter mAdapter;

    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        accesDonnees = new AccesDonnees(this);
        getIntent();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        btn_delete_all = (Button) findViewById(R.id.btn_delete_all);

        Cursor cursor=accesDonnees.TT_les_titres();
        while (cursor.moveToNext())
        {
            item_list.add(0,new Model(cursor.getString(1)));
        }



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new ModelAdapter(item_list);
        recyclerView.setAdapter(mAdapter);




        btn_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Delete_all_title(v);
                    Snackbar.make(v, "Tout les flux sont supprimés.", Snackbar.LENGTH_LONG).show();

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchManager searchManager = (SearchManager) getSystemService((Context.SEARCH_SERVICE));
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                Intent main = new Intent(this,MainActivity.class);
                startActivity(main);
                return true;
            case R.id.action_favorite:
                Intent fv = new Intent(this,Favoris.class);
                startActivity(fv);

            default:
        }

        return super.onOptionsItemSelected(item);
    }

    // Supprimer tous les flux
    private void Delete_all_title(View v ) {

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

        builder.setMessage("Veuillez vous supprimées tous les flux")
                .setCancelable(false)
                .setPositiveButton("CONFIRM",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                accesDonnees.Delete_all();
                                item_list.clear();
                                mAdapter.notifyDataSetChanged();



                            }
                        })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                });

        builder.show();

    }



}

