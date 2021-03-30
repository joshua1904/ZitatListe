package realease.zitatl.iste;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class bearbeiten extends AppCompatActivity implements View.OnClickListener {

    TextView view;
    Button bearbeiten,löschen;
    EditText nameEdit,datumEdit,zitatEdit;
    String neuesZitat;
    boolean beiFocusWechsselLoeschen=true;
    ArrayList<Zitat> zitatListe;
    ListView listView;
    LinearLayout layout;
    CheckBox weedBox,bierBox, favBox;
    boolean weed = false,bier = false,fav = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bearbeiten);
        bearbeiten=findViewById(R.id.bearbeiten);
        bearbeiten.setOnClickListener(this);
        löschen=findViewById(R.id.loechen);
        löschen.setOnClickListener(this);
        nameEdit=findViewById(R.id.nameEditb);
        layout = findViewById(R.id.linearLayout);
        weedBox = findViewById(R.id.weedBearbeitenBox);
        weedBox.setOnClickListener(this);
        bierBox = findViewById(R.id.bierBearbeitenBox);
        bierBox.setOnClickListener(this);
        favBox = findViewById(R.id.sternBearbeitenBox);
        favBox.setOnClickListener(this);
        auffüllen();
        try{

            zitatListe=new ArrayList<>();
            zitatListe=alleZitateDesPhilosophenGeben();


        }
        catch(Exception e)
        {

        }

        datumEdit=findViewById(R.id.datumEditb);
        zitatEdit=findViewById(R.id.zitatEditb);
        listView=findViewById(R.id.LISTE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==1) {
                    auffüllen();
                }
            }
        });
        auffüllen();
        adapterSetzen();

    }
    public void adapterSetzen()
    {
        ZitatAdapter adapter = new ZitatAdapter(this, R.layout.zitatview, zitatListe);
        listView.setAdapter(adapter);

    }

    /**
     * erstellt eine Liste mit allen Zitaten vom Zitierten des ausgewählten Zitats
     * @return
     */
    private ArrayList<Zitat> alleZitateDesPhilosophenGeben()
    {
       Speicher s = new Speicher();
       ArrayList<Zitat> ganzeZitatListe = s.loadArray(getApplicationContext(),"ZitatSpeicher");
        if(ganzeZitatListe != null)
        {
            String diesesZitat=getIntent().getExtras().getString("zitat");
            int position = getIntent().getExtras().getInt("position");
            zitatListe.add(new Zitat("This quote:"));
            zitatListe.add(ganzeZitatListe.get(position));
            zitatListe.add(new Zitat("More quotes of "+getName(diesesZitat)+":"));
            for(int i=0;i<ganzeZitatListe.size();i++)
            {
                // Testet ob die Namen des Zitates mit dem Namen des Original Zitats übereinstimmen, und ob das Zitat nicht das gleiche ist, wie ausgewhlt
                if(getName(ganzeZitatListe.get(i).ganzesZitatGeben()).toUpperCase().equals(getName(diesesZitat).toUpperCase())&& !ganzeZitatListe.get(i).ganzesZitatGeben().toUpperCase().equals(diesesZitat.toUpperCase()))
                {
                    zitatListe.add(ganzeZitatListe.get(i));
                }
            }
        }
        return zitatListe;
    }

    public String getName(String zitat)
    {

        String[] split=zitat.split("~");
        return split[1];
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.loechen:
                loeschBestaetigung();
                break;
            case R.id.bearbeiten:
                aendern();
                break;
            case R.id.LISTE:
                auffüllen();
                break;
            case R.id.weedBearbeitenBox:
                if(weedBox.isChecked())
                {
                    weed = true;
                }
                else
                {
                    weed = false;
                }
                break;
            case R.id.bierBearbeitenBox:
                if(bierBox.isChecked())
                {
                    bier = true;
                }
                else
                {
                    bier= false;
                }
                break;
            case R.id.sternBearbeitenBox:
                if(favBox.isChecked())
                {
                    fav = true;
                }
                else
                {
                    fav = false;
                }
                break;
        }

    }

    /**
     * füllt die textfelder mit dem ZItat/Dqatum/name/booleans auf
     */

    private void auffüllen() {
        try {
            zitatEdit.setText(zitatListe.get(1).getZitat());
            datumEdit.setText(zitatListe.get(1).getDatum());
            nameEdit.setText(zitatListe.get(1).getName());
            bierBox.setChecked(zitatListe.get(1).bierIntus());
            bier=zitatListe.get(1).bierIntus();
            weedBox.setChecked(zitatListe.get(1).weedIntus());
            weed = zitatListe.get(1).weedIntus();
            favBox.setChecked(zitatListe.get(1).fav());
            fav = (zitatListe.get(1).fav());

            beiFocusWechsselLoeschen = false;
        }
        catch (Exception e)
        {

        }

    }

    /**
     * bearbeitet das Zitat
     */
    private void aendern()
    {
        String zitat = zitatEdit.getText().toString().trim();
        String datum = datumEdit.getText().toString().trim();
        String name = nameEdit.getText().toString().trim();

        if (zitat.equals("") || datum.equals("") || name.equals(""))
        {
            Toast.makeText(this, "You have to fill all EditTexts :)",Toast.LENGTH_LONG).show();
        }
        else
        {

            ActivityWechsel(zitat,datum,name,weed,bier,fav);

        }


    }

    private void ActivityWechsel(String zitat,String datum,String name,boolean weed,boolean bier,boolean fav)
    {
        int position=getIntent().getExtras().getInt("position");
        Speicher s = new Speicher();
        ArrayList<Zitat> ladeListe = s.loadArray(getApplicationContext(),"ZitatSpeicher");
        ladeListe.set(position,new Zitat(zitat,datum,name,weed,bier,fav));
        s.safeArray(getApplicationContext(),ladeListe,"ZitatSpeicher");
        finish();
    }

    private void loeschen()
    {
        Speicher s = new Speicher();
        ArrayList liste = s.loadArray(getApplicationContext(),"ZitatSpeicher");
        int position=getIntent().getExtras().getInt("position");
        liste.remove(position);
        s.safeArray(getApplicationContext(),liste,"ZitatSpeicher");
       finish();
    }

    public void loeschBestaetigung()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this,R.style.dialogTheme);
        dialog.setTitle("Warning!");
        dialog.setIcon(R.mipmap.cadaver);
        dialog.setMessage("Do you really want to delete this Quote?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loeschen();
            }
        });
        dialog.setNegativeButton("N0", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.create().show();
    }
}