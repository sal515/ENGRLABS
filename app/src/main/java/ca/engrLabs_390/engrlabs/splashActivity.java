package ca.engrLabs_390.engrlabs;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class splashActivity extends AppCompatActivity {

    TextView splashText = null;
    int SPLASH_TIME = 2000;
    int textSize = 0;
    static boolean startup = true;
    ImageView image;
    float opacity = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar ab = getSupportActionBar();
        ab.hide();
        image = (ImageView) findViewById(R.id.imageView);
        image.setImageResource(R.drawable.old_personal_computer);
        image.setAlpha(opacity);

        splashText = (TextView) findViewById(R.id.splashText);
        RotateAnimation animation = new RotateAnimation(0, 1440,RotateAnimation.RELATIVE_TO_SELF, 0.9f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        //RotateAnimation animation = new RotateAnimation(0,1440,0.5f,0.5f);
        animation.setDuration(800);
        animation.setFillAfter(true);
        splashText.startAnimation(animation);
        Canvas canvas;

        new CountDownTimer(2000, 1) {
            public void onTick(long millisUntilFinished) {
                if (textSize < 80){
                    splashText.setTextSize(textSize+=2);
                }
                image.setAlpha(opacity);
                opacity+=0.025f;

            }
            public void onFinish() {
                startup = false;
                //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                startActivity(new Intent(getApplicationContext(), canvasActivity.class));
            }
        }.start();
    }

    @Override
    protected void onStart(){
        super.onStart();
        if (startup == false){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        if (startup == false){

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
}
