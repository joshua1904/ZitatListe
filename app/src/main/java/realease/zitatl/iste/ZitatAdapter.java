package realease.zitatl.iste;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ZitatAdapter extends ArrayAdapter<Zitat>  {

    /**
     * @param context
     * @param recource
     * @param objects
     */
    private static final String TAG = "ZitatAdapter";
            private Context mContext;
            private int mRecource;
            String weed = "", fav = "", bier ="";

    public ZitatAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Zitat> objects) {
        super(context, resource, objects);
        mContext = context;
        mRecource = resource;
    }

    @NonNull
    @Override
    /**
     * füllt das zitatView Layout aus
     */
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String zitat = getItem(position).ganzesZitatGeben();
        if(getItem(position).weedIntus())
        {
            weed = "\uD83E\uDD66";
        }
        else weed = "";
        if(getItem(position).bierIntus())
        {
            bier ="\uD83C\uDF7A";

        }
        else bier = "";
        if(getItem(position).fav())
        {
            fav = "⭐";
        }else fav = "";
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mRecource,parent,false);
        TextView zitatView = convertView.findViewById(R.id.zitatView);
        TextView weedView = convertView.findViewById(R.id.weedView);
        TextView favView = convertView.findViewById(R.id.FavView);
        TextView bierView = convertView.findViewById(R.id.BierView);
        zitatView.setText(zitat);
        weedView.setText(weed);
        favView.setText(fav);
        bierView.setText(bier);
        return convertView;

    }


}
