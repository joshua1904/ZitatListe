package realease.zitatl.iste;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> zitatStringListe;
    SharedPreferences sp;
    ListView listView;
    ClipboardManager cbm;
    ArrayList<Zitat> zitatListe;
    //speichert die Ids der FavorietenListe
    ArrayList<Integer> favPositionList;
    boolean favListeAngezeigt = false;
    View v;
    int bearbeitenPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("Zitate", 0);
        cbm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        zitatStringListe = new ArrayList<>();
        v = findViewById(R.id.layout);
        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!favListeAngezeigt) {
                    bearbeitenActivityWechsel(position);
                } else
                    Snackbar.make(view, "That is only possible in the normal list", BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (favListeAngezeigt) {
                    dialogErstellen("Warning!", "Do you want to delete the quote out of your favorites?", "yes", "no", "", false, "favLöschen", position);
                } else {
                    dialogErstellen("Warning!", "Do you want to delete the quote?", "yes", "no", "", false, "zitatLöschen", position);
                }

                return false;
            }
        });


    }

    @Override
    /**
     * läd Zitate in eine Liste und füllt die ListView
     */
    protected void onStart() {
        super.onStart();
        Speicher s = new Speicher();
        zitatListe = s.loadArray(getApplicationContext(), "ZitatSpeicher");
        zitatListe = s.loadArray(getApplicationContext(), "ZitatSpeicher");
        zitatStringListe = zitatezumString(zitatListe);
        adapterSetzen(zitatListe);
       listView.setSelection(bearbeitenPosition);


    }
    /**
     * Wandelt alle Zitate in einen String um
     *
     * @param zitatListe
     * @return
     */
    private ArrayList<String> zitatezumString(ArrayList<Zitat> zitatListe) {
        ArrayList<String> returnListe = new ArrayList<>();
        for (int i = 0; i < zitatListe.size(); i++) {
            returnListe.add(zitatListe.get(i).ganzesZitatGeben());
        }
        return returnListe;
    }

    public void adapterSetzen(ArrayList<Zitat> liste) {
        ZitatAdapter adapter = new ZitatAdapter(this, R.layout.zitatview, liste);
        listView.setAdapter(adapter);
    }

    private void bearbeitenActivityWechsel(int position) {
        Intent i = new Intent(MainActivity.this, bearbeiten.class);
        i.putExtra("zitat", zitatStringListe.get(position));
        i.putExtra("position", position);
        bearbeitenPosition = listView.getScrollX();
        startActivity(i);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflanter = getMenuInflater();
        inflanter.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.hinzufuegen:
                hinzufuegen();
                break;
            case R.id.sortierenName:
                spielStarten();
                break;
            case R.id.kopieren:
                alleZitateKopieren();
                break;
            case R.id.einfuegefunktion:
                kopiertenTextEinfuegen();
                break;
            case R.id.hilfe:
                hilfeWechsel();
                break;
            case R.id.favListe:
                favListeAnzeigen();
                break;
            case R.id.zurueckSetzen:
                zurueckSetzen();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void zurueckSetzen() {
        dialogErstellen("Warning!", "Do you really want to delete everything?", "yes", "no", "copy and delete", true, "zurückSetzen", 1);
    }

    /**
     * Erstellt ein dialog mit jeweiliger funktion
     *
     * @param title
     * @param Nachricht
     * @param ja
     * @param nein
     * @param vlt
     * @param dritteAntwort
     * @param funktion      ("zurückSetzen") allesLoeschen() ("zitatLöschen") zitatLöschen() "favLöschen" ausFaventfrenen()
     * @param position
     */
    public void dialogErstellen(String title, String Nachricht, String ja, String nein, String vlt, boolean dritteAntwort, String funktion, int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.dialogTheme);
        dialog.setTitle(title);
        dialog.setIcon(R.mipmap.cadaver);
        dialog.setMessage(Nachricht);
        dialog.setPositiveButton(ja, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ("zurückSetzen".equals(funktion)) {
                    allesLoeschen();
                }
                if ("zitatLöschen".equals(funktion)) {
                    zitatLoeschen(position);
                }
                if ("favLöschen".equals(funktion)) {
                    ausFavEntfernen(position);
                }

            }
        });
        dialog.setNegativeButton(nein, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        if (dritteAntwort) {
            dialog.setNeutralButton(vlt, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alleZitateKopieren();
                    allesLoeschen();
                }
            });
        }

        dialog.create().show();

    }

    private void allesLoeschen() {
        Speicher speicher = new Speicher();
        zitatListe.clear();
        speicher.loeschen(getApplicationContext(), "ZitatSpeicher");
        zitatStringListe.clear();
        adapterSetzen(zitatListe);

    }

    private void favListeAnzeigen() {
        if (favListeAngezeigt) {
            Speicher s = new Speicher();
            zitatListe = s.loadArray(getApplicationContext(), "ZitatSpeicher");
            adapterSetzen(zitatListe);
            favListeAngezeigt = false;
        } else {
            zitatListe = favListeErstellen();
            adapterSetzen(zitatListe);
            favListeAngezeigt = true;
        }
    }

    private ArrayList<Zitat> favListeErstellen() {
        favPositionList = new ArrayList<>();
        ArrayList<Zitat> favListe = new ArrayList<>();
        for (int i = 0; i < zitatListe.size(); i++) {
            if (zitatListe.get(i).fav()) {
                favPositionList.add(i);
                favListe.add(zitatListe.get(i));
            }
        }
        return favListe;
    }

    private void hilfeWechsel() {
        favListeAngezeigt = false;
        Intent i = new Intent(MainActivity.this, Hilfe.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if (favListeAngezeigt) {
            favListeAnzeigen();
        } else finish();
    }

    private void hinzufuegen() {
        Intent i = new Intent(MainActivity.this, Hinzufuegen.class);
        favListeAngezeigt = false;
        bearbeitenPosition = 0;
        startActivity(i);
    }

    private void spielStarten() {
        if (zitatStringListe.size() != 0) {
            favListeAngezeigt = false;
            Intent i = new Intent(MainActivity.this, Spiel.class);
            i.putExtra("zitatString", getZitatListenString());
            startActivity(i);
        } else
            Snackbar.make(v, "You have to add quotes first", BaseTransientBottomBar.LENGTH_LONG).show();
    }

    public void alleZitateKopieren() {
        String ladeString = getZitatListenString();
        String copyString = ladeString.replace(";", "\n");
        ClipData clip = ClipData.newPlainText("zitate", copyString);
        cbm.setPrimaryClip(clip);
        Snackbar.make(v, "finish", BaseTransientBottomBar.LENGTH_LONG).show();

    }

    private String getZitatListenString() {
        String returnString = "";
        for (int i = 0; i < zitatStringListe.size(); i++) {
            if (i != 0) {
                returnString = returnString + "\n" + zitatStringListe.get(i);
            } else returnString = zitatStringListe.get(i);

        }
        return returnString;

    }

    public void kopiertenTextEinfuegen() {
        int vorherlänge = zitatStringListe.size();
        ClipData data = cbm.getPrimaryClip();
        ClipData.Item item = data.getItemAt(0);
        String einfuegeText = item.getText().toString();
        String[] split = einfuegeText.split("\n");
        for (int i = 0; i < split.length; i += 2) {
            if (formTest(split, i)) {
                String einZitat = split[i] + "\n" + split[i + 1];
                String[] datumUndName = split[i + 1].split("~");
                String[] zitat = split[i].split("\"");

                zitatStringListe.add(einZitat);
                zitatListe.add(new Zitat(zitat[1].trim(), datumUndName[0].trim(), datumUndName[1].trim(), false, false, false));
            }
        }

        int nachherlaenge = zitatStringListe.size();
        if (nachherlaenge > vorherlänge) {
            Speicher s = new Speicher();
            s.safeArray(getApplicationContext(), zitatListe, "ZitatSpeicher");
            adapterSetzen(zitatListe);
            Snackbar.make(v, "finish", BaseTransientBottomBar.LENGTH_LONG).show();
        } else {
            Snackbar.make(v, "Sorry, that did'nt work. More information in \"Help\"", BaseTransientBottomBar.LENGTH_LONG).show();

        }
    }

    /**
     * testet ob das format der kopierten Zitate ins format passen
     */
    public boolean formTest(String[] split, int i) {
        try {
            String einZitat = split[i] + "\n" + split[i + 1];
            String[] datumUndName = split[i + 1].split("~");
            String[] zitat = split[i].split("\"");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void zitatLoeschen(int position) {
        Speicher s = new Speicher();
        zitatListe.remove(position);
        s.safeArray(getApplicationContext(), zitatListe, "ZitatSpeicher");
        adapterSetzen(zitatListe);
    }

    public void ausFavEntfernen(int position) {
        Speicher s = new Speicher();
        zitatListe = s.loadArray(getApplicationContext(), "ZitatSpeicher");
        zitatListe.get(favPositionList.get(position)).setFav(false);
        s.safeArray(getApplicationContext(), zitatListe, "ZitatSpeicher");
        favListeAngezeigt = false;
        favListeAnzeigen();
    }

}
