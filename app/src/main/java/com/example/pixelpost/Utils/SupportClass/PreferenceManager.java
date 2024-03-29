package com.example.pixelpost.Utils.SupportClass;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class PreferenceManager {
    public static String KEY_PREFERENCE_NAME = "pixel_post_preference";
    private final SharedPreferences sharedPreferences;
    public PreferenceManager(Context context)
    {
        if(context!=null)
            sharedPreferences = context.getSharedPreferences(KEY_PREFERENCE_NAME,Context.MODE_PRIVATE);
        else
            sharedPreferences=null;
    }
    public void putBoolean(String key, Boolean value)
    {
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }
    public Boolean getBoolean(String key)
    {
        return sharedPreferences.getBoolean(key,false);
    }
    public void putString(String key,String value)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public String getString(String key)
    {
        return sharedPreferences.getString(key,null);
    }
    public void clear()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
    public void putFilters(String keyName, ArrayList<String> filters) {
        StringBuilder sb = new StringBuilder();
        for (String filter : filters) {
            sb.append(filter).append(",");
        }
        String filterString = sb.toString().substring(0, sb.length() - 1);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(keyName, filterString);
        editor.apply();
    }

    public ArrayList<String> getFilters(String keyName) {
        ArrayList<String> listString;
        String filterString = sharedPreferences.getString(keyName, null);
        if (filterString != null && !filterString.isEmpty()) {
            listString = new ArrayList<>(Arrays.asList(filterString.split(",")));
            listString.size();
            return listString;
        }
        return null;
    }
    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }
    public void putSerializable(String key, Serializable value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(value);
            objectOutputStream.close();
            byte[] objectBytes = byteArrayOutputStream.toByteArray();
            editor.putString(key, Base64.encodeToString(objectBytes, Base64.DEFAULT));
            editor.apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Serializable getSerializable( String key) {
        String objectString = sharedPreferences.getString(key, null);
        if (objectString != null) {
            byte[] objectBytes = Base64.decode(objectString, Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(objectBytes);
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                Serializable object = (Serializable) objectInputStream.readObject();
                objectInputStream.close();
                return object;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public void removeKey( String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }
}

