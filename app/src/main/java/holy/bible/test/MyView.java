package holy.bible.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Author: chenzhongkai
 * Date: 2019-07-13
 * Describe: 测试view
 */
public class MyView extends View {
    Paint paint;

    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setColor(Color.RED);
        for (int a :
                new ArrayList<Integer>())
            Log.d("dsf", "dfs");
        System.out.println();

    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        Path path = new Path();                 // 创建Path并添加一些内容
        path.lineTo(400, -100);
        path.lineTo(200, 100);
        path.close();
        path.addCircle(-100, 0, 100, Path.Direction.CW);
//        path.rewind();
        canvas.drawPath(path, paint);

    }


}
