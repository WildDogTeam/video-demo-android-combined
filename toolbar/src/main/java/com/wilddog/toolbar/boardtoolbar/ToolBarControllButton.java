package com.wilddog.toolbar.boardtoolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.widget.ImageButton;


import com.wilddog.toolbar.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SuppressLint("AppCompatCustomView")
public class ToolBarControllButton extends ImageButton {
    Paint mPaint;


    @IntDef({
            ControllButtonType.CICLE,
            ControllButtonType.PEN,
            ControllButtonType.LINE,
            ControllButtonType.RECT,
            ControllButtonType.PIC,
            ControllButtonType.TEXT,
            ControllButtonType.UNDO,
            ControllButtonType.DEL

    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ControllButtonType {
        int PEN = 0;
        int LINE = 1;
        int RECT = 2;
        int CICLE = 3;
        int PIC = 4;
        int TEXT = 5;
        int UNDO = 6;
        int DEL = 7;
    }


    private int mConctrollButtonType;

    public ToolBarControllButton(Context context) {
        this(context, null);
    }

    public ToolBarControllButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public ToolBarControllButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }
    private void init(Context context, AttributeSet attrs){
        mPaint = new Paint();
        mPaint.setAntiAlias(true); //消除锯齿

        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.FloatingButton, 0, 0);
        mConctrollButtonType = attr.getInt(R.styleable.FloatingButton_fab_type, ControllButtonType.PEN);
        attr.recycle();

    }

    public @ControllButtonType int getControllButtonType(){
        return mConctrollButtonType;
    }

}
