package realease.zitatl.iste;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Speicher {
    SharedPreferences sp;

    public ArrayList<Zitat> loadArray(Context context, String key)
    {
        sp = context.getSharedPreferences("p", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString(key, null);
        Type type = new TypeToken<ArrayList<Zitat>>() {}.getType();
        ArrayList<Zitat> list = gson.fromJson(json, type);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public void safeZitat(Context context,Zitat test,String key)
    {
        sp = context.getSharedPreferences("p",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        ArrayList<Zitat> list = loadArray(context,key);
        list.add(test);
        String json = gson.toJson(list);
        editor.putString(key,json);
        editor.apply();
    }

    public void safeArray(Context context,ArrayList<Zitat> list,String key)
    {
        sp = context.getSharedPreferences("p",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key,json);
        editor.apply();
    }
    public void loeschen(Context context,String key)
    {
        sp = context.getSharedPreferences("p",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        ArrayList<Zitat> list = new ArrayList<>();
        String json = gson.toJson(list);
        editor.putString(key,json);
        editor.apply();
    }
}
