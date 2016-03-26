package com.example.android.battleships;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class Cell extends ImageButton {
    int id;

    public Cell(Context context) {
        super(context);
        setImage(R.drawable.cloud);
        setPadding(0, 0, 0, 0);
    }
    public Cell(Context context, int i) {
        super(context);
        setImage(R.drawable.cloud);
        setPadding(0, 0, 0, 0);
        setId(i);
        id=i;
    }
    public Cell(Context context, AttributeSet attrs, int i) {
        super(context, attrs);
        setImage(R.drawable.cloud);
        setPadding(0, 0, 0, 0);
        setId(i);
        id=i;
    }

    public void setImage(int i)
    {
        setImageResource(i);
        invalidate();
    }
    public String toString()
    {
        return "cell: "+(id/10)+","+(id%10);
    }

}
