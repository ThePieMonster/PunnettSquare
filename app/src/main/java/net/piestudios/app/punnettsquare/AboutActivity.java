package net.piestudios.app.punnettsquare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AboutActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_about);
        // ******************** End Theme Preferences ********************

        // ******************** Start About ********************
        ImageView imageViewPS;
        ImageView imageViewMono;
        ImageView imageViewDihy;

        imageViewPS = (ImageView) findViewById(R.id.imageViewPS);
        Drawable drawablePS = getDrawable(R.drawable.image_ps);
        imageViewPS.setImageDrawable(drawablePS);

        imageViewMono = (ImageView) findViewById(R.id.imageViewMono);
        Drawable drawableMono = getDrawable(R.drawable.image_mono);
        imageViewMono.setImageDrawable(drawableMono);

        imageViewDihy = (ImageView) findViewById(R.id.imageViewDihy);
        Drawable drawableDihy = getDrawable(R.drawable.image_dihy);
        imageViewDihy.setImageDrawable(drawableDihy);
        // ******************** End About ********************

    }

    public void sourceLink (View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://en.wikipedia.org/wiki/Punnett_square"));
        startActivity(intent);
    }

}
