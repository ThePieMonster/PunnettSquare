package net.piestudios.app.punnettsquare;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.text.Editable;
import android.text.TextWatcher;

import net.piestudios.app.punnettsquare.InputFilterMinMax;
import android.text.InputFilter;
import android.text.Spanned;

public class MainActivity extends AppCompatActivity {
    // Number of traits box and variable
    EditText numberoftraitslistner;
    int numberoftraits;

    // Table Layout
    // TableLayout layout = (TableLayout) findViewById(R.id.table);
    TableRow row1;
    TableRow row2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting parent 1 and 2 rows
        numberoftraitslistner = (EditText) findViewById(R.id.numberoftraitsbox);
        row1 = (TableRow) findViewById(R.id.row1);
        row2 = (TableRow) findViewById(R.id.row2);

        // Listening to numberoftraitsbox
        numberoftraitslistner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Don't care
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // InputFilterMinMax function call
                EditText et = (EditText) findViewById(R.id.numberoftraitsbox);
                et.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "5")});
            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    // Reset row values when new number is entered
                    row1.removeAllViews();
                    row2.removeAllViews();

                    // Set numberoftraits to a string (value can be viewed as a toast for testing)
                    numberoftraits = Integer.parseInt(s.toString());
                    //Toast.makeText(getApplicationContext(), numberoftraits + "", Toast.LENGTH_SHORT).show();

                    // Create trait text boxs based on numberoftraits number
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
                    //Toast.makeText(getApplicationContext(), "Can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // End listner function
    }
}
