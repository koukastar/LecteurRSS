package com.example.user.projet;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class MainActivity extends AppCompatActivity {
    /* Declaration des variables */
    Parssing parssing;
    EditText url_rss;
    public  static DownloadManager dm;
    public  ProgressBar mProgressBar;
    DownloadManager.Request request;
    public  AccesDonnees accesDonnees;
    String url_name="";
    public static  String file_name="",fichier;
    String url;
    ArrayList<HashMap<String, String>> itemList;
    Boolean downoald = false;
    File myFile;
    Button enregistrer,annuler, down;
    TextView txt;
    long enqueue;
    private static int REQUEST_CODE=1;
    private static int REQUEST_CODE2=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, REQUEST_CODE);
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, REQUEST_CODE2);

        url_rss=findViewById(R.id.url);
        enregistrer = findViewById(R.id.enregistrer);
        annuler = findViewById(R.id.annuler);
        down = findViewById(R.id.downoald);
        annuler.setVisibility(View.INVISIBLE);
        mProgressBar=findViewById(R.id.progressBar);
        mProgressBar.setMax(1);
        txt = findViewById(R.id.output);
        dm = (DownloadManager) this.getSystemService(this.DOWNLOAD_SERVICE);
        accesDonnees = new AccesDonnees(this);
        parssing = new Parssing();
        itemList = new ArrayList<>();
         /** Nettoyage automatique de la base de données**/
        Nettoyage();

        /** Ajout des URL **/

        Intent i=getIntent();
        url_name = i.getStringExtra("url");
        url_rss.setText(url_name);





    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuA:
                Choisirurl();
                return true;
            case R.id.menuB:
                Afficher_flux();
                return true;
            case R.id.action_favorite:
                Intent fv = new Intent(this,Favoris.class);
                startActivity(fv);

            default:
        }

        return super.onOptionsItemSelected(item);
    }


/** Téléchargement  de flux **/
    public void chagerxml(View v) {


       if (isDownloadManagerAvailable(this)) {
            if ((url_rss.getText().toString().equals(""))) {

                Toast.makeText(this, "Veuillez saisir une adresse", Toast.LENGTH_LONG).show();
            }
            else {
                down.setVisibility(View.INVISIBLE);
                annuler.setVisibility(View.VISIBLE);
                String url = url_rss.getText().toString();
                file_name = url.substring(12, url.length());
                fichier = file_name.substring(file_name.lastIndexOf("/") + 1);
                String xml_extension = fichier.substring(fichier.indexOf(".")+1);

                    if (xml_extension.equals("xml")) {
                        request = new DownloadManager.Request(Uri.parse(url));

                        request.setDescription("Rss file");
                        request.setTitle("Rss");

                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fichier);
                        //Toast.makeText(this, "File sauvegardé  " + fichier, Toast.LENGTH_SHORT).show();
                        try {
                            setParssing();
                            Snackbar.make(v, "insertion", Snackbar.LENGTH_LONG).show();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        /* get download service and enqueue file*/
                        enqueue = dm.enqueue(request);
                        new DownloadProgressCounter(dm.enqueue(request)).start();

                        BroadcastReceiver receiver = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                String action = intent.getAction();
                                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                                    long downloadId = intent.getLongExtra(
                                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                                    DownloadManager.Query query = new DownloadManager.Query();
                                    query.setFilterById(enqueue);
                                    Cursor c = dm.query(query);
                                    if (c.moveToFirst()) {
                                        int columnIndex = c
                                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
                                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                                .getInt(columnIndex)) {
                                            downoald = true;
                                            down.setVisibility(View.VISIBLE);
                                            annuler.setVisibility(View.INVISIBLE);

                                            Toast toast = Toast.makeText(context, "download finished", Toast.LENGTH_SHORT);
                                            //toast.show();
                                        }
                                    }
                                }
                            }
                        };

                        this.registerReceiver(receiver, new IntentFilter(
                                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                        Delete_file();


                    } else {

                        Toast.makeText(this, "Veuillez saisir un fichier xml", Toast.LENGTH_LONG).show();

                    }






            }


       }




    }

/** Delete file de l'apareil */
    public void Delete_file()
    {
        File extStore = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String path = extStore.getAbsolutePath() + "/" + fichier;
        myFile = new File(path);
        if (myFile.exists()) {
            myFile.delete();
            Log.d(String.valueOf(3),"File delete");

        }
    }


/** Sauvegarder les flux parsé dans la base de données **/
    public  void setParssing() throws ParseException {
        String title, link, description, date;
        Cursor cursor;
        itemList = parssing.ParseXml();



        for (int i = 0; i < itemList.size(); i++) {
            title = itemList.get(i).get("title");
            link = itemList.get(i).get("link");
            description = itemList.get(i).get("description");
            date = itemList.get(i).get("pubDate");
            date = date.substring(0, 25);
            accesDonnees.ajoutItem(link, title, description, date);



        }




    }
    /** Annuler le téléchargement **/

    public void annuler(View v)
    {

        dm.remove(enqueue);
        dm = (DownloadManager) this.getSystemService(this.DOWNLOAD_SERVICE);
        down.setVisibility(View.VISIBLE);
        annuler.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.GONE);

    }

