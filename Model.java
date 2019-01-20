package com.example.user.projet;

import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.io.Serializable;


public  class Model  implements Serializable {
    private static String authority = "fr.projet.rss";

    private String title_Name;


    public Model(String title_Name) {
        this.title_Name = title_Name;

    }

    public String getItemName() {
        return title_Name;
    }

    public void setItemName(String title_Name) {
        this.title_Name = title_Name;
    }





}
