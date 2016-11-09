package net.piestudios.app.punnettsquare;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.preference.ListPreference;
import android.content.SharedPreferences;

import net.piestudios.app.punnettsquare.InputFilterMinMax;
import android.text.InputFilter;
import android.text.Spanned;

public class MainActivity extends AppCompatActivity {

    // ********** Menu **********
    // Show menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // Interact with menu
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.settings:
                Intent pref = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(pref);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // ********** Activity **********
    // Number of traits box and variable
    EditText numberoftraitslistner;
    int numberoftraits;
    TableLayout square;

    String[][] parent1;
    String[][] parent2;
    TableRow row1;
    TableRow row2;

    // ******************** Start Accent Color ********************
    int[][] states = new int[][] {
            new int[] { android.R.attr.state_focused}, // enabled
            //new int[] {-android.R.attr.state_enabled}, // disabled
            //new int[] {-android.R.attr.state_checked}, // unchecked
            new int[] { android.R.attr.state_window_focused}  // pressed
    };

    int[] colors = new int[] {
            Color.GREEN,
            //Color.BLUE,
            //Color.YELLOW,
            Color.GRAY
    };

    ColorStateList myColorAccentList = new ColorStateList(states, colors);
    // ******************** End Accent Color ********************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ******************** Start Theme Preferences ********************
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        switch(sp.getString("theme_list", "-1")) {
            case "0":
                setTheme(R.style.AppTheme);
                break;
            case "1":
                setTheme(R.style.AppThemeDark);
                break;
            default:
                break;
        }
        setContentView(R.layout.activity_main);
        // ******************** End Theme Preferences ********************


        // Lock rotation to portait
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


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
                numberoftraitslistner.setFilters(new InputFilter[]{new InputFilterMinMax("1", "3")});
                square.removeAllViews();
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
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

