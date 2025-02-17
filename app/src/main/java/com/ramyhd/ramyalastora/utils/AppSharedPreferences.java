package com.ramyhd.ramyalastora.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ramyhd.ramyalastora.classes.responses.players_details.ParticipatingLeague;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by baher on 9/25/16.
 */
public class AppSharedPreferences {

    private final static String SHARED_FILE = "_dataFile";
    private final static String Delivery_Status = "deliveryStatus";


    private SharedPreferences pref ;



    public AppSharedPreferences(Context context) {
        pref = context.getSharedPreferences(SHARED_FILE, Context.MODE_PRIVATE);
    }



    public void writeString(String key1, String value1) {

        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putString(key1, value1);
        prefEditor.commit();

    }


    public String readString(String key2) {
        return pref.getString(key2, "");
    }
//////


//////

    public void writeArray(String key3, ArrayList arrayList3) {

        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList3);
        editor.putString(key3, json);
        editor.apply();

    }

    public ArrayList<ParticipatingLeague> readArray(String key4){
        ArrayList<ParticipatingLeague> upOrderArrayList ;
        Gson gson = new Gson();
        String json = pref.getString(key4, null);
        Type type = new TypeToken<ArrayList<ParticipatingLeague>>(){}.getType();
        upOrderArrayList = gson.fromJson(json, type);

        if (upOrderArrayList == null){
            upOrderArrayList = new ArrayList<>();
        }

        return upOrderArrayList;
    }

    /*public ArrayList<Orderitem> orderitem_readArray(String key4){
        ArrayList<Orderitem> upOrderArrayList ;
        Gson gson = new Gson();
        String json = pref.getString(key4, null);
        Type type = new TypeToken<ArrayList<Orderitem>>(){}.getType();
        upOrderArrayList = gson.fromJson(json, type);

        if (upOrderArrayList == null){
            upOrderArrayList = new ArrayList<>();
        }

        return upOrderArrayList;
    }

    public ArrayList<String> readArraymobileCallCenter(String key4){
        ArrayList<String> upOrderArrayList ;
        Gson gson = new Gson();
        String json = pref.getString(key4, null);
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        upOrderArrayList = gson.fromJson(json, type);

        if (upOrderArrayList == null){
            upOrderArrayList = new ArrayList<>();
        }

        return upOrderArrayList;
    }*/

    public void writeInteger(String key5, int value5) {

        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putInt(key5, value5);
        prefEditor.commit();

    }

    public int readInteger(String key6) {
        return pref.getInt(key6, 0);
    }






    public void writeBoolean(String key7, boolean value7) {

        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putBoolean(key7, value7);
        prefEditor.commit();

    }

    public boolean readBoolean(String key8) {
        return pref.getBoolean(key8, false);
    }





    public void clear() {
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.clear();
        prefEditor.commit();

    }

    public void defualtData () {
        SharedPreferences.Editor prefEditor = pref.edit();
        ///////////////////////////////////////////
/*      prefEditor.putString("token",null);
        prefEditor.putString("refresh_token",null);*/
        ///////////////////////////////////////////
        prefEditor.commit();
    }

}
