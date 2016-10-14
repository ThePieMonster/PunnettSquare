package net.piestudios.app.punnettsquare;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.text.Editable;
import android.text.TextWatcher;

public class MainActivity extends AppCompatActivity {

    // Number of traits box and variable
    EditText numberoftraitslistner;
    int numberoftraits;

    // Table Layout
    //TableLayout layout = (TableLayout) findViewById(R.id.table);
    TableRow row1;
    TableRow row2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberoftraitslistner = (EditText) findViewById(R.id.numberoftraitsbox);
        row1 = (TableRow) findViewById(R.id.row1);
        row2 = (TableRow) findViewById(R.id.row2);

        numberoftraitslistner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Don't care
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Also don't care
            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    row1.removeAllViews();
                    row2.removeAllViews();

                    numberoftraits = Integer.parseInt(s.toString());
                    //Toast.makeText(getApplicationContext(), numberoftraits + "", Toast.LENGTH_SHORT).show();

                    for(int i = 0; i < numberoftraits; i++) {
                        row1.addView(new EditText(getApplicationContext()));
                        ((EditText)row1.getVirtualChildAt(i)).setHint("Trait " + (i+1) + ":");
                        ((EditText)row1.getVirtualChildAt(i)).setTextColor(Color.BLACK);
                        ((EditText)row1.getVirtualChildAt(i)).setHintTextColor(Color.GRAY);
                        row2.addView(new EditText(getApplicationContext()));
                        ((EditText)row2.getVirtualChildAt(i)).setHint("Trait " + (i+1) + ":");
                        ((EditText)row2.getVirtualChildAt(i)).setTextColor(Color.BLACK);
                        ((EditText)row2.getVirtualChildAt(i)).setHintTextColor(Color.GRAY);
                    }


                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
