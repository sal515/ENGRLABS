package ca.engrLabs_390.engrlabs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class MyCanvas extends View {

    Paint paint;
    Rect rect;
    public MyCanvas(Context context) {
        super(context);
        paint = new Paint();
        rect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(3);

        canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight()/2,paint);
    }
}