/** Nettoyage de la base de Données **/
    public void Nettoyage()
    {
        Cursor cursor = accesDonnees.dates_pub();
        String item_date;
        Date current_date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

        while (cursor.moveToNext()) {

                item_date = cursor.getString(1);

                String month = item_date.substring(8, 11);
                int mm = getMonth(month);
                String day = item_date.substring(5, 7);

                String year = item_date.substring(12,16);
                 Log.d(String.valueOf(1),item_date+" year  " + year + "month "+mm +" day "+day);
                /** get current_year **/
                simpleDateFormat.applyPattern("yyyy");
                int year_current = Integer.parseInt(simpleDateFormat.format(current_date));
                simpleDateFormat.applyPattern("MM");
                int month_current = Integer.parseInt(simpleDateFormat.format(current_date));
                simpleDateFormat.applyPattern("dd");
                int day_current = Integer.parseInt(simpleDateFormat.format(current_date));
                int result = day_current - Integer.parseInt(day);
                if(year_current - Integer.parseInt(year) == 0)
                {

                    if(month_current - mm !=0)
                    {


                            if (result < 0)
                            {
                                result = result *(-1);
                            }
                            int calcul = (31 - result) * 24;
                            if (calcul == 720 || calcul > 720)
                            {
                                accesDonnees.Delete_date(item_date);
                                Log.d(String.valueOf(2),"title supprimé de la date "+  item_date);
                            }


                        }


                }
                else
                {
                    if((year_current - Integer.parseInt(year) ==1) && (month_current == 1) && (mm == 12))
                    {

                        if (result < 0)
                        {
                            result = result *(-1);
                        }
                        int calcul = (31 - result) * 24;
                        if (calcul == 720 || calcul > 720)
                        {
                            accesDonnees.Delete_date(item_date);
                        }

                    }
                    else
                    {
                        accesDonnees.Delete_date(item_date);
                    }

                }





        }
    }






/** Choisir URL de la base de données**/
    public void Choisirurl()
    {
        Intent intent_url = new Intent(this,URL_DATA.class);
        startActivity(intent_url);
    }


/**Sauvegarder URL dans la base de données **/
    public void enregistrerURL(View v)
    {
        if(url_rss.getText().toString().equals(""))
        {
            Toast.makeText(this,"Veuillez saisir une adresse",Toast.LENGTH_LONG).show();
        }
        else
        {
            accesDonnees.ajoutURL( url_rss.getText().toString());
            Toast.makeText(this,"URL sauvegarder",Toast.LENGTH_LONG).show();
        }

    }


/** Consulter Flux sauvegardé  dans la base de données**/
    public  void  Afficher_flux ()
    {
       Intent i = new Intent(this,TitleActivity.class);
       startActivity(i);
    }



        /**
         * @param / used to check the device version and DownloadManager information
         * @return true if the download manager is available
         */
      public static boolean isDownloadManagerAvailable(Context context) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                return true;
            }
            return false;
        }

/** Get Month() **/
        public int getMonth(String month)
        {
            int month_num =0;
           if(month.equals("Jan"))
           {
               month_num = 1;
           }
            if(month.equals("Feb"))
            {
                month_num = 2;
            }
            if(month.equals("Mar"))
            {
                month_num = 3;
            }
            if(month.equals("Apr"))
        {
            month_num = 4;
        }
        if(month.equals("May"))
        {
            month_num = 5;
        }
        if(month.equals("Jun"))
        {
            month_num = 6;
        }
        if(month.equals("Jul"))
        {
            month_num = 7;
        }
        if(month.equals("Aug"))
        {
            month_num = 8;
        }
        if(month.equals("Sep"))
        {
            month_num = 9;
        }
        if(month.equals("Oct"))
        {
            month_num = 10;
        }
        if(month.equals("Nov"))
        {
            month_num = 11;
        }
        if(month.equals("Dec"))
        {
            month_num = 12;
        }

            return month_num;
        }

        /** Vérifier si mémoire disponible en lecture **/

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.d("Memoire","memoire externe disponible en lecture");
            return true;
        }
        return false;
    }






/** Progress  Bar **/
    class DownloadProgressCounter extends Thread {

        private final long downloadId;
        private final DownloadManager.Query query;
        private Cursor cursor;
        private int lastBytesDownloadedSoFar;
        private int totalBytes;

        public DownloadProgressCounter(long downloadId) {
            this.downloadId = downloadId;
            this.query = new DownloadManager.Query();
            query.setFilterById(enqueue);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void run() {

            while (downloadId> 0) {
                try {
                    Thread.sleep(300);

                    cursor = dm.query(query);
                    if (cursor.moveToFirst()) {

                        //get total bytes of the file
                        if (totalBytes <= 0) {
                            totalBytes = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        }

                        final int bytesDownloadedSoFar = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));

                        if (bytesDownloadedSoFar == totalBytes && totalBytes > 0) {
                            this.interrupt();
                        } else {
                            //update progress bar
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(mProgressBar.getProgress() + (bytesDownloadedSoFar - lastBytesDownloadedSoFar));
                                    lastBytesDownloadedSoFar = bytesDownloadedSoFar;

                                }

                            });

                        }

                        mProgressBar.setVisibility(View.GONE);
                    }
                    cursor.close();
                } catch (Exception e) {
                    return;
                }
            }

        }

    }

}
