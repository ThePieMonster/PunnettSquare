package net.piestudios.app.punnettsquare;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v7.app.*;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VersionActivity extends AppCompatActivity implements SensorEventListener {
// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private float mLastX, mLastY, mLastZ;
    private boolean mInitialized;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final float NOISE = (float) 2.0;

    ImageView imageViewChromoX;
    ImageView imageViewChromoY;

    float gyroXaxis;
    float gyroYaxis;
    float gyroZaxis;

    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

    int counter = 0;
    int temp = 0;
// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    //private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            //mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_version);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
// ++++++++++++++++++++++++++++++++++++++++ onCreate ++++++++++++++++++++++++++++++++++++++++++++
        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        // Create Chromo X & Y images
        imageViewChromoX = (ImageView) findViewById(R.id.chromosome_x);
        Drawable drawableChromoX = getDrawable(R.drawable.chromosome_x);
        imageViewChromoX.setImageDrawable(drawableChromoX);

        imageViewChromoY = (ImageView) findViewById(R.id.chromosome_y);
        Drawable drawableChromoY = getDrawable(R.drawable.chromosome_y);
        imageViewChromoY.setImageDrawable(drawableChromoY);

        final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        final Rect parentRect = new Rect(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        final PointF offsetPoint = new PointF();



/*
        imageViewChromoX.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                final int action = motionEvent.getAction();
                switch (action & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        offsetPoint.x = motionEvent.getX();
                        offsetPoint.y = motionEvent.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        float x = motionEvent.getX();
                        float y = motionEvent.getY();

                        imageViewChromoX.offsetLeftAndRight((int) (x - offsetPoint.x));
                        imageViewChromoX.offsetTopAndBottom((int) (y - offsetPoint.y));

                        // check boundaries
                        if (imageViewChromoX.getRight() > parentRect.right) {
                            imageViewChromoX.offsetLeftAndRight(-(imageViewChromoX.getRight() - parentRect.right));
                        } else if (imageViewChromoX.getLeft() < parentRect.left) {
                            imageViewChromoX.offsetLeftAndRight((parentRect.left - imageViewChromoX.getLeft()));
                        }

                        if (imageViewChromoX.getBottom() > parentRect.bottom) {
                            imageViewChromoX.offsetTopAndBottom(-(imageViewChromoX.getBottom() - parentRect.bottom));
                        } else if (imageViewChromoX.getTop() < parentRect.top) {
                            imageViewChromoX.offsetTopAndBottom((parentRect.top - imageViewChromoX.getTop()));
                        }

                        break;
                }
                return true;
            }
        });
*/


        View.OnTouchListener listener = new View.OnTouchListener()
        {
            PointF DownPT = new PointF(); // Record Mouse Position When Pressed Down
            PointF StartPT = new PointF(); // Record Start Position of 'img'

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (v.getTag() != null && v.getTag().equals("chromX")) {
                    int eid = event.getAction();
                    switch (eid)
                    {
                        case MotionEvent.ACTION_MOVE :
                            PointF mv = new PointF(event.getX() - DownPT.x, event.getY() - DownPT.y);
                            imageViewChromoX.setX((int) (StartPT.x + mv.x));
                            imageViewChromoX.setY((int) (StartPT.y + mv.y));
                            StartPT = new PointF(imageViewChromoX.getX(), imageViewChromoX.getY());
                            break;
                        case MotionEvent.ACTION_DOWN :
                            DownPT.x = event.getX();
                            DownPT.y = event.getY();
                            StartPT = new PointF(imageViewChromoX.getX(), imageViewChromoX.getY() );
                            break;
                        case MotionEvent.ACTION_UP :
                            // Nothing have to do
                            break;
                        default :
                            break;
                    }
                }
                else if (v.getTag() != null && v.getTag().equals("chromY")) {
                    int eid = event.getAction();
                    switch (eid)
                    {
                        case MotionEvent.ACTION_MOVE :
                            PointF mv = new PointF( event.getX() - DownPT.x, event.getY() - DownPT.y);
                            imageViewChromoY.setX((int)(StartPT.x+mv.x));
                            imageViewChromoY.setY((int)(StartPT.y+mv.y));
                            StartPT = new PointF(imageViewChromoY.getX(), imageViewChromoY.getY() );
                            break;
                        case MotionEvent.ACTION_DOWN :
                            DownPT.x = event.getX();
                            DownPT.y = event.getY();
                            StartPT = new PointF(imageViewChromoY.getX(), imageViewChromoY.getY() );
                            break;
                        case MotionEvent.ACTION_UP :
                            // Nothing have to do
                            break;
                        default :
                            break;
                    }
                }
                return true;
            }
        };

        imageViewChromoX.setOnTouchListener(listener);
        imageViewChromoY.setOnTouchListener(listener);



// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        mVisible = true;
        //mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            //show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onStop()
    {
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // *** Accelerometer ***
        TextView tvX = (TextView) findViewById(R.id.a_x_axis);
        TextView tvY = (TextView) findViewById(R.id.a_y_axis);
        TextView tvZ = (TextView) findViewById(R.id.a_z_axis);
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (!mInitialized) {
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            tvX.setText("0.0");
            tvY.setText("0.0");
            tvZ.setText("0.0");
            mInitialized = true;
        } else {
            float deltaX = Math.abs(mLastX - x);
            float deltaY = Math.abs(mLastY - y);
            float deltaZ = Math.abs(mLastZ - z);
            if (deltaX < NOISE) deltaX = (float) 0.0;
            if (deltaY < NOISE) deltaY = (float) 0.0;
            if (deltaZ < NOISE) deltaZ = (float) 0.0;
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            tvX.setText(Float.toString(deltaX));
            tvY.setText(Float.toString(deltaY));
            tvZ.setText(Float.toString(deltaZ));


            // *** Gyroscope ***
            // If sensor is unreliable, return void
            if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
                return;
            }
            // Else it will output the Roll, Pitch and Yawn values
            TextView tgX = (TextView) findViewById(R.id.g_x_axis);
            TextView tgY = (TextView) findViewById(R.id.g_y_axis);
            TextView tgZ = (TextView) findViewById(R.id.g_z_axis);

            gyroXaxis = event.values[2];
            gyroYaxis = event.values[1];
            gyroZaxis = event.values[0];

            tgX.setText(Float.toString(gyroXaxis));
            tgY.setText(Float.toString(gyroYaxis));
            tgZ.setText(Float.toString(gyroZaxis));

            // testing
            tvZ.setText("Counter: " + Float.toString(counter));

            temp = (int) gyroYaxis;
            temp = temp * 1000; // raw values are to small to notice
            tvY.setText("temp: " + Float.toString(temp));

            //if (counter == 10) {
                imageMove();
            //}
            counter++;

        }
    }

    public class PointF
    {
        public float x = 0;
        public float y = 0;
        public PointF(){};
        public PointF( float _x, float _y ){ x = _x; y = _y; }
    }

    public void imageMove()
    {
        ValueAnimator animator1 = ValueAnimator.ofFloat(gyroYaxis, 0);
        ValueAnimator animator = ValueAnimator.ofInt(temp);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) imageViewChromoX.getLayoutParams();
                lp.setMargins(0, (Integer) animation.getAnimatedValue(), 0, 0);
                imageViewChromoX.setLayoutParams(lp);
            }
        });
        animator.start();
    }
// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}
