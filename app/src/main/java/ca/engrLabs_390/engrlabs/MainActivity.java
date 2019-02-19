// This is the barebone Project
package ca.engrLabs_390.engrlabs;

import android.content.Intent;
import android.drm.DrmStore;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    Button eight = null;
    Button nine = null;
    Button ten = null;
    Button search = null;
    RelativeLayout searchlayout = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Labs");
        //ab.setDisplayHomeAsUpEnabled();
        eight = findViewById(R.id.eightButton);
        nine = findViewById(R.id.nineButton);
        ten = findViewById(R.id.tenButton);
        search = findViewById(R.id.searchButton);
        searchlayout = findViewById(R.id.searchBackground);
        setUpButton(eight);
        setUpButton(nine);
        setUpButton(ten);
        setUpButton(search);
    }

    private void setUpButton(final Button button){
        button.setOnTouchListener(new Button.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (button != search){
                    button.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));//R.color.colorPrimary);
                }
                else{
                    searchlayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));//R.color.colorPrimary);
                }
                return false;
            }
        });

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button != search) {
                    button.setBackgroundColor(Color.argb(0, 0, 0, 0));
                }
                else{
                    searchlayout.setBackgroundColor(Color.argb(0, 0, 0, 0));
                }
            }
        });
    }

}
