package com.example.android.battleships;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

public class Cell extends ImageButton {
    private int id;
    private int imageId;
    private int y;
    private int x;
    private boolean isEmpty = true;
    private boolean isHit = false;

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
    }
    public Cell(Context context, AttributeSet attrs, int i) {
        super(context, attrs);
        setImage(R.drawable.cloud);
        setPadding(0, 0, 0, 0);
        setId(i);
    }

    public void setImage(int i)
    {
        imageId=i;
        setImageResource(i);
        //invalidate();
    }

    public void setId(int i){
        super.setId(i);
        id = i;
    }

    public void setCoords(int x, int y){
        this.x = x;
        this.y = y;
    }



    public boolean isEmpty(){
        return isEmpty;
    }

    public boolean isHit(){
        return isHit;
    }

    public void hit(){
        isHit=true;
    }

    public void build(boolean i){
        isEmpty = !i;
    }

    public int getId(){return id;}
    public int getYCoord(){return y;}
    public int getXCoord(){return x;}
    public int getImageId(){return imageId;}

}
