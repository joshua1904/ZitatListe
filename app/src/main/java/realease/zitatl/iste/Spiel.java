package realease.zitatl.iste;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Spiel extends AppCompatActivity implements View.OnClickListener {
    TextView Zitatview;
    ListView listView;
    ArrayList<String> zitateListe,randomZitateListe,nameListe;
    boolean ende=false, geraten=false;
    int randomListeId=0;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spiel);
        zitateListe=new ArrayList<>();
       zitateListe=crapladen();
       nameListe=new ArrayList<>();
        nameListe=nameListe();
       randomZitateListe=randomReihenfolge();
        Zitatview=findViewById(R.id.zitatView);
        Zitatview.setText(nameRausFiltern(randomZitateListe.get(0)));
        Zitatview.setOnClickListener(this);

        listView=findViewById(R.id.listViewSpiel);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                nameRaten(position);
            }
        });
        adapterSetzen();
    }

    /**
     * Testet ob der geratene Name zum Zitat passt
     * @param position
     */
    private void nameRaten(int position)
    {
        String zitat=randomZitateListe.get(randomListeId);
        String name=zitatRausFiltern(zitat);
        if(geraten==false ) {
            if (name.toLowerCase().equals(nameListe.get(position).toLowerCase())) {
                Zitatview.setText("Richtig! \n" + zitat);

            } else {
                Zitatview.setText("Falsch! Das Zitat: " + nameRausFiltern(zitat) + " stammt von: " + name);
            }
            geraten=true;
        }
        else
        {
            if(!ende) {
                Toast.makeText(this, "Du hast schon geraten! Drücke auf das TextFeld um das Nächste Zitat zu Sehen", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void adapterSetzen()
    {
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.row, nameListe);
        listView.setAdapter(adapter);

    }
    public ArrayList<String> crapladen()
    {

        ArrayList<String> returnArray = new ArrayList<>();
        String ladeString = this.getIntent().getExtras().getString("zitatString");
        String[] split = ladeString.split("\n");
        for(int i = 0; i < split.length - 1; i +=2)
        {
            returnArray.add(split[i]+"\n"+split[i+1]);
        }

        return returnArray;

    }
    public ArrayList<String> randomReihenfolge()
    {
        ArrayList<String> neueListe=new ArrayList<>();
        int size=zitateListe.size();
        for(int i = 0; i < size; i++) {

            int randomID = (int) (Math.random() * zitateListe.size());
            neueListe.add(zitateListe.get(randomID));
            zitateListe.remove(randomID);

        }

        return neueListe;

    }

    /**
     * gibt das Zitat zurück
     * @param zitat
     * @return
     */
    public String nameRausFiltern(String zitat)
    {
        String[] split=zitat.split("\n");
        return split[0];
    }

    /**
     * erstellt eine Liste mit allen Namen
     * @return
     */
    public ArrayList<String> nameListe()
    {
        for(int i = 0; i < zitateListe.size(); i++)
        {
            String name=zitatRausFiltern(zitateListe.get(i));
            if(!nameListe.contains(name))
            {
                nameListe.add(name);
            }
        }
        return nameListe;



    }

    /**
     * gibt den Namen zurück
     * @param zitat
     * @return
     */
    public String zitatRausFiltern(String zitat)
    {
        String[] split=zitat.split("~");
        return split[1];
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.zitatView:
                naechstesZitat();
                break;
        }
    }

    private void naechstesZitat() {
        if(randomListeId < randomZitateListe.size() -1)
        {
            randomListeId++;
            Zitatview.setText(nameRausFiltern(randomZitateListe.get(randomListeId)));
            geraten=false;
        }
        else
        {
            Zitatview.setText("Ende im Gelände");
            ende=true;
        }
    }
}