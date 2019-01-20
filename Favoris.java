package com.example.user.projet;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;

public class Favoris extends AppCompatActivity {
    private AccesDonnees accesDonnees;
    private static String authority = "fr.projet.rss";
    public RecyclerView recyclerView;
    public Button btn_delete_all;
    private ArrayList<Model> item_list = new ArrayList<>();
    private  FavorisAdapter mAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);

        accesDonnees = new AccesDonnees(this);
        getIntent();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        btn_delete_all = (Button) findViewById(R.id.btn_delete_all);
        Cursor cursor=accesDonnees.titres_favoris();
        while (cursor.moveToNext())
        {
            item_list.add(0,new Model(cursor.getString(1)));
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new FavorisAdapter(item_list);
        recyclerView.setAdapter(mAdapter);





        btn_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Delete_all_title(v);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favoris_menu, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                Intent main = new Intent(this,MainActivity.class);
                startActivity(main);
                return true;
            case R.id.flux:
                Intent fv = new Intent(this,TitleActivity.class);
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
