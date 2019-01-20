package com.example.user.projet;

import android.Manifest;
import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static android.content.ContentValues.TAG;
import static com.example.user.projet.MainActivity.file_name;


public class Parssing {
    ArrayList<HashMap<String, String>> itemList;
    private static int REQUEST_CODE=1;
    public Parssing()
    {
        itemList = new ArrayList<>();
    }

    public ArrayList ParseXml() {


        try {
            if(isExternalStorageReadable()) {
                String fichier = file_name.substring(file_name.lastIndexOf("/")+1);
                File extStore = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


                String path = extStore.getAbsolutePath()+"/"+fichier;
                File myFile = new File(path);
                FileInputStream istream = new FileInputStream(myFile);
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(istream);
                NodeList nList = doc.getElementsByTagName("item");
                for (int i = 0; i < nList.getLength(); i++) {
                    if (nList.item(0).getNodeType() == Node.ELEMENT_NODE) {
                        HashMap<String, String> user = new HashMap<>();
                        Element elm = (Element) nList.item(i);
                        user.put("title", getNodeValue("title", elm));
                        user.put("link", getNodeValue("link", elm));
                        user.put("description", getNodeValue("description", elm));
                        user.put("pubDate",getNodeValue("pubDate",elm));
                        itemList.add(user);
                    }
                }
            }
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
         catch (IOException e) {
            e.printStackTrace();
        }
        catch (SAXException e) {
            e.printStackTrace();
        }
        return itemList;


    }



    protected String getNodeValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        Node node = nodeList.item(0);
        if(node!=null){
            if(node.hasChildNodes()){
                Node child = node.getFirstChild();
                while (child!=null){
                    if(child.getNodeType() == Node.TEXT_NODE){
                        return  child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }




    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.d("Memoire","memoire externe disponible en lecture");
            return true;
        }
        return false;
    }

}