                        //Add code to reset boxes after new number is entered
                    }


                    // Set numberoftraits to a string (value can be viewed as a toast for testing)
                    numberoftraits = Integer.parseInt(s.toString());
                    //Toast.makeText(getApplicationContext(), numberoftraits + "", Toast.LENGTH_SHORT).show();

                    // Create trait text boxs based on numberoftraits number
                    for (int i = 0; i < numberoftraits; i++) {
                        row1.addView(new EditText(getApplicationContext()));
                        ((EditText) row1.getVirtualChildAt(i)).setHint("Trait " + (i + 1) + ":");
                        ((EditText) row1.getVirtualChildAt(i)).setBackgroundTintList(myColorAccentList);
                        //((EditText) row1.getVirtualChildAt(i)).setTextColor(Color.WHITE);
                        //((EditText) row1.getVirtualChildAt(i)).setHintTextColor(Color.GRAY);
                        ((EditText) row1.getVirtualChildAt(i)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
                        row2.addView(new EditText(getApplicationContext()));
                        ((EditText) row2.getVirtualChildAt(i)).setHint("Trait " + (i + 1) + ":");
                        ((EditText) row2.getVirtualChildAt(i)).setBackgroundTintList(myColorAccentList);
                        //((EditText) row2.getVirtualChildAt(i)).setTextColor(Color.WHITE);
                        //((EditText) row2.getVirtualChildAt(i)).setHintTextColor(Color.GRAY);
                        ((EditText) row2.getVirtualChildAt(i)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
                    }


                } catch (Exception e) {
                    //Toast.makeText(getApplicationContext(), "Can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // End listener function
    }

    public void onClick(View view) {
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            parent1 = new String[row1.getChildCount()][2];
            parent2 = new String[row1.getChildCount()][2];
            square.removeAllViews();
            int length = (int) Math.pow(2, row1.getChildCount());
            int size = 0;
            switch (row1.getChildCount()) {
                case 1:
                    size = 250;
                    break;
                case 2:
                    size = 140;
                    break;
                case 3:
                    size = 130;
                    //Need to determine a good size for these boxes or change font size.
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
                if (row1.getVirtualChildAt(i) instanceof EditText) {
                    parent1[i][0] = ((EditText) row1.getVirtualChildAt(i)).getText().toString().substring(0, 1).toUpperCase();
                    parent1[i][1] = ((EditText) row1.getVirtualChildAt(i)).getText().toString().substring(1).toLowerCase();
                }
            }

            for (int i = 0; i < row2.getChildCount(); i++) {
                if (row2.getVirtualChildAt(i) instanceof EditText) {
                    parent2[i][0] = ((EditText) row2.getVirtualChildAt(i)).getText().toString().substring(0, 1).toUpperCase();
                    parent2[i][1] = ((EditText) row2.getVirtualChildAt(i)).getText().toString().substring(1).toLowerCase();
                }
            }

            for (int i = 0; i < length + 1; i++) {
                square.addView(new TableRow(getApplicationContext()));
                for (int j = 0; j < length + 1; j++) {
                    ((TableRow) square.getChildAt(i)).addView(new TextView(getApplicationContext()));
                    ((TextView) ((TableRow) square.getChildAt(i)).getChildAt(j)).setBackgroundColor(Color.MAGENTA); // Delete this when done testing square creation
                    //((TextView)((TableRow)square.getChildAt(i)).getChildAt(j)).setTextColor(Color.BLACK);
                    ((TextView) ((TableRow) square.getChildAt(i)).getChildAt(j)).setLayoutParams(squareParams);
                }
            }

            //((TextView)((TableRow)square.getChildAt(0)).getChildAt(0)).setBackgroundColor(Color.BLACK);

            // Mapping traits to alleles
            switch (row1.getChildCount()) {
                case 1:
                /*((TextView)((TableRow) square.getChildAt(0)).getChildAt(1)).setText(parent1[0][0]);
                //((TextView)((TableRow) square.getChildAt(0)).getChildAt(2)).setText(parent1[0][1]);
                //((TextView)((TableRow) square.getChildAt(1)).getChildAt(0)).setText(parent2[0][0]);
                ((TextView)((TableRow) square.getChildAt(2)).getChildAt(0)).setText(parent2[0][1]);
                */
                    String[] parent1Combo1 = new String[length];
                    String[] parent2Combo1 = new String[length];
                    String parentCombo1Result1 = new String();
                    String parentCombo1Result2 = new String();
                    String parentCombo1ResultFinal1 = new String();
                    String parentCombo2ResultFinal1 = new String();


                    for (int i = 0; i < 2; i++) {
                        ((TextView) ((TableRow) square.getChildAt(0)).getChildAt(i + 1)).setText(parent1[0][i]);
                    }

                    for (int i = 0; i < 2; i++) {
                        ((TextView) ((TableRow) square.getChildAt(i + 1)).getChildAt(0)).setText(parent2[0][i]);
                    }

                    for (int i = 0; i < 2; i++) {
                        parent1Combo1[i] = parent1[0][i];
                        parent2Combo1[i] = parent2[0][i];
                    }

                    for (int i = 0; i < length; i++) {
                        for (int j = 0; j < length; j++) {
                            parentCombo1Result2 = parent1Combo1[j];
                            parentCombo1Result1 = parent2Combo1[i];
                            parentCombo1ResultFinal1 = parentCombo1Result2.substring(1) + parentCombo1Result1.substring(1);
                            parentCombo2ResultFinal1 = parentCombo1Result2.substring(0) + parentCombo1Result1.substring(0);
                            parentCombo2ResultFinal1 = parentCombo2ResultFinal1 + parentCombo1ResultFinal1;

                            ((TextView) ((TableRow) square.getChildAt(i + 1)).getChildAt(j + 1)).setText(parentCombo2ResultFinal1);
                        }
                    }
                    break;
                case 2:
                    String[] parent1Combo = new String[length];
                    String[] parent2Combo = new String[length];
                    String parentComboResult1 = new String();
                    String parentComboResult2 = new String();
                    String parentCombo1ResultFinal = new String();
                    String parentCombo2ResultFinal = new String();

                    int cnt = 0;
                    for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < 2; j++) {
                            parent1Combo[cnt] = parent1[0][i] + parent1[1][j];
                            cnt++;
                        }
                    }
                    cnt = 0;
                    for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < 2; j++) {
                            parent2Combo[cnt] = parent2[0][i] + parent2[1][j];
                            cnt++;
                        }
                    }
                    for (int i = 0; i < 4; i++) {
                        ((TextView) ((TableRow) square.getChildAt(0)).getChildAt(i + 1)).setText(parent1Combo[i]);
                    }
                    for (int i = 0; i < 4; i++) {
                        ((TextView) ((TableRow) square.getChildAt(i + 1)).getChildAt(0)).setText(parent2Combo[i]);
                    }

                    for (int i = 0; i < length; i++) {
                        for (int j = 0; j < length; j++) {
                            parentComboResult2 = parent1Combo[j];
                            parentComboResult1 = parent2Combo[i];
                            parentCombo1ResultFinal = parentComboResult2.substring(1, 2) + parentComboResult1.substring(1, 2);
                            parentCombo2ResultFinal = parentComboResult2.substring(0, 1) + parentComboResult1.substring(0, 1);
                            parentCombo2ResultFinal = parentCombo2ResultFinal + parentCombo1ResultFinal;

                            ((TextView) ((TableRow) square.getChildAt(i + 1)).getChildAt(j + 1)).setText(parentCombo2ResultFinal);
                        }
                    }
                    break;
                case 3:
                    String[] parent1Combo3 = new String[length];
                    String[] parent2Combo3 = new String[length];
                    String parentCombo3Result1 = new String();
                    String parentCombo3Result2 = new String();
                    String parentCombo3Result3 = new String();
                    String parentCombo1ResultFinal3 = new String();
                    String parentCombo2ResultFinal3 = new String();
                    String parentCombo3ResultFinal3 = new String();
                    String parentComboFinal3 = new String();
                    //variable names are messy, will worry about it later.

                    int trait3count = 0;
                    for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < 2; j++) {
                            for (int d = 0; d < 2; d++) {
                                parent1Combo3[trait3count] = parent1[0][i] + parent1[1][j] + parent1[2][d];
                                trait3count++;
                            }
                        }
                    }
                    trait3count = 0;
                    for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < 2; j++) {
                            for (int d = 0; d < 2; d++) {
                                parent2Combo3[trait3count] = parent2[0][i] + parent2[1][j] + parent2[2][d];
                                trait3count++;
                            }
                        }
                    }

                    for (int i = 0; i < 8; i++) {
                        ((TextView) ((TableRow) square.getChildAt(0)).getChildAt(i + 1)).setText(parent1Combo3[i]);
                    }
                    for (int i = 0; i < 8; i++) {
                        ((TextView) ((TableRow) square.getChildAt(i + 1)).getChildAt(0)).setText(parent2Combo3[i]);
                    }

                    for (int i = 0; i < length; i++) {
                        for (int j = 0; j < length; j++) {
                            for (int d = 0; d < length; d++) {

                                parentCombo3Result2 = parent1Combo3[j];
                                parentCombo3Result1 = parent2Combo3[i];
                                parentCombo3ResultFinal3 = parentCombo3Result2.substring(2, 3) + parentCombo3Result1.substring(2, 3);
                                parentCombo1ResultFinal3 = parentCombo3Result2.substring(1, 2) + parentCombo3Result1.substring(1, 2);
                                parentCombo2ResultFinal3 = parentCombo3Result2.substring(0, 1) + parentCombo3Result1.substring(0, 1);
                                parentComboFinal3 = parentCombo2ResultFinal3 + parentCombo1ResultFinal3 + parentCombo3ResultFinal3;

                                ((TextView) ((TableRow) square.getChildAt(i + 1)).getChildAt(j + 1)).setText(parentComboFinal3);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Traits can't be empty", Toast.LENGTH_SHORT).show();
        }
    }
}
