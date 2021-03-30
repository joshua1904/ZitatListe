package realease.zitatl.iste;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Hinzufuegen extends AppCompatActivity implements View.OnClickListener {
    Button hinzufuegebtn;
    EditText nameEdit,datumEdit,zitatEdit;
    String neuesZitat="fehler";
    CheckBox weedBox,bierBox,favBox;
    // variablen sagen ob beim sagen des zitats bier oder gras im Spiel war.( fav = favorit)
    boolean weed ,bier , fav ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hinzufuegen);
        hinzufuegebtn=findViewById(R.id.Hinzufuegebtn);
        hinzufuegebtn.setOnClickListener(this);
        nameEdit=findViewById(R.id.NameEdit);
        datumEdit=findViewById(R.id.DatumEdit);
        datumEdit.setText(datumGeben());
        zitatEdit=findViewById(R.id.ZitatEdit);
        weedBox = findViewById(R.id.weedBox);
        weedBox.setOnClickListener(this);
        bierBox = findViewById(R.id.bierBox);
        bierBox.setOnClickListener(this);
        favBox = findViewById(R.id.sternBox);
        favBox.setOnClickListener(this);


    }
    public String datumGeben()
    {
       String currentTime = Calendar.getInstance().getTime().toString();
       String[] datumUndJahr=currentTime.split(" ");
       return datumUndJahr[2]+" "+datumUndJahr[1]+  " "+datumUndJahr[5];
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.Hinzufuegebtn:
                hinzufuegen();
                break;
            case R.id.weedBox:
                if(weedBox.isChecked())
                {
                    weed = true;
                }
                else
                {
                    weed = false;
                }
                break;
            case R.id.bierBox:
                if(bierBox.isChecked())
                {
                    bier = true;
                }
                else
                {
                    bier= false;
                }
                break;
            case R.id.sternBox:
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

    private void hinzufuegen()
    {
        String zitat = zitatEdit.getText().toString().trim();
        String datum = datumEdit.getText().toString().trim();
        String name = nameEdit.getText().toString().trim();
        if(!zitat.contains("\n") || !datum.contains("\n") || !name.contains("\n") )
        {
            if (zitat.equals("") || datum.equals("") || name.equals(""))
            {
                Toast.makeText(this, "You have to fill all EditTexts :)", Toast.LENGTH_LONG).show();
            } else speichern(zitat, datum, name, weed, bier, fav);
        }
        else Toast.makeText(this, "Do not use the enter key :)", Toast.LENGTH_LONG).show();


    }
    public void speichern(String zitat,String datum,String name,boolean weed,boolean bier,boolean fav)
    {
       Speicher speicher = new Speicher();
       Zitat ganzesZitat = new Zitat(zitat,datum,name,weed,bier,fav);
        speicher.safeZitat(getApplicationContext(),ganzesZitat,"ZitatSpeicher");
        finish();
    }

}