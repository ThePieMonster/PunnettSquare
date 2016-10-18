package net.piestudios.app.punnettsquare;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    TableLayout square;

    String[] parent1D;
    String[] parent1R;
    String[] parent2D;
    String[] parent2R;

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
        square = (TableLayout) findViewById(R.id.square);



        // Listening to numberoftraitsbox
        numberoftraitslistner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Don't care
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // InputFilterMinMax function call
                numberoftraitslistner.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "3")});
            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    // Reset row values when new number is entered
                    row1.removeAllViews();
                    row2.removeAllViews();
                    if (numberoftraitslistner.getText().toString().length() == 0) {
                        row1.addView(new EditText(getApplicationContext()));
                        //((EditText) row1.getVirtualChildAt(0)).setText("NA");
                        ((EditText) row1.getVirtualChildAt(0)).setVisibility(View.INVISIBLE);
                        row2.addView(new EditText(getApplicationContext()));
                        //((EditText) row2.getVirtualChildAt(0)).setText("NA");
                        ((EditText) row2.getVirtualChildAt(0)).setVisibility(View.INVISIBLE);
                    }


                    // Set numberoftraits to a string (value can be viewed as a toast for testing)
                    numberoftraits = Integer.parseInt(s.toString());
                    //Toast.makeText(getApplicationContext(), numberoftraits + "", Toast.LENGTH_SHORT).show();

                    // Create trait text boxs based on numberoftraits number
                    for(int i = 0; i < numberoftraits; i++) {
                        row1.addView(new EditText(getApplicationContext()));
                        ((EditText)row1.getVirtualChildAt(i)).setHint("Trait " + (i+1) + ":");
                        ((EditText)row1.getVirtualChildAt(i)).setTextColor(Color.WHITE);
                        ((EditText)row1.getVirtualChildAt(i)).setHintTextColor(Color.GRAY);
                        ((EditText)row1.getVirtualChildAt(i)).setFilters(new InputFilter[] {new InputFilter.LengthFilter(2)});
                        row2.addView(new EditText(getApplicationContext()));
                        ((EditText)row2.getVirtualChildAt(i)).setHint("Trait " + (i+1) + ":");
                        ((EditText)row2.getVirtualChildAt(i)).setTextColor(Color.WHITE);
                        ((EditText)row2.getVirtualChildAt(i)).setHintTextColor(Color.GRAY);
                        ((EditText)row2.getVirtualChildAt(i)).setFilters(new InputFilter[] {new InputFilter.LengthFilter(2)});
                    }








                } catch (Exception e){
                    //Toast.makeText(getApplicationContext(), "Can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // End listener function
    }

    public void onClick(View view) {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        parent1D = new String[row1.getChildCount()];
        parent1R = new String[row1.getChildCount()];
        parent2D = new String[row1.getChildCount()];
        parent2R = new String[row1.getChildCount()];
        square.removeAllViews();
        int length = (int) Math.pow(2, row1.getChildCount());
        int size = 0;
        switch (row1.getChildCount()) {
            case 1:
                size = 250;
                break;
            case 2:
                size = 150;
                break;
            case 3:
                size = 74;
                break;
            default:
                break;
        }


        TableRow.LayoutParams squareParams = new TableRow.LayoutParams(
                size,
                size
        );
        squareParams.setMargins(15, 15, 15, 15);

        for (int i = 0; i < row1.getChildCount(); i++) {
            if (row1.getVirtualChildAt(i) instanceof EditText){
                parent1D[i] = ((EditText)row1.getVirtualChildAt(i)).getText().toString().substring(0,1);
                parent1R[i] = ((EditText)row1.getVirtualChildAt(i)).getText().toString().substring(1);
            }
        }

        for (int i = 0; i < row2.getChildCount(); i++) {
            if (row2.getVirtualChildAt(i) instanceof EditText){
                parent2D[i] = ((EditText)row2.getVirtualChildAt(i)).getText().toString().substring(0,1);
                parent2R[i] = ((EditText)row2.getVirtualChildAt(i)).getText().toString().substring(1);
            }
        }

        for (int i = 0; i < length + 1; i++) {
            square.addView(new TableRow(getApplicationContext()));
            for (int j = 0; j < length + 1; j++) {
                ((TableRow)square.getChildAt(i)).addView(new TextView(getApplicationContext()));
                ((TextView)((TableRow)square.getChildAt(i)).getChildAt(j)).setBackgroundColor(Color.WHITE);
                ((TextView)((TableRow)square.getChildAt(i)).getChildAt(j)).setLayoutParams(squareParams);
            }
        }

        ((TextView)((TableRow)square.getChildAt(0)).getChildAt(0)).setBackgroundColor(Color.BLACK);

        // Mapping traits to alleles
        switch (row1.getChildCount()) {
            case 1:
                ((TextView)((TableRow) square.getChildAt(0)).getChildAt(1)).setText(parent1D[0]);
                ((TextView)((TableRow) square.getChildAt(0)).getChildAt(1)).setTextColor(Color.BLACK);
                ((TextView)((TableRow) square.getChildAt(0)).getChildAt(2)).setText(parent1R[0]);
                ((TextView)((TableRow) square.getChildAt(0)).getChildAt(2)).setTextColor(Color.BLACK);
                ((TextView)((TableRow) square.getChildAt(1)).getChildAt(0)).setText(parent2D[0]);
                ((TextView)((TableRow) square.getChildAt(1)).getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView)((TableRow) square.getChildAt(2)).getChildAt(0)).setText(parent2R[0]);
                ((TextView)((TableRow) square.getChildAt(2)).getChildAt(0)).setTextColor(Color.BLACK);
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }
    }
}
