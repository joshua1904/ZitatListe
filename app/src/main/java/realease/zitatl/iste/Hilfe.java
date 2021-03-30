package realease.zitatl.iste;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Hilfe extends AppCompatActivity {
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hilfe);
        text=findViewById(R.id.textView2);
        text.setText("Help:\n\n" +
                "With the import function, you can import quotes into the app by copying the quote Text. \n"+
                "Use for a successfull import this format: \n"+
                "\"Zitat\"\n" +
                "datum ~name~\n");


    }
}