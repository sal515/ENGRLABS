package ca.engrLabs_390.engrlabs;

import android.content.ClipData;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class canvasActivity extends AppCompatActivity {

    MyCanvas myCanvas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_canvas);
        myCanvas = new MyCanvas(this);
        myCanvas.setBackgroundColor(Color.RED);
        setContentView(myCanvas);
    }

}
