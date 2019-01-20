package com.example.user.projet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class FavorisAdapter extends  RecyclerView.Adapter<FavorisAdapter.ViewHolder> {


public ArrayList<Model> item_list;





public FavorisAdapter(ArrayList<Model> arrayList) {

        item_list = arrayList;
        }


    @NonNull
    @Override
    public FavorisAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_favoris_adapter, null);

        // create ViewHolder
        FavorisAdapter.ViewHolder viewHolder = new FavorisAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
public void onBindViewHolder(final FavorisAdapter.ViewHolder holder, final int position) {
final int pos = position;

        holder.item_name.setText(item_list.get(position).getItemName());



        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
public void onClick(View v) {
        holder.accesDonnees.Delete(item_list.get(position).getItemName());
        deleteItemFromList(v, pos);


        }
        });


        holder.item_name.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Affichage(v,pos,holder.context,holder.accesDonnees);

        }
        });

        }

@Override
public int getItemCount() {
        return item_list.size();
        }


// Delete title
private void deleteItemFromList(View v, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());


        builder.setMessage("Delete Title ?")
        .setCancelable(false)
        .setPositiveButton("CONFIRM",
        new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int id) {

        item_list.remove(position);
        notifyDataSetChanged();


        }
        })
        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int id) {


        }
        });

        builder.show();

        }




// Affichage de description
private void Affichage(View v, final int position, final Context c , final AccesDonnees a) {
final String description= a.get_description(item_list.get(position).getItemName());


        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

        builder.setMessage("Description \n" + a.get_description(item_list.get(position).getItemName()))
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


public static class ViewHolder extends RecyclerView.ViewHolder {

    public final TextView item_name;
    public ImageButton btn_delete ;
    public  AccesDonnees accesDonnees;
    public final Context context;
    public ImageView imageView;

    public ViewHolder(View itemLayoutView) {
        super(itemLayoutView);

        item_name = (TextView) itemLayoutView.findViewById(R.id.txt_Name);
        btn_delete = (ImageButton) itemLayoutView.findViewById(R.id.btn_delete_unit);
        accesDonnees = new AccesDonnees(itemLayoutView.getContext());
        imageView = itemLayoutView.findViewById(R.id.imgview);

        context = itemLayoutView.getContext();
    }
}
}
